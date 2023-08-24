package com.immpresariat.ArtAgencyApp.models;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name must not be blank")
    private String name;

    @Size(max=500, message = "Description can not be longer then 500 characters")
    private String description;

    @Min(1)
    @Max(12)
    private int monthWhenOrganized;


}
