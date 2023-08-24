package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final InstitutionRepository institutionRepository;
    private final InputCleaner inputCleaner;
    private final DTOMapper dtoMapper;

    public EventServiceImpl(EventRepository eventRepository, InstitutionRepository institutionRepository, InputCleaner inputCleaner, DTOMapper dtoMapper) {
        this.eventRepository = eventRepository;
        this.institutionRepository = institutionRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public EventDTO create(EventDTO unsynchronizedEventDTO, Long institutionId) {

        Institution institution = ensureInstitutionExists(institutionId);
        ensureEventNotExists(unsynchronizedEventDTO, institution);

        Event unsynchronizedEvent = dtoMapper.mapUnsyncInputDTOToEvent(unsynchronizedEventDTO);
        Event synchronizedEvent = eventRepository.save(inputCleaner.clean(unsynchronizedEvent));
        return dtoMapper.mapEventToDTO(synchronizedEvent);

    }

    @Override
    public List<EventDTO> getAll() {
        List<Event> event = eventRepository.findAll();
        return event.stream().map(dtoMapper::mapEventToDTO).collect(Collectors.toList());
    }

    @Override
    public EventDTO getById(Long id) {
        Event event = ensureEventExists(id);
        return dtoMapper.mapEventToDTO(event);
    }

    @Override
    public EventDTO update(EventDTO eventDTO) {
        Event event = ensureEventExists(eventDTO.getId());
       event.setName(eventDTO.getName());
       event.setDescription(eventDTO.getDescription());
       event.setMonthWhenOrganized(event.getMonthWhenOrganized());
        Event eventDb = eventRepository.save(inputCleaner.clean(event));
        return dtoMapper.mapEventToDTO(eventDb);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }


    private Institution ensureInstitutionExists(Long institutionId) {
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", institutionId));
        }
        return institutionOptional.get();
    }

    private Event ensureEventExists(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No event with id: %s", id));
        }
        return eventOptional.get();
    }

    private void ensureEventNotExists(EventDTO unsynchronizedEventDTO, Institution institution) {
//        Optional<Event> eventOptional = eventRepository.
//                findEventByNameAndInstitution(unsynchronizedEventDTO.getName(), institution);
//        if (eventOptional.isPresent()) {
//            throw new ResourceAlreadyExistsException(String.format("Institution %s already contains event: %s", institution, unsynchronizedEventDTO.getName()));
//        }
    }

}
