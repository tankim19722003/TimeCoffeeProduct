package com.example.demo.services;

import com.example.demo.dtos.TableDTO;
import com.example.demo.model.Tables;

import java.util.List;

public interface ITableService {
    Tables createTable(TableDTO tableDTO) throws Exception;
    void deleteTable(int id);

    List<Tables> findAllByAreaId(int AreaId) throws Exception;
}
