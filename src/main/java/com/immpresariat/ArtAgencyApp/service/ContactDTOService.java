package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;


import java.util.List;
import java.util.Optional;

public interface ContactDTOService {

    ContactDTO getByInstitutionID(Long institutionId);
    List<ContactDTO> getAll();

}
