package com.example.demo.services;

import com.example.demo.dtos.TableDTO;
import com.example.demo.model.Area;
import com.example.demo.model.Tables;
import com.example.demo.repositories.AreaRepository;
import com.example.demo.repositories.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService implements ITableService{
    private final TableRepository tableRepository;

    private final AreaRepository areaRepository;
    @Override
    public Tables createTable(TableDTO tableDTO) throws Exception {
        boolean existingTable = tableRepository.existsByName(tableDTO.getName());

        if (existingTable) {
            throw new Exception("Table existed");
        }
        Area existingArea = areaRepository.findById(tableDTO.getAreaId()).orElseThrow(
                () -> new Exception("Area doesn't exist")
        );
        Tables tables = Tables.builder()
                .area(existingArea)
                .name(tableDTO.getName())
                .status(false)
                .build();
        return tableRepository.save(tables);
    }

    @Override
    public void deleteTable(int id) {
        tableRepository.deleteById(id);
    }

    @Override
    public List<Tables> findAllByAreaId(int AreaId) throws Exception {
        List<Tables> tables = new ArrayList<>();
        tables = tableRepository.findAllByAreaId(AreaId);

        if (tables == null) throw new Exception("Data does not found");

        return tables;
    }
}
