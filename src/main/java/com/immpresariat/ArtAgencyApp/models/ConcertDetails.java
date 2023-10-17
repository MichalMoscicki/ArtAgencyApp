package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ConcertDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    @NotNull
    private Date soundcheck;
    @NotNull
    private Date performance;
    private boolean outdoor;
    private String description;

}

//concert nic nie wie o detailsach, pobiera się je osobnym zapytaniem.
// Zadbaj o to, żeby id były takie same;
