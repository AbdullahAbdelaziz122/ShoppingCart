package com.abdullah.shoppingCart.dto;

import com.abdullah.shoppingCart.model.CartItem;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDTO {

    private Long id;

    private BigDecimal totalAmount;

    private Set<CartItemDTO> cartItems;
}
