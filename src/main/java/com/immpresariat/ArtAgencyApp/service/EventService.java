package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    EventDTO create(EventDTO unsynchronizedEventDTO, Long institutionId);

    List<EventDTO> getAll();

    List<EventDTO> getAllByInstitutionId(Long institutionId);

    EventDTO getById(Long id);

    EventDTO update(EventDTO eventDTO);

    void delete(Long id);
    void delete(Event event);

}
