package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.response.CategoryResponse;
import com.service.marketplace.mapper.CategoryMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseList(categories);
    }

    @Override
    public CategoryResponse getCategoryById(Integer categoryId) {
        CategoryResponse category = categoryMapper.categoryToCategoryResponse(categoryRepository.findById(categoryId).orElse(null));

        return category;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryToCreate) {
        Category newCategory = categoryMapper.categoryRequestToCategory(categoryToCreate);
        return categoryMapper.categoryToCategoryResponse(categoryRepository.save(newCategory));
    }

    @Override
    public CategoryResponse updateCategory(Integer categoryId, CategoryRequest categoryToUpdate) {
        Category existingCategory = categoryRepository.findById(categoryId).orElse(null);

        if (existingCategory != null) {
            existingCategory.setName(categoryToUpdate.getName());
            existingCategory.setDescription(categoryToUpdate.getDescription());

            return categoryMapper.categoryToCategoryResponse(categoryRepository.save(existingCategory));
        } else {
            return null;
        }
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
