package kz.yerkhan.ToDoList.service;



import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.exceptions.ResourceNotFoundException;
import kz.yerkhan.ToDoList.helpers.TodoMapper;
import kz.yerkhan.ToDoList.models.Category;
import kz.yerkhan.ToDoList.models.Todo;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.CategoryRepository;
import kz.yerkhan.ToDoList.repositories.TodoRepository;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final TodoMapper todoMapper;
    private final CategoryRepository categoryRepository;

    public TodoResponse createTodo(TodoRequest todoRequest, String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found"));

        Todo todo = todoMapper.toEntity(todoRequest);
        todo.setUser(user);

        if (todoRequest.getCategoryId() != null) {

            Category category = categoryService.getCategoryById(todoRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Категория с ID " + todoRequest.getCategoryId() + " не найдена"));

            todo.setCategory(category);
        } else {

            todo.setCategory(null);
        }

        Todo savedTodo = todoRepository.save(todo);

        return todoMapper.toResponse(savedTodo);
    }

    public TodoResponse updateTodo(Long todoId, TodoRequest todoRequest, String email) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена " + todoId));

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким логином - " + email + " не найден!"));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Это не ваша задача! Вы не можете ее изменить.");
        }

        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setPriority(todoRequest.getPriority());
        todo.setCompleted(todoRequest.isCompleted());
        todo.setDueDate(todoRequest.getDueDate());

        if (todoRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(todoRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена - " + todoRequest.getCategoryId()));

            if (!category.getUser().getId().equals(user.getId())) {
                throw new ResourceNotFoundException("Вы не можете использовать чужую категорию!");
            }
            todo.setCategory(category);
        } else {
            todo.setCategory(null);
        }

        Todo updatedTodo = todoRepository.save(todo);

        return todoMapper.toResponse(updatedTodo);

    }

    public List<TodoResponse> getUserTodos(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Todo> userTodos = todoRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        return userTodos.stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }


}
