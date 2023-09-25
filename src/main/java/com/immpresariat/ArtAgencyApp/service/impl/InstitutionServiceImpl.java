package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    final private InstitutionRepository institutionRepository;
    final private ContactRepository contactRepository;
    final private InputCleaner inputCleaner;
    final private DTOMapper dtoMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository,
                                  ContactRepository contactRepository,
                                  InputCleaner inputCleaner,
                                  DTOMapper dtoMapper) {
        this.institutionRepository = institutionRepository;
        this.contactRepository = contactRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }


    @Override
    public InstitutionDTO create(InstitutionDTO unsynchronizedInstitutionDTO, Long contactId) {
        Contact contact = ensureContactExists(contactId);
        Institution unsynchronizedInstitution = dtoMapper.mapToEntity(unsynchronizedInstitutionDTO);
        Institution synchronizedInstitution = institutionRepository.save(inputCleaner.clean(unsynchronizedInstitution));
        updateContact(contact, synchronizedInstitution);
        return dtoMapper.mapToDTO(synchronizedInstitution);
    }

    @Override
    public InstitutionDTO getById(Long id) {
        Institution institution = ensureInstitutionExists(id);
        return dtoMapper.mapToDTO(institution);
    }

    @Override
    public InstitutionDTO update(InstitutionDTO updatedInstitutionDTO, Long contactId) {
        //todo ensure contact exists
        ensureInstitutionExists(updatedInstitutionDTO.getId());
        Institution updatedInstitution = dtoMapper.mapToEntity(updatedInstitutionDTO);
        Institution institutionDB = institutionRepository.save(inputCleaner.clean(updatedInstitution));
        updateContactUpdatedField(contactId);
        return dtoMapper.mapToDTO(institutionDB);

    }

    @Override
    public void delete(Long institutionId, Long contactId) {

        Contact contact = ensureContactExists(contactId);
        Institution institution = ensureInstitutionExists(institutionId);
        removeInstitutionFromContact(contact, institution);
        institutionRepository.deleteById(institutionId);

    }

    private void removeInstitutionFromContact(Contact contact, Institution institution) {
        List<Institution> contactInstitutions = contact.getInstitutions();
        contactInstitutions.remove(institution);
        contact.setInstitutions(contactInstitutions);
        contactRepository.save(contact);
    }

    private Contact ensureContactExists(Long contactId) {
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if (contactOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No contact with id: %s", contactId));
        }
        return contactOptional.get();
    }

    private Institution ensureInstitutionExists(Long id) {
        Optional<Institution> institutionOptional = institutionRepository.findById(id);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", id));
        }
        return institutionOptional.get();
    }

    private void updateContact(Contact contact, Institution synchronizedInstitution) {
        List<Institution> institutions;
        if (contact.getInstitutions() == null) {
            institutions = new ArrayList<>();
        } else {
            institutions = contact.getInstitutions();
        }
        institutions.add(synchronizedInstitution);
        contact.setInstitutions(institutions);
        contact.setUpdated(new Date());
        contactRepository.save(contact);
    }

    private void updateContactUpdatedField(long contactId) {
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if (contactOptional.isEmpty()) {
            throw new ResourceNotFoundException("No contact with id:" + contactId);
        }

        Contact contact = contactOptional.get();
        contact.setUpdated(new Date());
        contactRepository.save(contact);
    }


}
