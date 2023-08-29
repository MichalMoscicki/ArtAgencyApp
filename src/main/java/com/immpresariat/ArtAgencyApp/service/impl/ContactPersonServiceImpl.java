package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;

import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    ContactPersonRepository contactPersonRepository;
    ContactRepository contactRepository;
    InputCleaner inputCleaner;
    DTOMapper dtoMapper;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository,
                                    ContactRepository contactRepository,
                                    InputCleaner inputCleaner,
                                    DTOMapper dtoMapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactRepository = contactRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ContactPersonDTO create(ContactPersonDTO unsyncContactPersonDTO, Long contactId) {
        Contact contact = ensureContactExists(contactId);
        ContactPerson unsyncContactPerson = dtoMapper.mapUnsyncDTOToContactPerson(unsyncContactPersonDTO);
        ContactPerson synchronizedContactPerson = contactPersonRepository.save(inputCleaner.clean(unsyncContactPerson));
        updateContact(contact, synchronizedContactPerson);
        return dtoMapper.mapContactPersonToDTO(synchronizedContactPerson);
    }

    @Override
    public List<ContactPersonDTO> getAll() {
        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        return contactPeople.stream().map(dtoMapper::mapContactPersonToDTO).collect(Collectors.toList());
    }

    @Override
    public ContactPersonDTO getById(Long id) {
        ContactPerson contactPerson = ensureContactPersonExists(id);
        return dtoMapper.mapContactPersonToDTO(contactPerson);
    }

    @Override
    public ContactPersonDTO update(ContactPersonDTO updatedContactPersonDTO) {
        ContactPerson contactPerson = ensureContactPersonExists(updatedContactPersonDTO.getId());
        contactPerson.setFirstName(updatedContactPersonDTO.getFirstName());
        contactPerson.setLastName(updatedContactPersonDTO.getLastName());
        contactPerson.setRole(updatedContactPersonDTO.getRole());
        contactPerson.setPhone(updatedContactPersonDTO.getPhone());
        contactPerson.setEmail(updatedContactPersonDTO.getEmail());

        ContactPerson contactPersonDB = contactPersonRepository.save(inputCleaner.clean(contactPerson));
        return dtoMapper.mapContactPersonToDTO(contactPersonDB);
    }

    @Override
    public void delete(Long contactPersonId, Long contactId) {
        Contact contact = ensureContactExists(contactId);
        ContactPerson contactPerson = ensureContactPersonExists(contactPersonId);
        removeContactPersonFromContact(contact, contactPerson);
        contactPersonRepository.deleteById(contactId);
    }

    private Contact ensureContactExists(Long contactId) {
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if (contactOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No contact with id: %s", contactId));
        }
        return contactOptional.get();
    }

    private ContactPerson ensureContactPersonExists(Long id) {
        Optional<ContactPerson> contactPersonOptional = contactPersonRepository.findById(id);
        if (contactPersonOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No contactPerson with id: %s", id));
        }
        return contactPersonOptional.get();
    }

    private void updateContact(Contact contact, ContactPerson synchronizedContactPerson) {
        List<ContactPerson> contactPeople;
        if (contact.getContactPeople() == null) {
            contactPeople = new ArrayList<>();
        } else {
            contactPeople = contact.getContactPeople();
        }
        contactPeople.add(synchronizedContactPerson);
        contact.setContactPeople(contactPeople);
        contactRepository.save(contact);
    }

    private void removeContactPersonFromContact(Contact contact, ContactPerson contactPerson) {
        List<ContactPerson> contactPeople = contact.getContactPeople();
        contactPeople.remove(contactPerson);
        contact.setContactPeople(contactPeople);
        contactRepository.save(contact);
    }
}