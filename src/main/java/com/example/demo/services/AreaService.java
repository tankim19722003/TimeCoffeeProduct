package com.example.demo.services;

import com.example.demo.dtos.AreaDTO;
import com.example.demo.model.Area;
import com.example.demo.repositories.AreaRepository;
import com.example.demo.responses.AreaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService implements IAreaService {

    private final AreaRepository areaRepository;

    @Override
    public List<AreaResponse> getAllArea() {
        List<Area> areas = areaRepository.findAll();

        List<AreaResponse> areaResponses = areas.stream().map(area -> {
                return Area.toAreaResponse(area);
        }).collect(Collectors.toList());
        return areaResponses;
    }

    @Override
    public AreaResponse createArea(AreaDTO areaDTO) throws Exception {
        Area existingArea = areaRepository.findByName(areaDTO.getName());
        if (existingArea == null) {
            Area area =  Area.builder()
                    .name(areaDTO.getName())
                    .build();
            Area AreaSaving = areaRepository.save(area);
            return AreaResponse.builder()
                    .id(AreaSaving.getId())
                    .name(AreaSaving.getName())
                    .build();
        } else {
            throw new Exception("data not found");
        }
    }

    @Override
    public void deleteArea(int id) {
        areaRepository.deleteById(id);
    }
}
