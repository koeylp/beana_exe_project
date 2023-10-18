package com.exe201.beana.repository;

import com.exe201.beana.entity.ChildCategory;
import com.exe201.beana.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByStatusAndName(byte status, String name);

    Optional<Product> findProductByStatusAndId(byte status, Long id);

    List<Product> findAllByStatus(byte status);

    List<Product> findAllByStatusAndChildCategory(byte status, ChildCategory childCategory);

    List<Product> findAllByStatusAndPriceBetween(byte status, double startPrice, double endPrice);
}
