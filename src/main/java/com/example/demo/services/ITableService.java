package com.example.demo.services;

import com.example.demo.dtos.TableDTO;
import com.example.demo.model.Tables;
import com.example.demo.responses.TableResponse;
import com.example.demo.responses.TableResponseByArea;

import java.util.List;

public interface ITableService {
    Tables createTable(TableDTO tableDTO) throws Exception;
    void deleteTable(int id);

    List<TableResponseByArea> findAllByAreaId(int AreaId) throws Exception;
}
