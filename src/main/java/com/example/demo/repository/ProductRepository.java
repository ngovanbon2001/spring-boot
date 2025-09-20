package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT n FROM Product n " +
		       "WHERE (:name IS NULL OR n.name LIKE CONCAT('%', :name, '%')) " +
		       "AND (:categoryId IS NULL OR n.categoryId = :categoryId)")
		Page<Product> getManageProducts(@Param("categoryId") Long categoryId,
		                                @Param("name") String name,
		                                Pageable pageable);
}