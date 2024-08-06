package service.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.compilations.CompilationsInDto;
import service.exception.model.NotFound;
import service.mapper.CompilationsMapper;
import service.model.Compilations;
import service.repository.CompilationsRepository;

@Service
public class CompilationsServiceImpl implements CompilationsService {
    @Autowired
    private CompilationsRepository compilationsRepository;
    @Autowired
    private CompilationsMapper compilationsMapper;

    @Override
    public Compilations addCompilations(CompilationsInDto compilationsInDto) {
        Compilations compilations = compilationsMapper.toCompilations(compilationsInDto);
        return compilationsRepository.save(compilations);
    }

    @Override
    public void delCompilations(Long compId) {
        Compilations compilations = compilationsRepository.findById(compId).orElseThrow(() -> new NotFound("Compilation with " + compId + " was not found"));
        compilationsRepository.deleteById(compId);
    }

    @Override
    public Compilations pathCompilations(CompilationsInDto compilationsInDto, Long compId) {
        Compilations compilationsFind = compilationsRepository.findById(compId).orElseThrow(() -> new NotFound("Compilation with " + compId + " was not found"));
        Compilations compilationsCreate = compilationsMapper.toCompilations(compilationsInDto);
        compilationsFind.setTitle(compilationsCreate.getTitle() != null && !compilationsCreate.getTitle().isEmpty() ? compilationsCreate.getTitle() : compilationsFind.getTitle());
        compilationsFind.setEvents(compilationsCreate.getEvents() != null ? compilationsCreate.getEvents() : compilationsFind.getEvents());
        compilationsFind.setPinned(compilationsCreate.getPinned() != null ? compilationsCreate.getPinned() : compilationsFind.getPinned());
        return compilationsRepository.save(compilationsFind);
    }


}
