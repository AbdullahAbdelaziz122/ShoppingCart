package com.abdullah.shoppingCart.service.category;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(CategoryDTO categoryDTO);

    Category updateCategory(CategoryDTO categoryDTO, Long id);

    void deleteCategory(Long id);


}
