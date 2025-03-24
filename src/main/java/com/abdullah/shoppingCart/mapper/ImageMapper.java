package com.abdullah.shoppingCart.mapper;


import com.abdullah.shoppingCart.dto.ImageDTO;
import com.abdullah.shoppingCart.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ImageMapper {
    ImageDTO toDto(Image image);
    Image toEntity(ImageDTO imageDTO);

    List<ImageDTO> toListDto(List<Image> images);
    List<Image> toEntityList(List<ImageDTO> imageDTOS);
}