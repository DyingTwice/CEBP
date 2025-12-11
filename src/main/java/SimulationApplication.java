package com.gameoflife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class SimulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimulationApplication.class, args);
        System.out.println("=== Game of Life REST API Started ===");
        System.out.println("Access at: http://localhost:8086/api/simulation/status");
    }
}