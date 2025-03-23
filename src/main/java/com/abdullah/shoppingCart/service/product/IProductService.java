package com.abdullah.shoppingCart.service.product;

import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.model.Product;

import java.util.List;

public interface IProductService {

    Product addProduct(ProductDTO productDTO);

    Product getProductById(Long id);

    Product getProductBySlug(String slug);

    void deleteProductById(Long id);
    Product updateProductById(ProductDTO product, Long id);

    List<Product> getAllProduct();

    List<Product> filterProducts(String name, String brand, String category);
    Long countProductsByBrandAndName(String brand, String name);


}
