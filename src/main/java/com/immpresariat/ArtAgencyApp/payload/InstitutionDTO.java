package com.immpresariat.ArtAgencyApp.payload;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class InstitutionDTO {

    private Long id;
    private String name;
    private String city;
    private String category;
    private boolean alreadyCooperated = false;
    private String notes;

}
