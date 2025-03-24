package com.abdullah.shoppingCart.service.image;

import com.abdullah.shoppingCart.dto.ImageDTO;
import com.abdullah.shoppingCart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
    void deleteImageLocally(Image image) throws IOException;

    List<ImageDTO> saveImages(List<MultipartFile> files, Long productId);
    Image getImageById(Long imageId);
    void updateImage(MultipartFile file, Long imageId);
    void deleteImageById(Long imageId) throws IOException;

    Image getImageByUrl(String url);
}