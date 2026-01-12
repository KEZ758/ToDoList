package kz.yerkhan.ToDoList.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.yerkhan.ToDoList.dto.StatsResponse;
import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Методы для работы с задачами")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/create")
    @Operation(summary = "Создать задачу", description = "Создает новую задачу и привязывает к текущему пользователю")
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest todoRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.createTodo(todoRequest, email));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Изменение задачи")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @RequestBody TodoRequest todoRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest, email));
    }

    @GetMapping
    @Operation(summary = "Получение всех задач пользователя")
    public ResponseEntity<List<TodoResponse>> getUserTodos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.getUserTodos(email));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление задачи")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Задача удалена");
    }

    @GetMapping("/stats")
    @Operation(summary = "Получение статистики по задачам", description = "Получение статистики по задачам за день, неделю, месяц и год.")
    public ResponseEntity<StatsResponse> getStats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(todoService.getUserStats(email));
    }
}




