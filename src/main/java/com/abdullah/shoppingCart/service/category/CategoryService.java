package com.abdullah.shoppingCart.service.category;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.exceptions.AlreadyExistsException;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.mapper.CategoryMapper;
import com.abdullah.shoppingCart.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourcesNotFoundException("Category with id" + id +" is not found"));
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourcesNotFoundException(String.format("No category with name '%s' exists", name)));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO addCategory(@Valid CategoryDTO categoryDTO) {

        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("Category with name '%s' already exists", categoryDTO.getName())
            );
        }
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDTO)));
    }


    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {

        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDTO.getName());
                    return existingCategory;
                })
                .map(existingCategory -> {
                    return categoryMapper.toDto( categoryRepository.save(existingCategory));

                })
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