package com.abdullah.shoppingCart.mapper;


import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.model.Product;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoryMapper.class, ImageMapper.class})
public interface ProductMapper {

    ProductDTO toDto(Product product);

    Product toEntity(ProductDTO productDTO);

    List<ProductDTO> toDtoList(List<Product> products);

    List<Product> toEntityList(List<ProductDTO> productDTOS);


    void updateProduct(@MappingTarget Product existingProduct, ProductDTO newProduct);

}
