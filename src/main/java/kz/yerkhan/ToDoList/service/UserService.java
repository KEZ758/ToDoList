package kz.yerkhan.ToDoList.service;


import kz.yerkhan.ToDoList.dto.UserUpdateDTO;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(String email, UserUpdateDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

}
