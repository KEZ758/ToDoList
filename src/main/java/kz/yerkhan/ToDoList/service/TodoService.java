package kz.yerkhan.ToDoList.service;


import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.helpers.TodoMapper;
import kz.yerkhan.ToDoList.models.Category;
import kz.yerkhan.ToDoList.models.Todo;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.CategoryRepository;
import kz.yerkhan.ToDoList.repositories.TodoRepository;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TodoMapper todoMapper;

    public TodoResponse createTodo(TodoRequest todoRequest, String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found"));

        Todo todo = todoMapper.toEntity(todoRequest);
        todo.setUser(user);

        if (todoRequest.getCategoryId() != null) {

            Category category = categoryRepository.findById(todoRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + todoRequest.getCategoryId()));


            if (!category.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Вы не можете использовать чужую категорию!");
            }

            todo.setCategory(category);
        } else {

            todo.setCategory(null);
        }

        Todo savedTodo = todoRepository.save(todo);

        return todoMapper.toResponse(savedTodo);
    }
}
