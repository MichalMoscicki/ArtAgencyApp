package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.Musician;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ConcertDTO {
    private Long id;
    private String title;
    private String address;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private List<Musician> musicians;
    private List<SongDTO> songs;

}
