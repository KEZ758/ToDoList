package kz.yerkhan.ToDoList.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Schema(description = "Новое имя пользователя")
    private String name;

    @Schema(description = "Новый пароль")
    private String password;
}
