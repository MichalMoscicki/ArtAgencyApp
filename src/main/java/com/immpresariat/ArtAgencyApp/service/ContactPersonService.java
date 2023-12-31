package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;

import java.util.List;
import java.util.Optional;

public interface ContactPersonService {

    ContactPersonDTO create(ContactPersonDTO unsyncContactPersonDTO, Long contactId);

    List<ContactPersonDTO> getAll();

    ContactPersonDTO getById(Long id);

    ContactPersonDTO update(ContactPersonDTO updatedContactPersonDTO, Long contactId);

    void delete(Long contactPersonId, Long contactId);
}
