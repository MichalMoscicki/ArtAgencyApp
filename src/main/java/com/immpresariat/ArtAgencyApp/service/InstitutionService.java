package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import org.springframework.stereotype.Service;

@Service
public interface InstitutionService {

    InstitutionDTO create(InstitutionDTO institutionDTO, Long contactId);

    InstitutionDTO update(InstitutionDTO updatedInstitutionDTO);

    InstitutionDTO getById(Long id);

    void delete(Long institutionId, Long contactId);


}
