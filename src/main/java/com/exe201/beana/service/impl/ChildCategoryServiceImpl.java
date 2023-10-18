package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.dto.ChildCategoryRequestDto;
import com.exe201.beana.entity.Category;
import com.exe201.beana.entity.ChildCategory;
import com.exe201.beana.exception.ResourceAlreadyExistsException;
import com.exe201.beana.mapper.ChildCategoryMapper;
import com.exe201.beana.repository.CategoryRepository;
import com.exe201.beana.repository.ChildCategoryRepository;
import com.exe201.beana.service.ChildCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildCategoryServiceImpl implements ChildCategoryService {

    private final ChildCategoryRepository childCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ChildCategoryDto addChildCategory(ChildCategoryRequestDto newChildCategory) {
        Optional<ChildCategory> foundChildCategory = childCategoryRepository.findChildCategoriesByStatusAndName((byte) 1, newChildCategory.getName());
        if (foundChildCategory.isPresent()) {
            throw new ResourceAlreadyExistsException("Child category already exists with id: " + foundChildCategory.get().getId());
        }
        Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndId((byte) 1, newChildCategory.getCategoryId());
        if (foundCategory.isEmpty()) {
            throw new ResourceAlreadyExistsException("Can not find category with id: " + newChildCategory.getCategoryId());
        }
        ChildCategory temp = new ChildCategory();
        temp.setName(newChildCategory.getName());
        temp.setStatus((byte) 1);
        temp.setCategory(foundCategory.get());
        return ChildCategoryMapper.INSTANCE.toChildCategoryDto(childCategoryRepository.save(temp));
    }

    @Override
    public List<ChildCategoryDto> getAllChildCategories() {
        return childCategoryRepository.findAll().stream().map(ChildCategoryMapper.INSTANCE::toChildCategoryDto).collect(Collectors.toList());
    }
}
