package com.abdullah.shoppingCart.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class CategoryDTO {

    @NotBlank(message = "Category name cannot be empty")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;

}
