package service.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.compilations.CompilationsUpdDto;
import service.exception.model.NotFound;
import service.mapper.CompilationsMapper;
import service.model.Compilations;
import service.repository.CompilationsRepository;

@Service
public class CompilationsServiceImpl implements CompilationsService {
    private static final Logger log = LoggerFactory.getLogger(CompilationsServiceImpl.class);
    @Autowired
    private CompilationsRepository compilationsRepository;
    @Autowired
    private CompilationsMapper compilationsMapper;

    @Override
    @Transactional
    public CompilationsOutDto addCompilations(CompilationsInDto compilationsInDto) {
        Compilations compilations = compilationsMapper.toCompilationsForIn(compilationsInDto);
        return compilationsMapper.toCompilationsOutDto(compilationsRepository.save(compilations));
    }

    @Override
    @Transactional
    public void delCompilations(Long compId) {
        Compilations compilations = compilationsRepository.findById(compId).orElseThrow(() -> new NotFound("Compilation with " + compId + " was not found"));
        compilationsRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationsOutDto pathCompilations(CompilationsUpdDto compilationsUpdDto, Long compId) {
        Compilations compilationsFind = compilationsRepository.findById(compId).orElseThrow(() -> new NotFound("Compilation with " + compId + " was not found"));
        Compilations compilationsCreate = compilationsMapper.toCompilationsForUpd(compilationsUpdDto);
        compilationsFind.setTitle(compilationsCreate.getTitle() != null && !compilationsCreate.getTitle().isEmpty() ? compilationsCreate.getTitle() : compilationsFind.getTitle());
        compilationsFind.setEvents(compilationsCreate.getEvents() != null ? compilationsCreate.getEvents() : compilationsFind.getEvents());
        compilationsFind.setPinned(compilationsCreate.getPinned() != null ? compilationsCreate.getPinned() : compilationsFind.getPinned());
        return compilationsMapper.toCompilationsOutDto(compilationsRepository.save(compilationsFind));
    }


}
