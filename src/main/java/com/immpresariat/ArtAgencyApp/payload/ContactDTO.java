package com.immpresariat.ArtAgencyApp.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ContactDTO {

    @NotNull
    private Long id;

    private List<Institution> institutions;

    @NotNull
    private List<ContactPerson> contactPeople;

    @JsonProperty("events")
    @NotNull
    private List<Event> events;


}
