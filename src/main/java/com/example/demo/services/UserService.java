package com.example.demo.services;

import com.example.demo.components.JwtTokenUtil;
import com.example.demo.dtos.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponse login(UserDTO userDTO) throws Exception {
        User user = userRepository.findByAccount(userDTO.getAccount()).orElseThrow(
                ()-> new Exception("User does not exist")
        );

        //check valid password
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new Exception("Invalid user");
        }
        String token = jwtTokenUtil.generateToken(user);
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .role(user.getRole().getName())
                .token(token)
                .build();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user.getAccount(),
                        userDTO.getPassword(),
                        user.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return userResponse;
    }

    public void register(UserDTO userDTO) {
        User user = User.builder()
                .account(userDTO.getAccount())
                .build();

        Role role = Role.builder()
                .id(1)
                .name("Admin")
                .build();
        String password = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(password);
        user.setRole(role);

        userRepository.save(user);
    }
}
