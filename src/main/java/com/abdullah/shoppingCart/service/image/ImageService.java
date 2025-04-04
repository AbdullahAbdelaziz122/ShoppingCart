package com.abdullah.shoppingCart.service.image;

import com.abdullah.shoppingCart.dto.ImageDTO;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.mapper.ImageMapper;
import com.abdullah.shoppingCart.model.Image;
import com.abdullah.shoppingCart.model.Product;
import com.abdullah.shoppingCart.repository.ImageRepository;
import com.abdullah.shoppingCart.repository.ProductRepository;
import com.abdullah.shoppingCart.service.product.IProductService;
import com.abdullah.shoppingCart.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ImageMapper imageMapper;
    private final StorageService storageService;
    private final ProductRepository productRepository;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No image found with id: " + id));
    }





    @Override
    public void deleteImageById(Long id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("No image found with id:" + id));
        storageService.deleteImage(image.getDownloadUrl());
        imageRepository.delete(image);

    }

    @Override
    public void deleteImageLocally(Image image) throws IOException{
        storageService.deleteImage(image.getDownloadUrl());
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId){

        // check product
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourcesNotFoundException(String.format("product with ID %d not found", productId))
        );

        List<Image> images = files.stream().map(file -> {

            try {
                String downloadUrl = storageService.uploadImage(file, productId);
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setDownloadUrl(downloadUrl);
                image.setProduct(product);
                return imageRepository.save(image);

            } catch (IOException e) {
                throw new RuntimeException("Failed to Upload image: " + e.getMessage());
            }
        }).toList();

        return images.stream()
                .map(imageMapper::toDto).toList();
    }


    @Override
    public void updateImage(MultipartFile file, Long imageId){
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourcesNotFoundException(String.format("Image with ID %d not found", imageId)));

        try {
            String downloadUrl = storageService.uploadImage(file, image.getProduct().getId());
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setDownloadUrl(downloadUrl);
            imageRepository.save(image);

        }catch (IOException e)
        {
            throw new RuntimeException("Image Update Failed" + e.getMessage());
        }
    }

    @Override
    public Image getImageByUrl(String url){
        return imageRepository.getImageByDownloadUrl(url).orElseThrow( () ->
                new ResourcesNotFoundException(String.format("Image with URL %s not Found", url))
        );
    }
}
