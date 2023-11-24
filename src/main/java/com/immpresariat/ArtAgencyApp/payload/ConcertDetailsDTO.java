package com.immpresariat.ArtAgencyApp.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConcertDetailsDTO {

    private Long id;
    private String address;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long concertId;

}
