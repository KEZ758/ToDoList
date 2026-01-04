package kz.yerkhan.ToDoList.exceptions;


import kz.yerkhan.ToDoList.models.AppError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentials(BadCredentialsException e) {
        log.error("Ошибка авторизации: {}", e.getMessage());
        return new ResponseEntity<>(new AppError(
                HttpStatus.UNAUTHORIZED.value(),
                "Неверный логин или пароль",
                new Date()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppError> handleIllegalArgument(IllegalArgumentException e) {
        log.error("Некорректные аргументы: {}", e.getMessage());
        return new ResponseEntity<>(new AppError(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                new Date()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleAllExceptions(Exception e) {
        log.error("Внутренняя ошибка сервера: {}", e);

        return new ResponseEntity<>(new AppError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Что-то пошло не так на сервере. Обратитесь к админу.",
                new Date()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppError> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.warn("Ошибка валидации: {}", message);

        return new ResponseEntity<>(new AppError(
                HttpStatus.BAD_REQUEST.value(),
                message,
                new Date()
        ), HttpStatus.BAD_REQUEST);
    }
}
