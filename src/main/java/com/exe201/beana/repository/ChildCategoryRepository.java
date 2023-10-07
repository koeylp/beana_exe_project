package com.exe201.beana.repository;

import com.exe201.beana.entity.ChildCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildCategoryRepository extends JpaRepository<ChildCategory, Long> {
    Optional<ChildCategory> findChildCategoriesByStatusAndName(byte status, String name);

    Optional<ChildCategory> findChildCategoryByStatusAndId(byte status, Long id);
}
