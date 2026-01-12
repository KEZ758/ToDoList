package kz.yerkhan.ToDoList.service;



import kz.yerkhan.ToDoList.dto.PeriodStats;
import kz.yerkhan.ToDoList.dto.StatsResponse;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
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

    public StatsResponse getUserStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        LocalDate today = LocalDate.now();

        int streak = calculateStreak(userId);

        PeriodStats dailyStats = getStatsForPeriod(userId, today.atStartOfDay(), today.atTime(LocalTime.MAX));

        PeriodStats weeklyStats = getStatsForPeriod(userId,
                today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay(),
                today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX));

        PeriodStats monthlyStats = getStatsForPeriod(userId,
                today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay(),
                today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX));

        PeriodStats yearlyStats = getStatsForPeriod(userId,
                today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay(),
                today.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX));

        return new StatsResponse(streak, dailyStats, weeklyStats, monthlyStats, yearlyStats);
    }

    private int calculateStreak(Long userId) {
        List<LocalDateTime> dates = todoRepository.findCompletedDatesByUserId(userId);

        if (dates.isEmpty()) return 0;

        int streak = 0;
        LocalDate checkDate = LocalDate.now();

        Set<LocalDate> completedDays = dates.stream()
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        if (!completedDays.contains(checkDate)) {
            checkDate = checkDate.minusDays(1);
            if (!completedDays.contains(checkDate)) {
                return 0;
            }
        }
        while (completedDays.contains(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }
        return streak;

    }

    private PeriodStats getStatsForPeriod(Long userId, LocalDateTime start, LocalDateTime end) {
        Long total = todoRepository.countByUserIdAndDueDateBetween(userId, start, end);
        Long completed = todoRepository.countByUserIdAndDueDateBetweenAndCompletedTrue(userId, start, end);
        return new PeriodStats(total.intValue(), completed.intValue());
    }


}
