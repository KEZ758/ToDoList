package kz.yerkhan.ToDoList.helpers;

import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.models.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {


    public TodoResponse toResponse(Todo todo) {
        if (todo == null) {
            return null;
        }

        TodoResponse response = new TodoResponse();
        response.setId(todo.getId());
        response.setTitle(todo.getTitle());
        response.setDescription(todo.getDescription());
        response.setCompleted(todo.isCompleted());


        response.setPriority(todo.getPriority());

        response.setDueDate(todo.getDueDate());
        response.setCreatedAt(todo.getCreatedAt());


        if (todo.getCategory() != null) {
            response.setCategoryTitle(todo.getCategory().getTitle());
            response.setCategoryId(todo.getCategory().getId());
        } else {
            response.setCategoryTitle("Без категории");
        }

        return response;
    }


    public Todo toEntity(TodoRequest request) {
        if (request == null) {
            return null;
        }

        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());

        todo.setPriority(request.getPriority());

        todo.setDueDate(request.getDueDate());

        todo.setCompleted(request.isCompleted());

        return todo;
    }

}