package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "City must not be blank")
    private String city;
    private boolean alreadyCooperated = false;
    @Size(max = 500, message = "Notes can not be longer then 500 characters")
    private String notes;


}
