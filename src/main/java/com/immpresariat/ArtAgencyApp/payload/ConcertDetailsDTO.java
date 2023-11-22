package com.immpresariat.ArtAgencyApp.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConcertDetailsDTO {

    private Long id;
    private String address;
    private Date start;
    private Date end;
    private Long concertId;

}
