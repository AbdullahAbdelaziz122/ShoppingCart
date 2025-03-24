package com.abdullah.shoppingCart.service.category;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.exceptions.AlreadyExistsException;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Category;
import com.abdullah.shoppingCart.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourcesNotFoundException("Category with id" + id +" is not found")
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourcesNotFoundException(String.format("No category with name '%s' exists", name)));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(@Valid CategoryDTO categoryDTO) {

        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("Category with name '%s' already exists", categoryDTO.getName())
            );
        }

        Category category = mapper.map(categoryDTO, Category.class);
        return categoryRepository.save(category);
    }


    @Override
    public Category updateCategory(CategoryDTO categoryDTO, Long id) {

        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDTO.getName());
                    return existingCategory;
                })
                .map(categoryRepository::save)
                .orElseThrow(() -> new ResourcesNotFoundException("Category with id " + id + " not found"));

    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourcesNotFoundException("Category with id " + id + " not found");
                });
    }


}