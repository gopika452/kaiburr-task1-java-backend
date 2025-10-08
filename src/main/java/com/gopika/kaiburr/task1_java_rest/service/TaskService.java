package com.gopika.kaiburr.task1_java_rest.service;

import com.gopika.kaiburr.task1_java_rest.model.Task;
import com.gopika.kaiburr.task1_java_rest.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return repository.findById(id);
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }

    public void deleteTask(String id) {
        repository.deleteById(id);
    }

    public List<Task> searchByName(String name) {
        return repository.findByNameContaining(name);
    }
}

