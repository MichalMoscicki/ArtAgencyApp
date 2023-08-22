package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EventServiceImpl implements EventService {

   private final EventRepository eventRepository;
   private final InputCleaner inputCleaner;
   private final DTOMapper dtoMapper;

    public EventServiceImpl(EventRepository eventRepository, InputCleaner inputCleaner, DTOMapper dtoMapper) {
        this.eventRepository = eventRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

//    @Override
//    public Event create(Event event) {
//
//        Optional<Event> eventOptional = eventRepository.
//                findEventByNameAndInstitution(event.getName(), event.getInstitution());
//
//        if(eventOptional.isEmpty()){
//            return eventRepository.save(inputCleaner.clean(event));
//        } else {
//            throw new ResourceAlreadyExistsException(String.format("Institution %s already contains event: %s", event.getInstitution().getName(), event.getName()));
//        }
//
//    }
//
//    @Override
//    public List<Event> getAll() {
//        return eventRepository.findAll();
//    }
//
//    @Override
//    public List<Event> getAllByInstitutionId(Long institutionId) {
//        return eventRepository.findAllByInstitutionId(institutionId);
//    }
//
//    @Override
//    public Optional<Event> getById(Long id) {
//        return eventRepository.findById(id);
//    }
//
//    @Override
//    public Event update(Event event) {
//
//        Optional<Event> eventOptional = eventRepository.findById(event.getId());
//        if(eventOptional.isEmpty()){
//            throw new ResourceNotFoundException(String.format("No event with id: %s", event.getId()));
//        }
//        return eventRepository.save(inputCleaner.clean(event));
//    }
//
//    @Override
//    public void delete(Long id) {
//        eventRepository.deleteById(id);
//    }
//
//    @Override
//    public void delete(Event event) {
//        eventRepository.delete(event);
//    }

}
