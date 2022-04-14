package com.careerdevs.gorestsql.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String getRootRoute () {
        return "You are at the root route";
    }
}
