package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.ProductDTO;
import com.hexalyte.salon.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, 
                                                  @Valid @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        try {
            ProductDTO product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        try {
            ProductDTO product = productService.getProductByCode(code);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> getProductsByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsByFilters(name, category, productType, brand, status, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getActiveProducts() {
        List<ProductDTO> products = productService.getActiveProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/type/{productType}")
    public ResponseEntity<List<ProductDTO>> getProductsByType(@PathVariable String productType) {
        List<ProductDTO> products = productService.getProductsByType(productType);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getDistinctCategories() {
        List<String> categories = productService.getDistinctCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getDistinctBrands() {
        List<String> brands = productService.getDistinctBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{productId}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable Long productId) {
        try {
            productService.activateProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
