package com.ama.training.polar.bookshop.catalog.controller;

import com.ama.training.polar.bookshop.catalog.config.PolarProperties;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("home")
@Hidden
public class HomeController {

    @Autowired
    PolarProperties polarProperties;

    @GetMapping
    public String getGreeting() {
        return polarProperties.getGreeting();
    }

}
