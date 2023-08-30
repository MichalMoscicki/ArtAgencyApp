package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final EventRepository eventRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final InstitutionRepository institutionRepository;
    private final DTOMapper dtoMapper;

    public ContactServiceImpl(ContactRepository contactRepository,
                              EventRepository eventRepository,
                              ContactPersonRepository contactPersonRepository,
                              InstitutionRepository institutionRepository,
                              DTOMapper dtoMapper) {
        this.contactRepository = contactRepository;
        this.eventRepository = eventRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.institutionRepository = institutionRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ContactDTO getById(Long id) {
        Contact contact = ensureContactExists(id);
        return dtoMapper.mapContactToDTO(contact);
    }

    @Override
    public List<ContactDTO> getAll() {
        List<Contact> contacts = contactRepository.findAll();
        return contacts.stream().map(dtoMapper::mapContactToDTO).collect(Collectors.toList());
    }

    @Override
    public ContactDTO create() {
        Contact contact = contactRepository.save(new Contact());
        return dtoMapper.mapContactToDTO(contact);
    }

    @Override
    public void deleteWithAssociatedData(Long id) {
        Contact contact = ensureContactExists(id);
        deleteAssociatedEvents(contact);
        deleteAssociatedInstitutions(contact);
        deleteAssociatedContactPeople(contact);
        contactRepository.deleteById(id);
    }

    private Contact ensureContactExists(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isEmpty()) {
            throw new ResourceNotFoundException("No contact with id: " + id);
        }
        return contactOptional.get();
    }
    private void deleteAssociatedEvents(Contact contact) {
        List<Event> events = contact.getEvents();
        contact.setEvents(new ArrayList<>());
        contactRepository.save(contact);
        eventRepository.deleteAll(events);

    }
    private void deleteAssociatedInstitutions(Contact contact) {
        List<Institution> institutions = contact.getInstitutions();
        contact.setInstitutions(new ArrayList<>());
        contactRepository.save(contact);
        institutionRepository.deleteAll(institutions);
    }
    private void deleteAssociatedContactPeople(Contact contact) {
        List<ContactPerson> contactPeople = contact.getContactPeople();
        contact.setContactPeople(new ArrayList<>());
        contactRepository.save(contact);
        contactPersonRepository.deleteAll(contactPeople);
    }
}


