package kz.yerkhan.ToDoList.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    private int status;
    private String message;
    private Date timestamp;
}
