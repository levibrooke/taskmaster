package com.levibrooke.taskmaster;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello, world!";
    }
}
