package service.admin.service;

import ru.practicum.ewm.dto.categories.CategoriesInDto;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;

public interface CategoriesService {
    CategoriesOutDto addCategories(CategoriesInDto categoriesIn);

    void delCat(Long catId);


    CategoriesOutDto updateCat(Long catId, CategoriesInDto categoriesInDto);
}
