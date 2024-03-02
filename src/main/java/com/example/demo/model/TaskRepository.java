package com.example.demo.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll(); //pobiera wszystkie Taski
    Page<Task> findAll(Pageable page);
    Optional<Task> findById(Integer id);// pobiera tylko konkretny Task

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);


    Task save(Task entity); //Task do zapisania/stworzenia
    List<Task> findByDone(boolean done); // to szuka po liscie tasków które są zrobione na true można zrobić tez false.czyli było by same findByDoneIsTrue/False
}
