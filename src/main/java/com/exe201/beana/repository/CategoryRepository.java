package com.exe201.beana.repository;

import com.exe201.beana.entity.Category;
import com.exe201.beana.entity.ChildCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByStatus(byte status);

    Optional<Category> findCategoryByStatusAndName(byte status, String name);

    Optional<Category> findCategoryByStatusAndId(byte status, Long id);

    Optional<Category> findCategoryByStatusAndChildCategoriesContaining(byte status, ChildCategory childCategory);
}
