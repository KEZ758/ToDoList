package kz.yerkhan.ToDoList.service;


import kz.yerkhan.ToDoList.dto.CategoryRequest;
import kz.yerkhan.ToDoList.dto.CategoryResponse;
import kz.yerkhan.ToDoList.models.Category;
import kz.yerkhan.ToDoList.models.User;
import kz.yerkhan.ToDoList.repositories.CategoryRepository;
import kz.yerkhan.ToDoList.repositories.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setTitle(category.getTitle());

        return categoryResponse;
    }

    public List<CategoryResponse> getUserCategories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Category> UserCategories = categoryRepository.findAllByUserId(user.getId());
        return UserCategories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    public CategoryResponse addCategory(String userEmail, CategoryRequest categoryRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = new Category();
        category.setTitle(categoryRequest.getTitle());
        category.setUser(user);
        categoryRepository.save(category);
        return mapToCategoryResponse(category);
    }

    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

}
