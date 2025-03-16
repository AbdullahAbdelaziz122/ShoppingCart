package com.abdullah.shoppingCart.service.image;

import com.abdullah.shoppingCart.dto.ImageDTO;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Image;
import com.abdullah.shoppingCart.model.Product;
import com.abdullah.shoppingCart.repository.ImageRepository;
import com.abdullah.shoppingCart.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::save, ()->{
            throw new ResourcesNotFoundException("No image found with id: " +id);
        });
    }

//    @Override
//    @Transactional
//    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
//        // validate input
//        if(files.isEmpty() || files == null){
//            throw new IllegalArgumentException("Image List cannot be empty");
//        }
//
//
//        // fetch product
//        Product product = productService.getProductById(productId);
//
//        List<ImageDTO> savedImagesDTOs = new ArrayList<>();
//
//        for(MultipartFile file : files){
//            try {
//                Image image = new Image();
//                image.setFileName(file.getOriginalFilename());
//                image.setImageData(file.getBytes());
//                image.setFileType(file.getContentType());
//                image.setProduct(product);
//
//                String urlBuilder = "api/v1/images/image/download/";
//                String downloadUrl =urlBuilder+ image.getId();
//                image.setDownloadUrl(downloadUrl);
//                Image savedImage = imageRepository.save(image);
//
//                savedImage.setDownloadUrl(urlBuilder + savedImage.getId());
//                imageRepository.save(savedImage);
//
//                ImageDTO imageDTO = new ImageDTO();
//                imageDTO.setImageId(savedImage.getId());
//                imageDTO.setImageName(savedImage.getFileName());
//                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
//                savedImagesDTOs.add(imageDTO);
//
//
//            }catch (IOException e){
//                throw new RuntimeException(e);
//            }
//        }
//
//    return null;
//
//    }

    @Transactional
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        // Validate input
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Image list cannot be null or empty");
        }

        // Fetch product
        Product product = productService.getProductById(productId);

        // Prepare images
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) { // Skip null/empty files
                try {
                    Image image = new Image();
                    image.setFileName(file.getOriginalFilename());
                    image.setImageData(file.getBytes());
                    image.setFileType(file.getContentType());
                    image.setProduct(product);
                    images.add(image);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process file: " + file.getOriginalFilename(), e);
                }
            }
        }

        // Save all images in one go
        List<Image> savedImages = imageRepository.saveAll(images);

        // Map to DTOs with download URLs
        List<ImageDTO> savedImagesDTOs = new ArrayList<>();
        String urlBuilder = "api/v1/images/image/download/";
        for (Image savedImage : savedImages) {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImageId(savedImage.getId());
            imageDTO.setImageName(savedImage.getFileName());
            imageDTO.setDownloadUrl(urlBuilder + savedImage.getId());
            savedImagesDTOs.add(imageDTO);

            // Optionally persist the download URL if needed
            savedImage.setDownloadUrl(imageDTO.getDownloadUrl());
        }
        imageRepository.saveAll(savedImages); // Update URLs if persisted

        return savedImagesDTOs;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        // validate file
        if (file.isEmpty() || file == null){
            throw new IllegalArgumentException("Image file cannot be null or empty");
        }

        // fetch existing image
        Image existingImage = getImageById(imageId);

        try {
            existingImage.setImageData(file.getBytes());
            existingImage.setFileName(file.getOriginalFilename());
            existingImage.setFileType(file.getContentType());

        } catch (IOException e) {
            throw new RuntimeException("Failed to update image file", e);
        }

        imageRepository.save(existingImage);

    }
}
