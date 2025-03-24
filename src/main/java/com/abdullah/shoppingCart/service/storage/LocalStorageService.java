package com.abdullah.shoppingCart.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    public String uploadImage(MultipartFile file, Long productId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileExtension = getFileExtension(file);
        if (!isValidFileType(fileExtension)) {
            throw new IllegalArgumentException("Invalid file type. Allowed: jpg, jpeg, png");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new IllegalArgumentException("File too large. Max size: 5MB");
        }

        String fileName = productId + "-" + UUID.randomUUID() + "." + fileExtension;
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent()); // Ensure directory exists
        Files.write(filePath, file.getBytes());

        return apiPrefix + "/images/" + fileName; // Relative URL for serving
    }

    private boolean isValidFileType(String fileExtension) {
        return fileExtension != null && List.of("jpg", "jpeg", "png").contains(fileExtension);
    }



    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null || !originalFileName.contains(".")){
            return "jpg";
        }
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

    }

    @Override
    public void deleteImage(String downloadUrl) throws IOException {

        String filename = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir,filename);
        Files.delete(filePath);
    }
}
