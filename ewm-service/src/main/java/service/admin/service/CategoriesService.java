package service.admin.service;

import service.dto.categories.CategoriesInDto;
import service.dto.categories.CategoriesOutDto;

public interface CategoriesService {
    CategoriesOutDto addCategories(CategoriesInDto categoriesIn);

    void delCat(Long catId);


    CategoriesOutDto updateCat(Long catId, CategoriesInDto categoriesInDto);
}
