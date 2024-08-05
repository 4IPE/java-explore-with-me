package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import service.exception.model.NotFound;
import service.model.Categories;
import service.repository.CategoriesRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoriesMapperAbs {
    @Autowired
    private CategoriesRepository categoriesRepository;


    @Named("toEntity")
    public Categories toEntity(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoriesRepository.findById(categoryId).orElseThrow(() -> new NotFound("Categories with " + categoryId + "don t found"));
    }
}
