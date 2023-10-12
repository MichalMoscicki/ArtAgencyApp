package com.immpresariat.ArtAgencyApp.utils;


import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.*;
import org.springframework.stereotype.Component;


@Component
public class DTOMapper {

    public InstitutionDTO mapToDTO(Institution institution) {
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

    public Institution mapToEntity(InstitutionDTO institutionDTO) {
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

    public EventDTO mapToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .description(event.getDescription())
                .monthWhenOrganized(event.getMonthWhenOrganized())
                .name(event.getName())
                .build();
    }

    public Event mapToEntity(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .monthWhenOrganized(eventDTO.getMonthWhenOrganized())
                .build();
    }

    public ContactPersonDTO mapToDTO(ContactPerson contactPerson) {
        return ContactPersonDTO.builder()
                .id(contactPerson.getId())
                .firstName(contactPerson.getFirstName())
                .lastName(contactPerson.getLastName())
                .email(contactPerson.getEmail())
                .phone(contactPerson.getPhone())
                .role(contactPerson.getRole())
                .build();
    }

    public ContactPerson mapToEntity(ContactPersonDTO contactPersonDTO) {
        return ContactPerson.builder()
                .id(contactPersonDTO.getId())
                .firstName(contactPersonDTO.getFirstName())
                .lastName(contactPersonDTO.getLastName())
                .phone(contactPersonDTO.getPhone())
                .email(contactPersonDTO.getEmail())
                .role(contactPersonDTO.getRole())
                .build();
    }

    public ContactDTO mapToDTO(Contact contact) {
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

    public Contact mapToEntity(ContactDTO contactDTO) {
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

    public TaskDTO mapToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .activationDate(task.getActivationDate())
                .active(task.isActive())
                .finished(task.isFinished())
                .updated(task.getUpdated())
                .priority(task.getPriority())
                .attachment(task.getAttachment())
                .build();
    }

    public Task mapToEntity(TaskDTO taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .activationDate(taskDTO.getActivationDate())
                .active(taskDTO.isActive())
                .finished(taskDTO.isFinished())
                .updated(taskDTO.getUpdated())
                .priority(taskDTO.getPriority())
                .attachment(taskDTO.getAttachment())
                .build();
    }

    public TaskAttachmentDTO mapToDTO(TaskAttachment attachment) {
        return TaskAttachmentDTO.builder()
                .id(attachment.getId())
                .contacts(attachment.getContacts())
                .build();
    }

    public TaskAttachment mapToEntity(TaskAttachmentDTO attachmentDTO) {
        return TaskAttachment.builder()
                .id(attachmentDTO.getId())
                .contacts(attachmentDTO.getContacts())
                .build();
    }

    public InstrumentDTO mapToDTO(Instrument instrument) {
        return InstrumentDTO.builder()
                .id(instrument.getId())
                .name(instrument.getName())
                .build();
    }

    public Instrument mapToEntity(InstrumentDTO instrumentDTO) {
        return Instrument.builder()
                .id(instrumentDTO.getId())
                .name(instrumentDTO.getName())
                .build();
    }

    public MusicianDTO mapToDTO(Musician musician) {
        return MusicianDTO.builder()
                .id(musician.getId())
                .firstName(musician.getFirstName())
                .lastName(musician.getLastName())
                .instruments(musician.getInstruments())
                .email(musician.getEmail())
                .phone(musician.getPhone())
                .notes(musician.getNotes())
                .build();
    }

    public Musician mapToEntity(MusicianDTO musicianDTO) {
        return Musician.builder()
                .id(musicianDTO.getId())
                .firstName(musicianDTO.getFirstName())
                .lastName(musicianDTO.getLastName())
                .instruments(musicianDTO.getInstruments())
                .email(musicianDTO.getEmail())
                .phone(musicianDTO.getPhone())
                .notes(musicianDTO.getNotes())
                .build();
    }
}
