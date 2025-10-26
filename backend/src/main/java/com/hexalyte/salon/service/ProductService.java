package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.ProductDTO;
import com.hexalyte.salon.model.Product;
import com.hexalyte.salon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Check if product code already exists
        if (productRepository.existsByCode(productDTO.getCode())) {
            throw new IllegalArgumentException("Product code already exists: " + productDTO.getCode());
        }
        
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        // Check if product code already exists for different product
        if (productRepository.existsByCodeAndProductIdNot(productDTO.getCode(), productId)) {
            throw new IllegalArgumentException("Product code already exists: " + productDTO.getCode());
        }
        
        // Update fields
        existingProduct.setName(productDTO.getName());
        existingProduct.setCode(productDTO.getCode());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setBrand(productDTO.getBrand());
        existingProduct.setUom(productDTO.getUom());
        existingProduct.setProductType(Product.ProductType.valueOf(productDTO.getProductType()));
        existingProduct.setCostPrice(productDTO.getCostPrice());
        existingProduct.setSellingPrice(productDTO.getSellingPrice());
        existingProduct.setAlertQuantity(productDTO.getAlertQuantity());
        existingProduct.setTaxRate(productDTO.getTaxRate());
        existingProduct.setStatus(Product.ProductStatus.valueOf(productDTO.getStatus()));
        
        Product savedProduct = productRepository.save(existingProduct);
        return convertToDTO(savedProduct);
    }
    
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        return convertToDTO(product);
    }
    
    @Transactional(readOnly = true)
    public ProductDTO getProductByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with code: " + code));
        return convertToDTO(product);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByFilters(String name, String category, String productType, 
                                               String brand, String status, Pageable pageable) {
        Product.ProductType typeEnum = productType != null ? Product.ProductType.valueOf(productType) : null;
        Product.ProductStatus statusEnum = status != null ? Product.ProductStatus.valueOf(status) : null;
        
        return productRepository.findByFilters(name, category, typeEnum, brand, statusEnum, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getActiveProducts() {
        return productRepository.findByStatus(Product.ProductStatus.ACTIVE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByType(String productType) {
        return productRepository.findByProductType(Product.ProductType.valueOf(productType))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctBrands() {
        return productRepository.findDistinctBrands();
    }
    
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        // Check if product is being used in inventory
        // This would require checking inventory, service mappings, etc.
        // For now, we'll just deactivate the product
        product.setStatus(Product.ProductStatus.INACTIVE);
        productRepository.save(product);
    }
    
    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        product.setStatus(Product.ProductStatus.ACTIVE);
        productRepository.save(product);
    }
    
    // Helper methods
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setUom(dto.getUom());
        product.setProductType(Product.ProductType.valueOf(dto.getProductType()));
        product.setCostPrice(dto.getCostPrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setAlertQuantity(dto.getAlertQuantity());
        product.setTaxRate(dto.getTaxRate());
        product.setStatus(Product.ProductStatus.valueOf(dto.getStatus()));
        return product;
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setUom(product.getUom());
        dto.setProductType(product.getProductType().name());
        dto.setCostPrice(product.getCostPrice());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setAlertQuantity(product.getAlertQuantity());
        dto.setTaxRate(product.getTaxRate());
        dto.setStatus(product.getStatus().name());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
