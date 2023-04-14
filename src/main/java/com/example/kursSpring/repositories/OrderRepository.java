package com.example.kursSpring.repositories;

import com.example.kursSpring.models.Order;
import com.example.kursSpring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    List<Order> findByNumberEndingWith(String string);
}
