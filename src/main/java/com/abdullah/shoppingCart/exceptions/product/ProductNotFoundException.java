package com.abdullah.shoppingCart.exceptions.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String messsage)  {
        super(messsage);
    }
}
