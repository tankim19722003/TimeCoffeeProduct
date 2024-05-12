package com.example.demo.services;

import com.example.demo.dtos.AreaDTO;
import com.example.demo.model.Area;
import com.example.demo.responses.AreaResponse;

import java.util.List;

public interface IAreaService {
    AreaResponse createArea(AreaDTO areaDTO) throws Exception;
    void deleteArea(int id);
    List<AreaResponse> getAllArea();
}
