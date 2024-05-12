package com.example.demo.services;

import com.example.demo.dtos.UserDTO;
import com.example.demo.responses.UserResponse;

public interface IUserService{
    UserResponse login(UserDTO user) throws Exception;
}
