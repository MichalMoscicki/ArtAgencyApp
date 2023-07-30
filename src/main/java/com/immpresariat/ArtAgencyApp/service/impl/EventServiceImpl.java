package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event create(Event event) {

        Optional<Event> eventOptional = eventRepository.
                findEventByNameAndInstitution(event.getName(), event.getInstitution());

        if(eventOptional.isEmpty()){
            return eventRepository.save(event);
        } else {
            throw new ResourceAlreadyExistsException(String.format("Institution %s already contains event: %s", event.getInstitution().getName(), event.getName()));
        }

    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getAllByInstitutionId(Long institutionId) {
        return eventRepository.findAllByInstitutionId(institutionId);
    }

    @Override
    public Optional<Event> getById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event update(Long id, Event event) {

        //sprawdzenie, czy juz jest
        return null;
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }
}
