package com.example.demo.model;

import com.example.demo.responses.AreaResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Builder
@Table(name = "areas")
@AllArgsConstructor
@NoArgsConstructor
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tables> Tables = new ArrayList<>();

    public static AreaResponse toAreaResponse(Area area) {
        return AreaResponse.builder()
                .id(area.getId())
                .name(area.getName())
                .build();
    }

}
