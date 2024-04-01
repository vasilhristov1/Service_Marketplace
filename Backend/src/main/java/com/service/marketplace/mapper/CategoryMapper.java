package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.response.CategoryResponse;
import com.service.marketplace.persistence.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryRequestToCategory(CategoryRequest request);

    CategoryResponse categoryToCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);

    void categoryFromRequest(CategoryRequest request, @MappingTarget Category category);
}
