package kz.yerkhan.ToDoList.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.yerkhan.ToDoList.dto.UserUpdateDTO;
import kz.yerkhan.ToDoList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Пользователь", description = "Управление пользователем")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Обновить профиль", description = "Позволяет изменить имя и/или пароль. Email изменить нельзя.")
    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody UserUpdateDTO userUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.updateUser(email, userUpdateDTO);

        return ResponseEntity.ok("Профиль успешно обновлен");
    }
}
