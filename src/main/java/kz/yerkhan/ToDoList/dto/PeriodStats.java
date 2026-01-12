package kz.yerkhan.ToDoList.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodStats {
    private int total;
    private int completed;
}
