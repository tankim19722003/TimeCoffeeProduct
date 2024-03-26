package com.example.demo.model;

import com.example.demo.responses.TableResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tables")
@AllArgsConstructor
@NoArgsConstructor
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "area_id")
    @JsonBackReference
    @JsonIgnore
    private Area area;

    private boolean status;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    public static TableResponse toTableResponse(Tables table) {
        TableResponse tableResponse = TableResponse.builder()
                .id(table.getId())
                .name(table.getName())
                .build();
        return  tableResponse;
    }
}
