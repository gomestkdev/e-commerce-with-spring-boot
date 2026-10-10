package io.github.gomestkdev.backend.services;

import io.github.gomestkdev.backend.services.interfaces.FileService;
import io.github.gomestkdev.backend.services.interfaces.ProductService;
import io.github.gomestkdev.backend.dtos.ProductDTO;
import io.github.gomestkdev.backend.exceptions.handler.ApiException;
import io.github.gomestkdev.backend.exceptions.handler.ResourceNotFoundException;
import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.models.ProductModel;
import io.github.gomestkdev.backend.payload.ProductResponse;
import io.github.gomestkdev.backend.repositories.CategoryRepository;
import io.github.gomestkdev.backend.repositories.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            FileService fileService,
            ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    private Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private ProductResponse buildProductResponse(Page<ProductModel> productPage) {
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse response = new ProductResponse();
        response.setContent(productDTOs);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalPages(productPage.getTotalPages());
        response.setTotalElements(productPage.getTotalElements());
        response.setLastPage(productPage.isLast());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Page<ProductModel> productPage = productRepository.findAll(createPageable(pageNumber, pageSize, sortBy, sortOrder));
        return buildProductResponse(productPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<ProductModel> productPage = productRepository.findByCategory(category, createPageable(pageNumber, pageSize, sortBy, sortOrder));
        return buildProductResponse(productPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Page<ProductModel> productPage = productRepository.findByNameContainingIgnoreCase(keyword, createPageable(pageNumber, pageSize, sortBy, sortOrder));
        return buildProductResponse(productPage);
    }

    @Override
    @Transactional
    public ProductDTO addProduct(Long categoryId, ProductDTO product) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean productExists = category.getProducts().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()));

        if (productExists) {
            throw new ApiException("Product with this name already exists in this category.");
        }

        double discountPercentage = product.getDiscount() != null ? product.getDiscount() : 0.0;
        double specialPrice = product.getPrice() - ((discountPercentage * 0.01) * product.getPrice());

        ProductModel newProduct = modelMapper.map(product, ProductModel.class);
        newProduct.setCategory(category);
        newProduct.setSpecialPrice(specialPrice);
        newProduct.setImage("default.png");

        ProductModel savedProduct = productRepository.save(newProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);

        ProductModel savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO product) {
        ProductModel existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());

        double discountPercentage = product.getDiscount() != null ? product.getDiscount() : 0.0;
        existingProduct.setSpecialPrice(product.getPrice() - ((discountPercentage * 0.01) * product.getPrice()));

        ProductModel savedProduct = productRepository.save(existingProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long productId) {
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }
}