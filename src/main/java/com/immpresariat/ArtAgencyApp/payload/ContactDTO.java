package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.Institution;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDTO {

    @NotNull
    private Institution institution;
    @NotNull
    List<ContactPersonDTO> contactPersonDTOS;
    @NotNull
    List<EventDTO> eventDTOS;


}
