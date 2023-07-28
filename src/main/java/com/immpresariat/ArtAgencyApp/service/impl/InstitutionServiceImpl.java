package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;

import java.util.List;
import java.util.Optional;

public class InstitutionServiceImpl implements InstitutionService {

    final private InstitutionRepository institutionRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
    public Institution create(Institution institution) {
        Optional<Institution> institutionOptional = institutionRepository.
                findInstitutionByNameAndCity(institution.getName(), institution.getCity());
        if(institutionOptional.isPresent()){
            throw new ResourceNotFoundException(String.format("Institution with given name: %s and city: %s already exists", institution.getName(), institution.getCity()));
        }

        return institutionRepository.save(institution);
    }

    @Override
    public List<Institution> getAll() {
        return null;
    }

    @Override
    public Optional<Institution> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Institution update(Long id, Institution updatedInstitution) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
