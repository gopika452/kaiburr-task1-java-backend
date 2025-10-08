package com.gopika.kaiburr.task1_java_rest.controller;

import com.gopika.kaiburr.task1_java_rest.model.Task;
import com.gopika.kaiburr.task1_java_rest.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /tasks
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) String name) {
        if (name != null) {
            List<Task> tasks = taskService.searchByName(name);
            if (tasks.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // GET /tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /tasks
    @PutMapping
    public ResponseEntity<Task> createOrUpdateTask(@RequestBody Task task) {
        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.ok(savedTask);
    }

    // DELETE /tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    // ✅ NEW: POST /tasks/{id}/execution
    @PostMapping("/{id}/execution")
    public ResponseEntity<?> executeTask(@PathVariable String id) {
        return taskService.getTaskById(id).map(task -> {
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", task.getCommand()});
                process.waitFor();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                task.getTaskExecutions().add(output.toString().trim());
                taskService.saveTask(task);

                return ResponseEntity.ok(task);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error running command: " + e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
