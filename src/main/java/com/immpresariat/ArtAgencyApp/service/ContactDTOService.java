package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface ContactDTOService {

    ContactDTO getByInstitutionID(Long institutionId);

    ContactDTO create(ContactDTO contactDTO);

    ContactDTO update(ContactDTO updatedContactDTO);
    List<ContactDTO> getAll();

    void deleteById(Long id);



}
