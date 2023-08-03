package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EventService {

    Event create(Event event);

    List<Event> getAll();

    List<Event> getAllByInstitutionId(Long institutionId);
    Optional<Event> getById(Long id);

    Event update(Event event);

    void delete(Long id);
    void delete(Event event);

}
