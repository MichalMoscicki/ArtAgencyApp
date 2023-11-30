package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String address;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @ManyToMany
    private List<Musician> musicians;
    @ManyToMany
    private List<Song> songs;
    private String description;

}
