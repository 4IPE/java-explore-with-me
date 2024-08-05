package service.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.categories.CategoriesInDto;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import service.exception.model.NotFound;
import service.mapper.CategoriesMapper;
import service.model.Categories;
import service.repository.CategoriesRepository;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoriesRepository repository;
    @Autowired
    private CategoriesMapper mapper;

    @Override
    public CategoriesOutDto addCategories(CategoriesInDto categoriesIn) {
        Categories categories = mapper.toCategories(categoriesIn);
        return mapper.toOut(repository.save(categories));
    }

    @Override
    public void delCat(Long catId) {
        Categories categories = repository.findById(catId).orElseThrow(() -> new NotFound("Категория с id: " + catId + " не была нйдена"));
        repository.deleteById(catId);
    }

    @Override
    public CategoriesOutDto updateCat(Long catId, CategoriesInDto categoriesInDto) {
        Categories categories = repository.findById(catId).orElseThrow(() -> new NotFound("Категория с id: " + catId + " не была нйдена"));
        categories.setName(categoriesInDto.getName() != null ? categories.getName() : categories.getName());
        return mapper.toOut(repository.save(categories));
    }

}
