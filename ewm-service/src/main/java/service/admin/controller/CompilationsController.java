package service.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.admin.service.CompilationsService;
import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.compilations.CompilationsUpdDto;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsController {

    private final CompilationsService compilationsService;

    @PostMapping
    public ResponseEntity<CompilationsOutDto> addCompilations(@RequestBody @Valid CompilationsInDto compilations) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationsService.addCompilations(compilations));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationsOutDto> patchCompilations(@PathVariable Long compId,
                                                                @Valid @RequestBody CompilationsUpdDto compilations) {

        return ResponseEntity.ok().body(compilationsService.pathCompilations(compilations, compId));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<String> removeCompilations(@PathVariable Long compId) {
        compilationsService.delCompilations(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Подборка удален");
    }
}
