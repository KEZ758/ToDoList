package kz.yerkhan.ToDoList.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.yerkhan.ToDoList.dto.CategoryRequest;
import kz.yerkhan.ToDoList.dto.CategoryResponse;
import kz.yerkhan.ToDoList.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "Методы для работы с категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получение категорий пользователя")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getUserCategory() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(categoryService.getUserCategories(userEmail));
    }

    @Operation(summary = "Добавление категорий пользователя")
    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(categoryService.addCategory(userEmail, request));
    }

    @Operation(summary = "Удаление категорий пользователя")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Категория удалена");
    }
}
