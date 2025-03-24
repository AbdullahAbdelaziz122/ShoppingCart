package com.abdullah.shoppingCart.mapper;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryDTO toDto(Category category);
    Category toEntity(CategoryDTO categoryDTO);

    List<CategoryDTO> toDtoList(List<Category> categories);
    List<Category> toEntityList(List<CategoryDTO> categoryDTOS);
}
