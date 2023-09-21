package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactResponse;


import java.util.List;

public interface ContactService {

    ContactDTO getById(Long Id);
    ContactResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    ContactDTO create(ContactDTO unsyncContactDTO);
    ContactDTO update(ContactDTO contactDTO);
    void deleteWithAssociatedData(Long id);
}
