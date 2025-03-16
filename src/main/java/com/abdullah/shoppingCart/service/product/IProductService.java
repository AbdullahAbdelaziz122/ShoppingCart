package com.abdullah.shoppingCart.service.product;

import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.model.Product;

import java.util.List;

public interface IProductService {

    Product addProduct(ProductDTO productDTO);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProductById(ProductDTO product, Long id);

    List<Product> getAllProduct();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);


}
