package kz.yerkhan.ToDoList.controllers;



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
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/create")
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest todoRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.createTodo(todoRequest, email));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @RequestBody TodoRequest todoRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest, email));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getUserTodos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(todoService.getUserTodos(email));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo deleted successfully");
    }



}




