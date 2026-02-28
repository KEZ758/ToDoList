package kz.yerkhan.ToDoList.repositories;

import kz.yerkhan.ToDoList.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserIdOrderByDueDateAsc(Long userId);

    Long countByUserIdAndDueDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    Long countByUserIdAndDueDateBetweenAndCompletedTrue(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t.dueDate FROM Todo t WHERE t.user.id = :userId AND t.completed = true ORDER BY t.dueDate DESC")
    List<LocalDateTime> findCompletedDatesByUserId(@Param("userId") Long userId);
}
