package com.immpresariat.ArtAgencyApp.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    private Long id;
    private String name;
    private String description;
    private int monthWhenOrganized;

}
