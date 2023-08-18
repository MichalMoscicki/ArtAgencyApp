package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactDTOServiceImpl implements ContactDTOService {

    private final DTOMapper dtoMapper;
    private final InstitutionService institutionService;

    public ContactDTOServiceImpl(DTOMapper dtoMapper, InstitutionService institutionService) {
        this.dtoMapper = dtoMapper;
        this.institutionService = institutionService;
    }

    @Override
    public List<ContactDTO> getAll() {

        List<Institution> institutions = institutionService.getAll();
        List<ContactDTO> contactDTOS = new ArrayList<>();
        for (Institution institution : institutions) {
            contactDTOS.add(dtoMapper.mapToContactDTO(institution.getId()));
        }

        return contactDTOS;
    }

    @Override
    public ContactDTO getByInstitutionID(Long institutionId) {
        Optional<Institution> institutionOptional = institutionService.getById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", institutionId));
        }
        return dtoMapper.mapToContactDTO(institutionId);
    }

}
