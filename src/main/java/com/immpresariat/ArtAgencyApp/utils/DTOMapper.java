package com.immpresariat.ArtAgencyApp.utils;


import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import org.springframework.stereotype.Component;


@Component
public class DTOMapper {

    public InstitutionDTO mapInstitutionToDTO (Institution institution){
        return InstitutionDTO.builder()
                .id(institution.getId())
                .name(institution.getName())
                .city(institution.getCity())
                .notes(institution.getNotes())
                .category(institution.getCategory())
                .build();
    }

    public Institution mapUnsyncDTOToInstitution(InstitutionDTO inputInstitutionDTO){
        return Institution.builder()
                .name(inputInstitutionDTO.getName())
                .city(inputInstitutionDTO.getCity())
                .category(inputInstitutionDTO.getCategory())
                .notes(inputInstitutionDTO.getNotes())
                .build();
    }

    public Institution mapDTOToInstitution(InstitutionDTO institutionDTO){
        return Institution.builder()
                .id(institutionDTO.getId())
                .name(institutionDTO.getName())
                .city(institutionDTO.getCity())
                .category(institutionDTO.getCategory())
                .notes(institutionDTO.getNotes())
                .build();
    }

    public EventDTO mapEventToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .description(event.getDescription())
                .monthWhenOrganized(event.getMonthWhenOrganized())
                .name(event.getName())
                .build();
    }

    public Event mapUnsyncInputDTOToEvent(EventDTO inputEventDTO) {
        return Event.builder()
                .name(inputEventDTO.getName())
                .description(inputEventDTO.getDescription())
                .monthWhenOrganized(inputEventDTO.getMonthWhenOrganized())
                .build();
    }

    public Event mapDTOToEvent(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
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

    public ContactPerson mapDTOToContactPerson(ContactPersonDTO contactPersonDTO) {
        return ContactPerson.builder()
                .id(contactPersonDTO.getId())
                .firstName(contactPersonDTO.getFirstName())
                .lastName(contactPersonDTO.getLastName())
                .phone(contactPersonDTO.getPhone())
                .email(contactPersonDTO.getEmail())
                .role(contactPersonDTO.getRole())
                .build();
    }

    public ContactPerson mapUnsyncDTOToContactPerson(ContactPersonDTO unsyncContactPersonDTO) {
        return ContactPerson.builder()
                .firstName(unsyncContactPersonDTO.getFirstName())
                .lastName(unsyncContactPersonDTO.getLastName())
                .phone(unsyncContactPersonDTO.getPhone())
                .email(unsyncContactPersonDTO.getEmail())
                .role(unsyncContactPersonDTO.getRole())
                .build();
    }

    public ContactDTO mapContactToDTO(Contact contact){
        return ContactDTO.builder()
                .id(contact.getId())
                .title(contact.getTitle())
                .alreadyCooperated(contact.isAlreadyCooperated())
                .updated(contact.getUpdated())
                .description(contact.getDescription())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .webPage(contact.getWebPage())
                .events(contact.getEvents())
                .contactPeople(contact.getContactPeople())
                .institutions(contact.getInstitutions())
                .build();
    }

    public Contact mapDTOToContact(ContactDTO contactDTO){
        return Contact.builder()
                .id(contactDTO.getId())
                .title(contactDTO.getTitle())
                .alreadyCooperated(contactDTO.isAlreadyCooperated())
                .description(contactDTO.getDescription())
                .phone(contactDTO.getPhone())
                .email(contactDTO.getEmail())
                .webPage(contactDTO.getWebPage())
                .updated(contactDTO.getUpdated())
                .events(contactDTO.getEvents())
                .contactPeople(contactDTO.getContactPeople())
                .institutions(contactDTO.getInstitutions())
                .build();
    }

}
