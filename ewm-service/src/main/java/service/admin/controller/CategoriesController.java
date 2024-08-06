package service.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.categories.CategoriesInDto;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import service.admin.service.CategoriesService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;

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
    public ResponseEntity<String> delCat(@PathVariable Long catId) {
        categoriesService.delCat(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Категория удален");
    }
}
