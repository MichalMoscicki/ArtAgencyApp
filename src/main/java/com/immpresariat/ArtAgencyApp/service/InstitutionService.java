package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InstitutionService {

    InstitutionDTO create(InstitutionDTO institutionDTO);

    List<InstitutionDTO> getAll();

    InstitutionDTO update(InstitutionDTO updatedInstitutionDTO);

    InstitutionDTO getById(Long id);

    void deleteWithAssociatedData(Long id);


}
