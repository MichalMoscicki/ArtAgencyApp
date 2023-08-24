package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany
    private List<Event> events;

    @OneToMany
    private List<ContactPerson> contactPeople;

    @OneToMany
    private List<Institution> institutions;



}
