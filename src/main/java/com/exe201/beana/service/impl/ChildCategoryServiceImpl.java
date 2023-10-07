package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.dto.ChildCategoryRequestDto;
import com.exe201.beana.entity.Category;
import com.exe201.beana.entity.ChildCategory;
import com.exe201.beana.exception.ResourceNameAlreadyExistsException;
import com.exe201.beana.mapper.ChildCategoryMapper;
import com.exe201.beana.repository.CategoryRepository;
import com.exe201.beana.repository.ChildCategoryRepository;
import com.exe201.beana.service.ChildCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildCategoryServiceImpl implements ChildCategoryService {

    private final ChildCategoryRepository childCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ChildCategoryDto addChildCategory(ChildCategoryRequestDto newChildCategory) {
        Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoriesByStatusAndName((byte) 1, newChildCategory.getName());
        if (foundChildCategory.isPresent()) {
            throw new ResourceNameAlreadyExistsException("Child category already exists with id: " + foundChildCategory.get().getId());
        }
        Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndId((byte) 1, newChildCategory.getCategoryId());
        if (foundCategory.isEmpty()) {
            throw new ResourceNameAlreadyExistsException("Can not find category with id: " + newChildCategory.getCategoryId());
        }
        ChildCategory temp = new ChildCategory();
        temp.setName(newChildCategory.getName());
        temp.setStatus((byte) 1);
        temp.setCategory(foundCategory.get());
        ChildCategory addedChildCategory = childCategoryRepository.save(temp);
        return ChildCategoryMapper.INSTANCE.toChildCategoryDto(addedChildCategory);
    }
}
