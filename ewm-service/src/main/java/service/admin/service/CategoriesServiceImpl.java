package service.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.dto.categories.CategoriesInDto;
import service.dto.categories.CategoriesOutDto;
import service.exception.model.NotFoundException;
import service.mapper.CategoriesMapper;
import service.model.Categories;
import service.repository.CategoriesRepository;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository repository;

    private final CategoriesMapper mapper;

    @Override
    public CategoriesOutDto addCategories(CategoriesInDto categoriesIn) {
        Categories categories = mapper.toCategories(categoriesIn);
        return mapper.toOut(repository.save(categories));
    }

    @Override
    public void delCat(Long catId) {
        Categories categories = repository.findById(catId).orElseThrow(() -> new NotFoundException("Категория с id: " + catId + " не была нйдена"));
        repository.deleteById(catId);
    }

    @Override
    public CategoriesOutDto updateCat(Long catId, CategoriesInDto categoriesInDto) {
        Categories categories = repository.findById(catId).orElseThrow(() -> new NotFoundException("Категория с id: " + catId + " не была нйдена"));
        categories.setName(categoriesInDto.getName() != null ? categoriesInDto.getName() : categories.getName());
        return mapper.toOut(repository.save(categories));
    }

}
