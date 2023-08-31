package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;


import java.util.List;

public interface ContactService {

    ContactDTO getById(Long Id);
    List<ContactDTO> getAll();
    ContactDTO create();
    void deleteWithAssociatedData(Long id);
}
