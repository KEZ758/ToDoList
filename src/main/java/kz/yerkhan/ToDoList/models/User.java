package kz.yerkhan.ToDoList.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String name;

    @Column(name = "password_hash", nullable = false)
    private String password;

}
