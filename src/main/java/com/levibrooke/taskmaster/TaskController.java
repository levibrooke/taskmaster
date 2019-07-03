package com.levibrooke.taskmaster;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello, world!";
    }

    @PostMapping("/tasks")
    public @ResponseBody Task createTask(@ModelAttribute Task task) {
        task.setStatus("Available");
        taskRepository.save(task);
        return taskRepository.findById(task.getId()).get();
    }

    
}
