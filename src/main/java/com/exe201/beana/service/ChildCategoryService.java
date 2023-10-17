package com.exe201.beana.service;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.dto.ChildCategoryRequestDto;

import java.util.List;

public interface ChildCategoryService {
    ChildCategoryDto addChildCategory(ChildCategoryRequestDto newChildCategory);

    List<ChildCategoryDto> getAllChildCategories();
}
