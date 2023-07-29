package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactPersonService {

    ContactPersonRepository contactPersonRepository;

    public ContactServiceImpl(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public ContactPerson create(ContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public List<ContactPerson> getAll() {
        return contactPersonRepository.findAll();
    }


    @Override
    public List<ContactPerson> getAllByInstitutionId(Long institutionId) {
        return contactPersonRepository.findAllByInstitutionId(institutionId);
    }

    @Override
    public Optional<ContactPerson> getById(Long id) {
        return contactPersonRepository.findById(id);
    }

    @Override
    public ContactPerson update(Long id, ContactPerson updaedContactPerson) {
        Optional<ContactPerson> contactPersonOptional = contactPersonRepository.findById(id);

        if(contactPersonOptional.isPresent()){
            return contactPersonRepository.save(updaedContactPerson);
        } else {
            throw new ResourceNotFoundException(String.format("No ContactPerson with id: %s", id));
        }
    }

    @Override
    public void delete(Long id) {
        contactPersonRepository.deleteById(id);
    }
}
