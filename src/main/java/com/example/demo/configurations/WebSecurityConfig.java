package com.example.demo.configurations;

import com.example.demo.filters.JwtTokenFilter;
import com.example.demo.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
// cho request nào được đi qua
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            // user
                            .requestMatchers(
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix)
                            )
                            .permitAll()

                            // CATEGORIES
                            .requestMatchers(GET,
                                    String.format("%s/category/**",apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/category/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/category/**",apiPrefix)).hasRole(Role.ADMIN)

                            //AREA

                            .requestMatchers(GET,
                                    String.format("%s/area/**",apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/area/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/area/**",apiPrefix)).hasRole(Role.ADMIN)

                            // table
                            .requestMatchers(GET,
                                    String.format("%s/table/**",apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/table/**",apiPrefix)).hasRole(Role.ADMIN)

                            //product

                            .requestMatchers(GET,
                                    String.format("%s/product/**",apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/product/**",apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/product/**",apiPrefix)).hasRole(Role.ADMIN)

                            // order detail
                            .requestMatchers(GET,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(POST,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(PUT,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(DELETE,
                                    String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            // orders
                            .requestMatchers(POST,
                                    String.format("%s/order/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/order/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)

                            .requestMatchers(PUT,
                                    String.format("%s/order/changeTableInOrder",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/order/**",apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token","Accept-Languague"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**",configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();
    }
}
