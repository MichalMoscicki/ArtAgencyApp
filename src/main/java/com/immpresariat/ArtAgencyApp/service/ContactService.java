package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;

import java.util.List;

public interface ContactService {

    ContactDTO getById(Long Id);
    PageResponse<ContactDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    ContactDTO create(ContactDTO unsyncContactDTO);
    ContactDTO update(ContactDTO contactDTO);
    void deleteWithAssociatedData(Long id);
    //todo czy to nie powinien być np TreeSet? - brak powtórek plus zachowana kolejność?
    List<Contact> export();

}
