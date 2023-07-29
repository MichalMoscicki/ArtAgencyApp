package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

@Service
public class ContactDTOServiceImpl implements ContactDTOService {

    DTOMapper dtoMapper;

    public ContactDTOServiceImpl(DTOMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ContactDTO getByInstitutionID(Long institutionId) {
        return dtoMapper.mapToContactDTO(institutionId);
    }


}
