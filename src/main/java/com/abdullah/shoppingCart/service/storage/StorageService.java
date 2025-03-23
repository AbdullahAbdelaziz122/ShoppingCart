package com.abdullah.shoppingCart.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadImage(MultipartFile file, Long productId) throws IOException;
}
