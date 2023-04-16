package com.eventmaster.backend.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoTestTest {

    @Test
    void demoTest() {
        String expected = "successful test";
        DemoTest demo = new DemoTest();

        assertEquals(expected, demo.demoTest());
    }
}