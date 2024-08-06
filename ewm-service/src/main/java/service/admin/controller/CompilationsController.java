package service.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilations.CompilationsInDto;
import service.admin.service.CompilationsService;
import service.model.Compilations;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationsController {
    @Autowired
    private CompilationsService compilationsService;

    @PostMapping
    public ResponseEntity<Compilations> addCompilations(@RequestBody @Valid CompilationsInDto compilations) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationsService.addCompilations(compilations));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Compilations> patchCategories(@PathVariable Long compId,
                                                        @Valid @RequestBody CompilationsInDto compilations) {

        return ResponseEntity.ok().body(compilationsService.pathCompilations(compilations, compId));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<String> delComp(@RequestParam Long compId) {
        compilationsService.delCompilations(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Подборка удален");
    }
}
