package kz.yerkhan.ToDoList.repositories;

import kz.yerkhan.ToDoList.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
