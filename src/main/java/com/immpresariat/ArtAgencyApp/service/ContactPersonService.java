package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;

import java.util.List;
import java.util.Optional;

public interface ContactPersonService {
    ContactPerson create(ContactPerson contactPerson);

    List<ContactPerson> getAll();

    List<ContactPerson> getAllByInstitutionId(Long institutionId);
    Optional<ContactPerson> getById(Long id);

    ContactPerson update(Long id, ContactPerson updatedContactPerson);

    void delete(Long id);
    void delete(ContactPerson contactPerson);
}
