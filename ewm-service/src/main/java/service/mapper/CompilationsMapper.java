package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.compilations.CompilationsInDto;
import service.model.Compilations;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CompilationsAbsMapper.class})
public interface CompilationsMapper {
    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    Compilations toCompilations(CompilationsInDto compilationsInDto);
}
