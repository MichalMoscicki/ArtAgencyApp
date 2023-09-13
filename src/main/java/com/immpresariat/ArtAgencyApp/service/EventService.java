package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    EventDTO create(EventDTO unsynchronizedEventDTO, Long contactId);

    EventDTO getById(Long id);

    EventDTO update(EventDTO eventDTO, Long contactId);

    void delete(Long eventId, Long contactId);

}
