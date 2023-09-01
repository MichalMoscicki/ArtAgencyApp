package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ContactDTO {

    private Long id;

    private String title;

    private boolean alreadyCooperated;

    private Date updated;

    private List<Institution> institutions;

    private List<ContactPerson> contactPeople;

    private List<Event> events;


}
