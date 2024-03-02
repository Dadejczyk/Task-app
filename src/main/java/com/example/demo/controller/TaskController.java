package com.example.demo.controller;



import com.example.demo.logic.TaskService;
import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all The Tasks");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTask(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTasks(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //szuka to domyślne zrobionych tasków jeżeli ktoś poda inną wartośc to zostanie ona zmapowana i będzie szukać innej wartości od true czyli wyłącznie false
    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTask(@RequestParam(defaultValue = "true") boolean state) { //@RequestParam czyli parametr żądania
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }


    @PutMapping("/{id}")
    ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate ){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        Task updatedTask = repository.findById(id)
                .map(task -> {
                    task.updateFrom(toUpdate);
                    return repository.save(task);
                })
                .orElse(null);

        return ResponseEntity.ok(updatedTask);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Void> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));

        return ResponseEntity.noContent().build();
    }
}