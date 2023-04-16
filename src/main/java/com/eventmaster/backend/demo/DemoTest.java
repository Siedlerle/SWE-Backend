package com.eventmaster.backend.demo;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


public class DemoTest {

    @RequestMapping(value="/controller", method=GET)
    @ResponseBody
    public String demoTest() {
        return "successful test";
    }


}