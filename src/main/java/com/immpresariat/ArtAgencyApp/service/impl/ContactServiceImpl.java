package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactResponse;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        return dtoMapper.mapContactToDTO(contact);
    }

    @Override
    public ContactResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Contact> page = contactRepository.findAll(pageable);
        List<Contact> contacts = page.getContent();
        List<ContactDTO> content =  contacts.stream().map(dtoMapper::mapContactToDTO).toList();
        return createResponse(page, content);
    }


    @Override
    public ContactDTO create(ContactDTO unsyncContactDTO) {
                unsyncContactDTO.setUpdated(new Date());
        Contact contact = contactRepository.save(inputCleaner.clean(dtoMapper.mapDTOToContact(unsyncContactDTO)));
        return dtoMapper.mapContactToDTO(contact);
    }

    @Override
    public ContactDTO update(ContactDTO contactDTO) {
        ensureContactExists(contactDTO.getId());
        contactDTO.setUpdated(new Date());
        Contact contact = contactRepository.save(inputCleaner.clean(dtoMapper.mapDTOToContact(contactDTO)));
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
    private ContactResponse createResponse(Page<Contact> page, List<ContactDTO> content) {
        ContactResponse response = new ContactResponse();
        response.setContent(content);
        response.setPageSize(page.getSize());
        response.setPageNo(page.getNumber());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }
}


