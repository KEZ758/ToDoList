package kz.yerkhan.ToDoList.controllers;


import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.validation.Valid;
import kz.yerkhan.ToDoList.dto.AuthRequest;
import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import kz.yerkhan.ToDoList.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}




