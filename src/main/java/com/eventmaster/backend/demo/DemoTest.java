package com.eventmaster.backend.demo;


import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin
@RequestMapping("/demo")
public class DemoTest {

    @GetMapping
    public String demoTest() {
        return "successful test";
    }


}