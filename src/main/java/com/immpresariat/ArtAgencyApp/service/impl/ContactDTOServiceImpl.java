package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Event;
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

    DTOMapper dtoMapper;
    InstitutionService institutionService;
    EventService eventService;
    ContactPersonService contactPersonService;

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
        if(institutionOptional.isEmpty()){
           throw new ResourceNotFoundException(String.format("No institution with id: %s", institutionId));
        }
        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public ContactDTO create(ContactDTO contactDTO) {
        Long institutionId = contactDTO.getInstitution().getId();
        institutionService.create(contactDTO.getInstitution());

        List<EventDTO> eventDTOS = contactDTO.getEventDTOS();
        for (EventDTO eventDTO: eventDTOS) {
           eventService.create(dtoMapper.mapDTOtoEvent(eventDTO, institutionId));
        }

        List<ContactPersonDTO> contactPersonDTOS = contactDTO.getContactPersonDTOS();
        for (ContactPersonDTO contactPersonDTO: contactPersonDTOS) {
            contactPersonService.create(dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId));
        }

        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public ContactDTO update(ContactDTO updatedContactDTO) {
        Long institutionId = updatedContactDTO.getInstitution().getId();

        institutionService.update(institutionId, updatedContactDTO.getInstitution());

        List<EventDTO> eventDTOS = updatedContactDTO.getEventDTOS();
        for (EventDTO eventDTO: eventDTOS) {
            eventService.update(dtoMapper.mapDTOtoEvent(eventDTO, institutionId));
        }

        List<ContactPersonDTO> contactPersonDTOS = updatedContactDTO.getContactPersonDTOS();
        for (ContactPersonDTO contactPersonDTO: contactPersonDTOS) {
            contactPersonService.update(contactPersonDTO.getId(), dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId));
        }


        return dtoMapper.mapToContactDTO(institutionId);
    }

    @Override
    public void deleteById(Long id) {
        institutionService.delete(id);

    }
}
