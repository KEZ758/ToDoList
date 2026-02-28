# ToDoList API

## Описание
REST API для управления задачами с категориями и статистикой.

## Технологии
- Java 17
- Spring Boot
- Spring Security (JWT)
- PostgreSQL

## Настройка базы данных

CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
email VARCHAR(255) NOT NULL UNIQUE,
name VARCHAR(100),
password VARCHAR(255) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
id BIGSERIAL PRIMARY KEY,
title VARCHAR(100) NOT NULL,
user_id BIGINT NOT NULL,

                            CONSTRAINT fk_category_user
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE todos (
id BIGSERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
description TEXT,
priority INTEGER DEFAULT 2,
completed BOOLEAN DEFAULT FALSE,

                       due_date TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,


                       user_id BIGINT NOT NULL,
                       category_id BIGINT,

                       CONSTRAINT fk_todo_user
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

                       CONSTRAINT fk_todo_category
                           FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);