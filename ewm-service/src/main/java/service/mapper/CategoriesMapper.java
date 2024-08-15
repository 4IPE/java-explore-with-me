package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import service.dto.categories.CategoriesInDto;
import service.dto.categories.CategoriesOutDto;
import service.exception.model.NotFoundException;
import service.model.Categories;
import service.repository.CategoriesRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoriesMapper {
    @Autowired
    private CategoriesRepository categoriesRepository;

    public abstract Categories toCategories(CategoriesInDto categories);

    public abstract CategoriesOutDto toOut(Categories categories);


    @Named("toEntity")
    public Categories toEntity(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoriesRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Categories with " + categoryId + "don t found"));
    }
}
