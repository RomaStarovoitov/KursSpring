package com.example.kursSpring.repositories;

import com.example.kursSpring.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    com.example.kursSpring.models.Category findByName(String name);
}
