package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    final private InstitutionRepository institutionRepository;
    final private ContactPersonService contactPersonService;
    final private EventService eventService;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository,
                                  ContactPersonService contactPersonService,
                                  EventService eventService) {
        this.institutionRepository = institutionRepository;
        this.contactPersonService = contactPersonService;
        this.eventService = eventService;
    }

    @Override
    public Institution create(Institution institution) {
        Optional<Institution> institutionOptional = institutionRepository.
                findInstitutionByNameAndCity(institution.getName(), institution.getCity());
        if(institutionOptional.isPresent()){
            throw new ResourceAlreadyExistsException(String.format("Institution with given name: %s and city: %s already exists",
                    institution.getName(), institution.getCity()));
        }

        return institutionRepository.save(institution);
    }

    @Override
    public List<Institution> getAll() {
        return institutionRepository.findAll();
    }

    @Override
    public Optional<Institution> getById(Long id) {

        return institutionRepository.findById(id);
    }

    @Override
    public Institution update(Long id, Institution updatedInstitution) {
        Optional<Institution> institutionOptional = institutionRepository.findById(id);

        if(institutionOptional.isPresent()){
            return institutionRepository.save(updatedInstitution);
        } else {
            throw new ResourceAlreadyExistsException(String.format("No institution with id: %s", id));
        }
    }

    @Override
    public void delete(Long id) {

        List<ContactPerson> contactPeople = contactPersonService.getAllByInstitutionId(id);
        contactPeople.forEach(contactPersonService::delete);

        List<Event> events = eventService.getAllByInstitutionId(id);
        events.forEach(eventService::delete);

        institutionRepository.deleteById(id);

    }

}
