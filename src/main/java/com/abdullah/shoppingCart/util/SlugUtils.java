package com.abdullah.shoppingCart.util;


import com.github.slugify.Slugify;
import lombok.Data;

@Data
public class SlugUtils {
    private final Slugify slugify = new Slugify();

    public String generateSlug(String name){
        return slugify.slugify(name);
    }

}
