package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;

public interface ContactService {

    ContactDTO getById(Long Id);
    PageResponse<ContactDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    ContactDTO create(ContactDTO unsyncContactDTO);
    ContactDTO update(ContactDTO contactDTO);
    void deleteWithAssociatedData(Long id);
}
