package service.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.admin.service.CategoriesService;
import service.dto.categories.CategoriesInDto;
import service.dto.categories.CategoriesOutDto;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoriesController {

    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<CategoriesOutDto> addCategories(@RequestBody @Valid CategoriesInDto categories) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.addCategories(categories));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoriesOutDto> patchCategories(@PathVariable Long catId,
                                                            @Valid @RequestBody CategoriesInDto categories) {

        return ResponseEntity.ok().body(categoriesService.updateCat(catId, categories));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<String> removeCategory(@PathVariable Long catId) {
        categoriesService.delCat(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Категория удален");
    }
}
