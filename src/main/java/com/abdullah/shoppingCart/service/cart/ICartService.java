package com.abdullah.shoppingCart.service.cart;

import com.abdullah.shoppingCart.dto.CartDTO;

public interface ICartService {
    CartDTO getCart(Long id);
    void clearCart(Long id);

}
