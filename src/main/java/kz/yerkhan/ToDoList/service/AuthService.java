package kz.yerkhan.ToDoList.service;


import kz.yerkhan.ToDoList.dto.AuthRequest;
import kz.yerkhan.ToDoList.jwt.JwtUtils;
import kz.yerkhan.ToDoList.models.Category;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.CategoryRepository;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CategoryRepository categoryRepository;


    @Transactional
    public String registerUser(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        createDefaultCategories(user);

        return jwtUtils.generateToken(user);
    }

    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        return jwtUtils.generateToken(user);
    }

    private void createDefaultCategories(User user) {
        List<Category> defaults = List.of(
                new Category("Работа", user),
                new Category("Личное", user),
                new Category("Покупки", user),
                new Category("Здоровье", user)
        );

        categoryRepository.saveAll(defaults);
    }
}
