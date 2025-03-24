package com.abdullah.shoppingCart.service.product;

import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.model.Product;

import java.util.List;

public interface IProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    ProductDTO getProductBySlug(String slug);

    void deleteProductById(Long id);
    ProductDTO updateProductById(ProductDTO product, Long id);

    List<ProductDTO> getAllProduct();

    List<ProductDTO> filterProducts(String name, String brand, String category);
    Long countProductsByBrandAndName(String brand, String name);


}
