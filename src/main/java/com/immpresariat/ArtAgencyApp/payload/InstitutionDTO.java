package com.immpresariat.ArtAgencyApp.payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstitutionDTO {

    private Long id;
    private String name;
    private String city;
    private String category;
    private boolean alreadyCooperated = false;
    private String notes;

}
