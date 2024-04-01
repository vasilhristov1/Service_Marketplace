package com.service.marketplace.service;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Integer categoryId);

    CategoryResponse createCategory(CategoryRequest categoryToCreate);

    CategoryResponse updateCategory(Integer categoryId, CategoryRequest categoryToUpdate);

    void deleteCategoryById(Integer categoryId);
}
