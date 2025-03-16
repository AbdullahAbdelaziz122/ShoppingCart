package com.abdullah.shoppingCart.dto;

import com.abdullah.shoppingCart.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private String name;

    private String brand;

    private BigDecimal price;

    private int inventory;

    private String description;

    private Category category;
}
