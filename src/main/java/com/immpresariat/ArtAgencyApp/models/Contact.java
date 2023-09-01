package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private boolean alreadyCooperated;

    private Date updated;


    @OneToMany(fetch = FetchType.EAGER)
    private List<Event> events;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ContactPerson> contactPeople;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Institution> institutions;

}
