package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactDTOServiceImpl implements ContactDTOService {

    private final DTOMapper dtoMapper;
    private final InstitutionService institutionService;
    private final EventService eventService;
    private final ContactPersonService contactPersonService;

    public ContactDTOServiceImpl(DTOMapper dtoMapper,
                                 InstitutionService institutionService,
                                 EventService eventService,
                                 ContactPersonService contactPersonService) {
        this.dtoMapper = dtoMapper;
        this.institutionService = institutionService;
        this.eventService = eventService;
        this.contactPersonService = contactPersonService;
    }

    @Override
    public List<ContactDTO> getAll() {

        List<Institution> institutions = institutionService.getAll();
        List<ContactDTO> contactDTOS = new ArrayList<>();
        for (Institution institution : institutions) {
            contactDTOS.add(dtoMapper.mapToContactDTO(institution.getId()));
        }

        return contactDTOS;
    }

    @Override
    public ContactDTO getByInstitutionID(Long institutionId) {
        Optional<Institution> institutionOptional = institutionService.getById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", institutionId));
        }
        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public ContactDTO create(ContactDTO contactDTO) {

        Institution institutionDB = institutionService.create(contactDTO.getInstitution());
        Long institutionId = institutionDB.getId();
        createEvents(contactDTO, institutionId);
        createContactPeople(contactDTO, institutionId);

        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public ContactDTO update(ContactDTO updatedContactDTO) {
        Long institutionId = updatedContactDTO.getInstitution().getId();
        institutionService.update(institutionId, updatedContactDTO.getInstitution());
        updateEvents(updatedContactDTO, institutionId);
        updateContactPeople(updatedContactDTO, institutionId);
        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public void deleteById(Long id) {
        institutionService.delete(id);

    }

    private void createContactPeople(ContactDTO contactDTO, Long institutionId) {
        List<ContactPersonDTO> contactPersonDTOS = contactDTO.getContactPersonDTOS();
        for (ContactPersonDTO contactPersonDTO : contactPersonDTOS) {
            contactPersonService.create(dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId));
        }
    }

    private void createEvents(ContactDTO contactDTO, Long institutionId) {
        List<EventDTO> eventDTOS = contactDTO.getEventDTOS();
        for (EventDTO eventDTO : eventDTOS) {
            eventService.create(dtoMapper.mapDTOtoEvent(eventDTO, institutionId));
        }
    }

    private void updateContactPeople(ContactDTO updatedContactDTO, Long institutionId) {
        List<ContactPersonDTO> contactPersonDTOS = updatedContactDTO.getContactPersonDTOS();
        for (ContactPersonDTO contactPersonDTO : contactPersonDTOS) {
            contactPersonService.update(dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId));
        }
    }

    private void updateEvents(ContactDTO updatedContactDTO, Long institutionId) {
        List<EventDTO> eventDTOS = updatedContactDTO.getEventDTOS();
        for (EventDTO eventDTO : eventDTOS) {
            eventService.update(dtoMapper.mapDTOtoEvent(eventDTO, institutionId));
        }
    }

}
