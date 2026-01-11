package kz.yerkhan.ToDoList.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private int currentStreak;
    private int totalTasksToday;
    private int completedTasksToday;
}
