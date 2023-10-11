package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.NotImportedContactInfo;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final EventRepository eventRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final InstitutionRepository institutionRepository;
    private final DTOMapper dtoMapper;
    private final InputCleaner inputCleaner;

    public ContactServiceImpl(ContactRepository contactRepository,
                              EventRepository eventRepository,
                              ContactPersonRepository contactPersonRepository,
                              InstitutionRepository institutionRepository,
                              DTOMapper dtoMapper,
                              InputCleaner inputCleaner) {
        this.contactRepository = contactRepository;
        this.eventRepository = eventRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.institutionRepository = institutionRepository;
        this.dtoMapper = dtoMapper;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public ContactDTO getById(Long id) {
        Contact contact = ensureContactExists(id);
        return dtoMapper.mapToDTO(contact);
    }

    @Override
    public PageResponse<ContactDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Contact> page = contactRepository.findAll(pageable);
        List<Contact> contacts = page.getContent();
        List<ContactDTO> content = contacts.stream().map(dtoMapper::mapToDTO).toList();
        return PageResponse.createResponse(page, content);
    }


    @Override
    public ContactDTO create(ContactDTO unsyncContactDTO) {
        unsyncContactDTO.setUpdated(new Date());
        Contact contact = contactRepository.save(inputCleaner.clean(dtoMapper.mapToEntity(unsyncContactDTO)));
        return dtoMapper.mapToDTO(contact);
    }

    //todo - czy to dobrze, że przed zapisaniem nie ustawiam id jako nulla? Tracę nieco miejsca db.
    @Transactional
    @Override
    public NotImportedContactInfo saveImportedContact(Contact contact) {

        if (contactRepository.existsByTitleAndUpdated(contact.getTitle(), contact.getUpdated())) {
            return new NotImportedContactInfo("", contact);
        }

        try {
            List<Institution> inputInstitutions = contact.getInstitutions();
            List<ContactPerson> inputContactPeople = contact.getContactPeople();
            List<Event> inputEvents = contact.getEvents();

            List<Institution> savedInstitutions = institutionRepository.saveAll(inputInstitutions);
            List<ContactPerson> savedContactPeople = contactPersonRepository.saveAll(inputContactPeople);
            List<Event> savedEvents = eventRepository.saveAll(inputEvents);

            contact.setContactPeople(savedContactPeople);
            contact.setInstitutions(savedInstitutions);
            contact.setEvents(savedEvents);

            contactRepository.save(contact);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new NotImportedContactInfo(e.getMessage(), contact);
        }
        return null;
    }


    @Override
    public ContactDTO update(ContactDTO contactDTO) {
        ensureContactExists(contactDTO.getId());
        contactDTO.setUpdated(new Date());
        Contact contact = contactRepository.save(inputCleaner.clean(dtoMapper.mapToEntity(contactDTO)));
        return dtoMapper.mapToDTO(contact);
    }

    @Override
    public void deleteWithAssociatedData(Long id) {
        Contact contact = ensureContactExists(id);
        deleteAssociatedEvents(contact);
        deleteAssociatedInstitutions(contact);
        deleteAssociatedContactPeople(contact);
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> export() {
        return contactRepository.findAll();
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


