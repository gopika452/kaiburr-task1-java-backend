package com.gopika.kaiburr.task1_java_rest.repository;

import com.gopika.kaiburr.task1_java_rest.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByNameContaining(String name); // search tasks by name containing a string
}
