package com.abdullah.shoppingCart.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {


    private Long id;


    private String name;


    private String slug;

    private String brand;

    private BigDecimal price;

    private Long inventory;

    private String description;

    private CategoryDTO category;

    private List<ImageDTO> images;
}
