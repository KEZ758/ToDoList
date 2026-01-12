package kz.yerkhan.ToDoList.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoRequest {

    @Schema(description = "Название задачи", example = "Купить молоко")
    String title;

    String description;
    @Schema(description = "Приоритет (1-Высокий, 2-Средний, 3-Низкий)", example = "1")
    int priority;
    boolean completed;
    LocalDateTime dueDate;
    Long categoryId;
}
