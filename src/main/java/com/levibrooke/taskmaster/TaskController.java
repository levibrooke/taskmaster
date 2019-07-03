package com.levibrooke.taskmaster;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class TaskController {
    private String[] statusOptions = {"Available", "Assigned", "Accepted", "Finished"};

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

        if (task.assigneeIsEmpty() == false) {
            task.setStatus(statusOptions[1]);
        } else if (task.assigneeIsEmpty() == true) {
            task.setStatus(statusOptions[0]);
        }

        taskRepository.save(task);
        return taskRepository.findById(task.getId()).get();
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    @PutMapping("/tasks/{id}/state")
    public @ResponseBody Task updateTaskState(@PathVariable String id) {
        Task task = taskRepository.findById(id).get();
        int index = Arrays.asList(statusOptions).indexOf(task.getStatus());
        if (index < 3) {
            task.setStatus(statusOptions[index + 1]);
        }
        taskRepository.save(task);
        return taskRepository.findById(task.getId()).get();
    }

    @DeleteMapping("tasks/{id}")
    public void deleteTask(@PathVariable String id) {
        Task task = taskRepository.findById(id).get();
        taskRepository.delete(task);
    }

    @GetMapping("/users/{name}/tasks")
    public List<Task> getTasksByUser(@PathVariable String name) {
        return taskRepository.findByAssignee(name);
    }
}
