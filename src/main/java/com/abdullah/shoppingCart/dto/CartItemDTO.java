package com.abdullah.shoppingCart.dto;

import com.abdullah.shoppingCart.model.Cart;
import com.abdullah.shoppingCart.model.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

public class CartItemDTO {


    private Long id;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private int quantity;


    private Product product;

    private Cart cart;
}
