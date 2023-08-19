package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;

import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    ContactPersonRepository contactPersonRepository;
    InputCleaner inputCleaner;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, InputCleaner inputCleaner) {
        this.contactPersonRepository = contactPersonRepository;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public ContactPerson create(ContactPerson contactPerson) {

        return contactPersonRepository.save(inputCleaner.clean(contactPerson));
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
    public ContactPerson update(ContactPerson updatedContactPerson) {
        Optional<ContactPerson> contactPersonOptional = contactPersonRepository.findById(updatedContactPerson.getId());

        if(contactPersonOptional.isPresent()){
            return contactPersonRepository.save(inputCleaner.clean(updatedContactPerson));
        } else {
            throw new ResourceNotFoundException(String.format("No ContactPerson with id: %s", updatedContactPerson.getId()));
        }
    }

    @Override
    public void delete(Long id) {
        contactPersonRepository.deleteById(id);
    }

    @Override
    public void delete(ContactPerson contactPerson) {
        contactPersonRepository.delete(contactPerson);
    }
}
