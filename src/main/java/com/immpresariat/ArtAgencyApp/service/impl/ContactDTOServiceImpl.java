package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactDTOServiceImpl implements ContactDTOService {

    DTOMapper dtoMapper;

    public ContactDTOServiceImpl(DTOMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Optional<ContactDTO> getByInstitutionID(Long institutionId) {
        return Optional.empty();
    }

    @Override
    public ContactDTO create(ContactDTO contactDTO) {
        return null;
    }

    @Override
    public ContactDTO update(ContactDTO updatedContactDTO) {
        return null;
    }

    @Override
    public List<ContactDTO> getAll() {
        return null;
    }

    @Override
    public void delete(ContactDTO contactDTO) {

    }
}
