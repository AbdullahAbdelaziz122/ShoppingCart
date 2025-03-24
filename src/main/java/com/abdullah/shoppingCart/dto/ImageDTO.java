package com.abdullah.shoppingCart.dto;

import com.abdullah.shoppingCart.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ImageDTO {

    private Long id;

    private String fileName;

    private String downloadUrl;

}
