package service.admin.service;

import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.compilations.CompilationsUpdDto;

public interface CompilationsService {
    CompilationsOutDto addCompilations(CompilationsInDto compilationsInDto);

    void delCompilations(Long compId);

    CompilationsOutDto pathCompilations(CompilationsUpdDto compilationsInDto, Long compId);
}
