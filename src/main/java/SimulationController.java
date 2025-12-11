package com.gameoflife;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "*")
public class SimulationController {
    private CellManager manager;
    private ResourcePool resourcePool;
    private boolean running = false;
    
    @PostMapping("/start")
    public Map<String, Object> startSimulation(
            @RequestParam(required = false, defaultValue = "15") Integer initialFood) {
        if (running) {
            return Map.of("error", "Simulation already running");
        }
        
        resourcePool = new ResourcePool(initialFood);
        manager = new CellManager(resourcePool);
        
        // Create initial cells
        manager.addCell(new AsexualCell(1, manager, resourcePool));
        manager.addCell(new AsexualCell(2, manager, resourcePool));
        manager.addCell(new SexualCell(3, manager, resourcePool));
        manager.addCell(new SexualCell(4, manager, resourcePool));
        
        running = true;
        
        return Map.of(
            "status", "started",
            "initialFood", initialFood,
            "cells", manager.getAliveCellsCount()
        );
    }
    
    @PostMapping("/stop")
    public Map<String, Object> stopSimulation() {
        if (!running) {
            return Map.of("error", "No simulation running");
        }
        
        manager.stopAll();
        running = false;
        
        return Map.of("status", "stopped");
    }
    
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        if (!running) {
            return Map.of("running", false);
        }
        
        List<Map<String, Object>> cellData = new ArrayList<>();
        for (Cell cell : manager.getCells()) {
            cellData.add(Map.of(
                "id", cell.getId(),
                "type", cell instanceof AsexualCell ? "asexual" : "sexual",
                "alive", cell.isAlive(),
                "hungry", cell.isHungry(),
                "reproducing", cell.isWantingToReproduce()
            ));
        }
        
        return Map.of(
            "running", true,
            "aliveCells", manager.getAliveCellsCount(),
            "totalCells", manager.getCells().size(),
            "availableFood", resourcePool.getAvailableFood(),
            "cells", cellData
        );
    }
    
    @PostMapping("/cells/add")
    public Map<String, Object> addCell(@RequestParam String type) {
        if (!running) {
            return Map.of("error", "Start simulation first");
        }
        
        int newId = manager.getCells().size() + 1;
        Cell newCell = type.equals("asexual") 
            ? new AsexualCell(newId, manager, resourcePool)
            : new SexualCell(newId, manager, resourcePool);
            
        manager.addCell(newCell);
        
        return Map.of(
            "status", "added",
            "cellId", newId,
            "type", type
        );
    }
}