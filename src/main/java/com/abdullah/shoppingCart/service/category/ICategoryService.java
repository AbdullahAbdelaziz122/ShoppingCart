package com.abdullah.shoppingCart.service.category;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.model.Category;

import java.util.List;

public interface ICategoryService {
    CategoryDTO getCategoryById(Long id);

    CategoryDTO getCategoryByName(String name);

    List<CategoryDTO> getAllCategories();

    CategoryDTO addCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);

    void deleteCategory(Long id);


}
