package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.Institution;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface InstitutionService {

    Institution create(Institution institution);

    List<Institution> getAll();

    Optional<Institution> getById(Long id);

    Institution update(Long id, Institution updatedInstitution);

    void delete(Long id);

}
