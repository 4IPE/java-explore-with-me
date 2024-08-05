package service.admin.service;

import ru.practicum.ewm.dto.compilations.CompilationsInDto;
import service.model.Compilations;

public interface CompilationsService {
    Compilations addCompilations(CompilationsInDto compilationsInDto);

    void delCompilations(Long compId);

    Compilations pathCompilations(CompilationsInDto compilationsInDto, Long compId);
}
