package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;

import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    ContactPersonRepository contactPersonRepository;
    InstitutionRepository institutionRepository;
    InputCleaner inputCleaner;
    DTOMapper dtoMapper;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository,
                                    InstitutionRepository institutionRepository,
                                    InputCleaner inputCleaner,
                                    DTOMapper dtoMapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.institutionRepository = institutionRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ContactPersonDTO create(ContactPersonDTO unsyncContactPersonDTO, Long institutionId) {
        Institution institution = ensureInstitutionExists(institutionId);
        ContactPerson unsyncContactPerson = dtoMapper.mapUnsyncDTOToContactPerson(unsyncContactPersonDTO);
        ContactPerson synchronizedContactPerson = contactPersonRepository.save(inputCleaner.clean(unsyncContactPerson));
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
    public void delete(Long id) {
        contactPersonRepository.deleteById(id);
    }

    @Override
    public void delete(ContactPerson contactPerson) {
        contactPersonRepository.delete(contactPerson);
    }

    private Institution ensureInstitutionExists(Long institutionId) {
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", institutionId));
        }
        return institutionOptional.get();
    }

    private ContactPerson ensureContactPersonExists(Long id) {
        Optional<ContactPerson> contactPersonOptional = contactPersonRepository.findById(id);
        if (contactPersonOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No contactPerson with id: %s", id));
        }
        return contactPersonOptional.get();
    }
}
