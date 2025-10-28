package com.mun.theatrems.model;

import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // MANY-TO-MANY relationship with Movie (inverse side)
    @ManyToMany(mappedBy = "genres")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
