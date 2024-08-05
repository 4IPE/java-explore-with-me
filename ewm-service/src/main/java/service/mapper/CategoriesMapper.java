package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.categories.CategoriesInDto;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import service.model.Categories;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface CategoriesMapper {
    Categories toCategories(CategoriesInDto categories);

    CategoriesOutDto toOut(Categories categories);
}
