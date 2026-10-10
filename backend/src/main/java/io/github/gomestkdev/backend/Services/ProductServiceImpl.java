package io.github.gomestkdev.backend.Services;

import io.github.gomestkdev.backend.Services.interfaces.FileService;
import io.github.gomestkdev.backend.Services.interfaces.ProductService;
import io.github.gomestkdev.backend.dtos.ProductDTO;
import io.github.gomestkdev.backend.exeptions.handler.ApiException;
import io.github.gomestkdev.backend.exeptions.handler.ResourceNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

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
        this.modelMapper = new ModelMapper();
    }

    @Override
    public ProductResponse getAllProducts(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    ) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<ProductModel> productPage = productRepository.findAll(pageable);

        List<ProductModel> productModels = productPage.getContent();
        List<ProductDTO> productDTOs = productModels.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(
            Long categoryId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    ) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<ProductModel> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(
            String keyword,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    ) {
        List<ProductModel> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO product) {
        CategoryModel isCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<ProductModel> products = isCategory.getProducts();
        for (ProductModel productModel : products) {
            if (productModel.getName().equals(product.getName())) {
                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {


        Double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

        ProductModel newProduct = modelMapper.map(product, ProductModel.class);
        newProduct.setCategory(isCategory);
        newProduct.setSpecialPrice(specialPrice);
        newProduct.setImage("default.png");

        ProductModel savedProduct = productRepository.save(newProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new ApiException("Product always exist!");
        }
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        ProductModel isProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        String fileName = fileService.uploadImage(path, image);

        isProduct.setImage(fileName);

        ProductModel savedProduct = productRepository.save(isProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO product) {
        ProductModel isProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        isProduct.setName(product.getName());
        isProduct.setDescription(product.getDescription());
        isProduct.setPrice(product.getPrice());
        isProduct.setDiscount(product.getDiscount());
        isProduct.setQuantity(product.getQuantity());
        isProduct.setPrice(product.getPrice());
        isProduct.setSpecialPrice(product.getSpecialPrice());

        ProductModel productSaved = productRepository.save(isProduct);

        return modelMapper.map(productSaved, ProductDTO.class);
    }

    @Override
    public ProductDTO delete(Long productId) {
        ProductModel isProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(isProduct);
        return modelMapper.map(isProduct, ProductDTO.class);
    }
}
