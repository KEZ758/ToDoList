package kz.yerkhan.ToDoList.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoRequest {

    String title;
    String description;
    int priority;
    boolean completed;
    LocalDateTime dueDate;
    Long categoryId;
}
