package com.abdullah.shoppingCart.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// this class is used to return data to our frontend
@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;

    public ApiResponse(String message, T data){
        this.message= message;
        this.data = data;
    }
}
