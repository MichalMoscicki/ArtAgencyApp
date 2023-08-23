package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;

import java.util.List;
import java.util.Optional;

public interface ContactPersonService {

    ContactPersonDTO create(ContactPersonDTO unsyncContactPersonDTO, Long institutionId);

    List<ContactPersonDTO> getAll();

    List<ContactPersonDTO> getAllByInstitutionId(Long institutionId);

    ContactPersonDTO getById(Long id);

    ContactPersonDTO update(ContactPersonDTO updatedContactPersonDTO);

    void delete(Long id);
    void delete(ContactPerson contactPerson);
}
