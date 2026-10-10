package io.github.gomestkdev.backend.services;

import io.github.gomestkdev.backend.services.interfaces.CategoryService;
import io.github.gomestkdev.backend.dtos.CategoryDTO;
import io.github.gomestkdev.backend.payload.CategoryResponse;
import io.github.gomestkdev.backend.exceptions.handler.ApiException;
import io.github.gomestkdev.backend.exceptions.handler.ResourceNotFoundException;
import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<CategoryModel> categoryPage = repository.findAll(pageDetails);

        List<CategoryDTO> categoriesDTO = categoryPage.getContent().stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList();

        CategoryResponse categoriesResponse = new CategoryResponse();
        categoriesResponse.setContent(categoriesDTO);
        categoriesResponse.setPageNumber(categoryPage.getNumber());
        categoriesResponse.setPageSize(categoryPage.getSize());
        categoriesResponse.setTotalElements(categoryPage.getTotalElements());
        categoriesResponse.setTotalPages(categoryPage.getTotalPages());
        categoriesResponse.setLastPage(categoryPage.isLast());

        return categoriesResponse;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO category) {
        Optional<CategoryModel> existingCategory = repository.findByName(category.getName());

        if (existingCategory.isPresent()) {
            throw new ApiException("Category with name: " + category.getName() + " already exists.");
        }

        CategoryModel categoryModel = modelMapper.map(category, CategoryModel.class);
        CategoryModel savedCategory = repository.save(categoryModel);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO category) {
        CategoryModel savedCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        savedCategory.setName(category.getName());

        CategoryModel updatedCategory = repository.save(savedCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO deleteCategory(Long id) {
        CategoryModel category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        repository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }
}