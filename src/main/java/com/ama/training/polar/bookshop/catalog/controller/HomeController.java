package com.ama.training.polar.bookshop.catalog.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class HomeController {

    @GetMapping
    public String getGreeting() {
        return "Welcome to the book catalog!";
    }

}
