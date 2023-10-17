package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.ConcertDetails;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Musician;
import com.immpresariat.ArtAgencyApp.models.Song;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ConcertDTO {
    private Long id;
    private String title;
    LocalDate date;
    private String notes;
    ConcertDetails details;
    private List<Musician> musicians;
    private List<Song> songs;
    private Contact organizer;
}
