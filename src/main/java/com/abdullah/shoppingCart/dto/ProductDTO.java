package com.abdullah.shoppingCart.dto;

import com.abdullah.shoppingCart.model.Category;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;


    private String name;


    private String brand;


    private BigDecimal price;


    private Integer inventory;

    private String description;

    private Category category;
}
