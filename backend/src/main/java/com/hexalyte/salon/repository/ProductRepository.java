package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByCode(String code);
    
    List<Product> findByStatus(Product.ProductStatus status);
    
    List<Product> findByCategory(String category);
    
    List<Product> findByProductType(Product.ProductType productType);
    
    List<Product> findByBrand(String brand);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:productType IS NULL OR p.productType = :productType) AND " +
           "(:brand IS NULL OR p.brand = :brand) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<Product> findByFilters(@Param("name") String name,
                               @Param("category") String category,
                               @Param("productType") Product.ProductType productType,
                               @Param("brand") String brand,
                               @Param("status") Product.ProductStatus status,
                               Pageable pageable);
    
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.category")
    List<String> findDistinctCategories();
    
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.brand")
    List<String> findDistinctBrands();
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.alertQuantity > 0")
    List<Product> findActiveProductsWithAlertQuantity();
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndProductIdNot(String code, Long productId);
}
