package kz.yerkhan.ToDoList.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private int priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private String categoryTitle;
}
