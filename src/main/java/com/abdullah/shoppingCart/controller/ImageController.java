package com.abdullah.shoppingCart.controller;


import com.abdullah.shoppingCart.dto.ImageDTO;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Image;
import com.abdullah.shoppingCart.response.ApiResponse;
import com.abdullah.shoppingCart.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Value("${app.upload-dir}")
    private String uploadDir;;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(
            @RequestParam List<MultipartFile> files,
            @RequestParam Long productId) {
        try {
            List<ImageDTO> imageDTOS = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success !", imageDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed! " + e.getMessage(), null));
        }
    }



    @GetMapping("download/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            byte[] imageBytes = Files.readAllBytes(Paths.get(image.getDownloadUrl().replace("/images/", uploadDir + "/")));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .body(imageBytes);
        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Image not found: " + e.getMessage(), null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to read image: " + e.getMessage(), null));
        }
    }




    // todo : Try this method later to check logic
    @PutMapping(value = "/image/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateImage(
            @PathVariable Long imageId,
            @RequestParam("file") MultipartFile file) {

        try {

            ResponseEntity<ApiResponse> body = validateImage(file);
            if (body != null) return body;
            // Update the image
            imageService.deleteImageLocally(imageService.getImageById(imageId));
            imageService.updateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Image updated successfully",null));

        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to update image", null));
        }
    }

    private static ResponseEntity<ApiResponse> validateImage(MultipartFile file) {
        // Validate input
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Image file cannot be null or empty", null));
        }
        return null;
    }


    @DeleteMapping(value = "/image/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(
            @PathVariable Long imageId) {

        try {
            // Delete the image
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));

        } catch (ResourcesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(String.format("Image with ID %d is not found", imageId), null));

        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(String.format("Image with ID %d can't be accessed or found", imageId), null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to update image", null));
        }
    }



}
