package com.levibrooke.taskmaster;

import com.levibrooke.taskmaster.S3Client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TaskController {
    private String[] statusOptions = {"Available", "Assigned", "Accepted", "Finished"};

    private S3Client s3Client;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

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

    @PutMapping("/tasks/{id}/assign/{assignee}")
    public @ResponseBody Task updateTaskAssignee(@PathVariable String id, @PathVariable String assignee) {
        Task task = taskRepository.findById(id).get();
        task.setAssignee(assignee);
        task.setStatus(statusOptions[1]);
        taskRepository.save(task);
        return taskRepository.findById(id).get();
    }

    @PostMapping("/tasks/{id}/images")
    public @ResponseBody Task uploadTaskImage(@PathVariable String id, @RequestPart(value = "file") MultipartFile file) {
        String pic = this.s3Client.uploadFile(file);
        Task task = taskRepository.findById(id).get();
        task.setPic(pic);

        int indexOfSlash = task.getPic().lastIndexOf("/");

        String fileName = pic.substring(indexOfSlash + 1);

        String resizedBucket = "https://taskmaster-image-upload-resized.s3-us-west-2.amazonaws.com/resized-";

        task.setResizedPic(resizedBucket + fileName);

        taskRepository.save(task);
        return task;
    }
}
