package kz.yerkhan.ToDoList.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private int currentStreak;

    private PeriodStats today;
    private PeriodStats week;
    private PeriodStats month;
    private PeriodStats year;

}
