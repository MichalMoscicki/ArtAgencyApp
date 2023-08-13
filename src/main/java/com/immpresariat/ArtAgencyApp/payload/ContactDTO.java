package com.immpresariat.ArtAgencyApp.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Institution institution;
    @JsonProperty("contactPeople")
    @NotNull
    List<ContactPersonDTO> contactPersonDTOS;

    @JsonProperty("events")
    @NotNull
    List<EventDTO> eventDTOS;


}
