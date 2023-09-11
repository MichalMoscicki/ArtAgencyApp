package com.immpresariat.ArtAgencyApp.payload;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class InstitutionDTO {

    private Long id;
    private String name;
    private String city;
    private String category;
    private String notes;

    private String phone;

    private String email;

    private String webPage;

}
