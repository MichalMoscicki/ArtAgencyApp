package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final DTOMapper dtoMapper;

    public ContactServiceImpl(ContactRepository contactRepository, DTOMapper dtoMapper) {
        this.contactRepository = contactRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ContactDTO getById(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if(contactOptional.isEmpty()){
            throw new ResourceNotFoundException("No contact with give id: " + id);
        }
        return dtoMapper.mapContactToDTO(contactOptional.get());
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

}


