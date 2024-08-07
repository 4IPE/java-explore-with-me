package service.admin.service;

import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;

public interface CompilationsService {
    CompilationsOutDto addCompilations(CompilationsInDto compilationsInDto);

    void delCompilations(Long compId);

    CompilationsOutDto pathCompilations(CompilationsInDto compilationsInDto, Long compId);
}
