package com.immpresariat.ArtAgencyApp.utils;


import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.*;
import org.springframework.stereotype.Component;


@Component
public class DTOMapper {

    public InstitutionDTO mapInstitutionToDTO(Institution institution) {
        return InstitutionDTO.builder()
                .id(institution.getId())
                .name(institution.getName())
                .city(institution.getCity())
                .notes(institution.getNotes())
                .category(institution.getCategory())
                .phone(institution.getPhone())
                .email(institution.getEmail())
                .webPage(institution.getWebPage())
                .build();
    }

    public Institution mapUnsyncDTOToInstitution(InstitutionDTO inputInstitutionDTO) {
        return Institution.builder()
                .name(inputInstitutionDTO.getName())
                .city(inputInstitutionDTO.getCity())
                .category(inputInstitutionDTO.getCategory())
                .phone(inputInstitutionDTO.getPhone())
                .email(inputInstitutionDTO.getEmail())
                .webPage(inputInstitutionDTO.getWebPage())
                .notes(inputInstitutionDTO.getNotes())
                .build();
    }

    public Institution mapDTOToInstitution(InstitutionDTO institutionDTO) {
        return Institution.builder()
                .id(institutionDTO.getId())
                .name(institutionDTO.getName())
                .city(institutionDTO.getCity())
                .category(institutionDTO.getCategory())
                .notes(institutionDTO.getNotes())
                .phone(institutionDTO.getPhone())
                .email(institutionDTO.getEmail())
                .webPage(institutionDTO.getWebPage())
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

    public ContactDTO mapContactToDTO(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .title(contact.getTitle())
                .alreadyCooperated(contact.isAlreadyCooperated())
                .updated(contact.getUpdated())
                .description(contact.getDescription())
                .events(contact.getEvents())
                .contactPeople(contact.getContactPeople())
                .institutions(contact.getInstitutions())
                .build();
    }

    public Contact mapDTOToContact(ContactDTO contactDTO) {
        return Contact.builder()
                .id(contactDTO.getId())
                .title(contactDTO.getTitle())
                .alreadyCooperated(contactDTO.isAlreadyCooperated())
                .description(contactDTO.getDescription())
                .updated(contactDTO.getUpdated())
                .events(contactDTO.getEvents())
                .contactPeople(contactDTO.getContactPeople())
                .institutions(contactDTO.getInstitutions())
                .build();
    }

    public TaskDTO mapTaskToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .activationDate(task.getActivationDate())
                .isActive(task.isActive())
                .finished(task.isFinished())
                .updated(task.getUpdated())
                .priority(task.getPriority())
                .build();
    }

    public Task mapDTOToTask(TaskDTO taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .activationDate(taskDTO.getActivationDate())
                .isActive(taskDTO.isActive())
                .finished(taskDTO.isFinished())
                .updated(taskDTO.getUpdated())
                .priority(taskDTO.getPriority())
                .build();
    }
}
