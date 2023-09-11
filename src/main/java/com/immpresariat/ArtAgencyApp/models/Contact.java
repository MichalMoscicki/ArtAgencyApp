package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.lang.Nullable;

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

    private String description;

    private boolean alreadyCooperated;

    private Date updated;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Event> events;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ContactPerson> contactPeople;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Institution> institutions;

}
