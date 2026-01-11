package kz.yerkhan.ToDoList.repositories;

import kz.yerkhan.ToDoList.models.Category;
import kz.yerkhan.ToDoList.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
