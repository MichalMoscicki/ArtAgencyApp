package com.immpresariat.ArtAgencyApp.utils;


import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class DTOMapper {

    InstitutionService institutionService;
    EventService eventService;
    ContactPersonService contactPersonService;

    public DTOMapper(InstitutionService institutionService, EventService eventService, ContactPersonService contactPersonService) {
        this.institutionService = institutionService;
        this.eventService = eventService;
        this.contactPersonService = contactPersonService;
    }

    public ContactDTO mapToContactDTO(Long institutionId) {


        Optional<Institution> institutionOptional = institutionService.getById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with given id: %s", institutionId));
        }

        List<Event> events = eventService.getAllByInstitutionId(institutionId);
        List<EventDTO> eventDTOS = events.stream().map(this::mapEventToDTO).toList();
        List<ContactPerson> contactPeople = contactPersonService.getAllByInstitutionId(institutionId);
        List<ContactPersonDTO> contactPersonDTOS = contactPeople.stream().map(this::mapContactPersonToDTO).toList();

        return ContactDTO.builder()
                .institution(institutionOptional.get())
                .contactPersonDTOS(contactPersonDTOS)
                .eventDTOS(eventDTOS)
                .build();
    }


    public EventDTO mapEventToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .monthWhenOrganized(event.getMonthWhenOrganized())
                .build();
    }

    public Event mapDTOtoEvent(EventDTO eventDTO, Long institutionId) {
        Institution institution = institutionService.getById(institutionId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("No institution with given id: %s", institutionId))
        );

        return Event.builder()
              //  .id(eventDTO.getId())
                .institution(institution)
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .monthWhenOrganized(eventDTO.getMonthWhenOrganized())
                .build();
    }

    public ContactPersonDTO mapContactPersonToDTO(ContactPerson contactPerson) {
        return ContactPersonDTO.builder()
                .id(contactPerson.getId())
                .firstName(contactPerson.getFirstName())
                .lastName(contactPerson.getLastName())
                .email(contactPerson.getEmail())
                .phone(contactPerson.getPhone())
                .role(contactPerson.getRole())
                .build();
    }

    public ContactPerson mapDTOtoContactPerson(ContactPersonDTO contactPersonDTO, Long institutionId) {
        Institution institution = institutionService.getById(institutionId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("No institution with given id: %s", institutionId))
        );

        return ContactPerson.builder()
            //    .id(contactPersonDTO.getId())
                .institution(institution)
                .firstName(contactPersonDTO.getFirstName())
                .lastName(contactPersonDTO.getLastName())
                .role(contactPersonDTO.getRole())
                .phone(contactPersonDTO.getPhone())
                .email(contactPersonDTO.getEmail())
                .build();
    }

    public InstitutionDTO mapInstitutionToDTO (Institution institution){
        return InstitutionDTO.builder()
                .id(institution.getId())
                .name(institution.getName())
                .city(institution.getCity())
                .notes(institution.getNotes())
                .category(institution.getCategory())
                .alreadyCooperated(institution.isAlreadyCooperated())
                .build();
    }

    public Institution mapInputDTOToInstitution(InstitutionDTO inputInstitutionDTO){
        return Institution.builder()
                .name(inputInstitutionDTO.getName())
                .city(inputInstitutionDTO.getCity())
                .category(inputInstitutionDTO.getCategory())
                .alreadyCooperated(inputInstitutionDTO.isAlreadyCooperated())
                .notes(inputInstitutionDTO.getNotes())
                .build();
    }

    public Institution mapDTOToInstitution(InstitutionDTO institutionDTO){
        return Institution.builder()
                .id(institutionDTO.getId())
                .name(institutionDTO.getName())
                .city(institutionDTO.getCity())
                .category(institutionDTO.getCategory())
                .alreadyCooperated(institutionDTO.isAlreadyCooperated())
                .notes(institutionDTO.getNotes())
                .build();
    }

}
