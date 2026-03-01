package kz.yerkhan.ToDoList.service;


import kz.yerkhan.ToDoList.dto.TodoRequest;
import kz.yerkhan.ToDoList.dto.TodoResponse;
import kz.yerkhan.ToDoList.exceptions.ResourceNotFoundException;
import kz.yerkhan.ToDoList.helpers.TodoMapper;
import kz.yerkhan.ToDoList.models.Todo;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.CategoryRepository;
import kz.yerkhan.ToDoList.repositories.TodoRepository;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TodoMapper todoMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private TodoService todoService;

    @Test
    void getUserTodos_shouldReturnTodos() {
        User user = new User();
        user.setId(1L);

        Todo todo = new Todo();
        todo.setTitle("Тест");

        TodoResponse response = new TodoResponse();
        response.setTitle("Тест");

        when(userRepository.findByEmail("test1@gmail.com")).thenReturn(Optional.of(user));
        when(todoRepository.findAllByUserIdOrderByDueDateAsc(1L)).thenReturn(List.of(todo));
        when(todoMapper.toResponse(todo)).thenReturn(response);

        List<TodoResponse> result = todoService.getUserTodos("test1@gmail.com");

        assertEquals(1, result.size());
        assertEquals("Тест", result.get(0).getTitle());
    }

    @Test
    void getUserTodos_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> todoService.getUserTodos("unknown@mail.com"));
    }

    @Test
    void deleteTodo_shouldCallRepository() {
        todoService.deleteTodo(1L);
        verify(todoRepository).deleteById(1L);
    }

    @Test
    void createTodo_shouldCreateTodoWithoutCategory() {
        User user = new User();
        user.setId(1L);

        TodoRequest request = new TodoRequest();
        request.setTitle("Новая задача");
        request.setCategoryId(null);

        Todo todo = new Todo();
        TodoResponse response = new TodoResponse();
        response.setTitle("Новая задача");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(todoMapper.toEntity(request)).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(todo);
        when(todoMapper.toResponse(todo)).thenReturn(response);

        TodoResponse result = todoService.createTodo(request, "test@mail.com");

        assertEquals("Новая задача", result.getTitle());
        verify(todoRepository).save(todo);
    }

    @Test
    void updateTodo_shouldThrowException_whenNotOwner() {
        User owner = new User();
        owner.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Todo todo = new Todo();
        todo.setUser(owner);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(userRepository.findByEmail("other@mail.com")).thenReturn(Optional.of(otherUser));

        assertThrows(ResourceNotFoundException.class,
                () -> todoService.updateTodo(1L, new TodoRequest(), "other@mail.com"));
    }
}
