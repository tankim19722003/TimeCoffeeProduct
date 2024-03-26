package com.example.demo.controllers;

import com.example.demo.dtos.AreaDTO;
import com.example.demo.model.Area;
import com.example.demo.responses.AreaResponse;
import com.example.demo.services.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/timecoffee/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @PostMapping("")
    public ResponseEntity<?> createArea(
            @RequestBody AreaDTO areaDTO
    ) {
        try {
            AreaResponse areaCreated = areaService.createArea(areaDTO);
            return ResponseEntity.ok().body(areaCreated);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Area>> getAllArea() {
        return ResponseEntity.ok().body(areaService.getAllArea());
    }

    @DeleteMapping("/{id}")
    public void deleteArea(
            @PathVariable int id) {
        areaService.deleteArea(id);
    }

}
