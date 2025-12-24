package kz.yerkhan.ToDoList.dto;


import lombok.Data;

@Data
public class AuthRequest {
    String email;
    String password;
}
