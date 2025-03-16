package com.abdullah.shoppingCart.repository;

import com.abdullah.shoppingCart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryName(String category);


    List<Product> findAllByBrand(String brand);

    List<Product> findAllByCategoryNameAndBrand(String category, String brand);

    List<Product> findAllByName(String name);

    List<Product> findAllByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);
}
