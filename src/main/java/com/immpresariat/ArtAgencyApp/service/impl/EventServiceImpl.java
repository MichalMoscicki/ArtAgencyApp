package com.immpresariat.ArtAgencyApp.service.impl;


import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ContactRepository contactRepository;
    private final InputCleaner inputCleaner;
    private final DTOMapper dtoMapper;

    public EventServiceImpl(EventRepository eventRepository,
                            ContactRepository contactRepository,
                            InputCleaner inputCleaner,
                            DTOMapper dtoMapper) {
        this.eventRepository = eventRepository;
        this.contactRepository = contactRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public EventDTO create(EventDTO unsynchronizedEventDTO, Long contactId) {
        Contact contact = ensureContactExists(contactId);
        Event unsynchronizedEvent = dtoMapper.mapUnsyncInputDTOToEvent(unsynchronizedEventDTO);
        Event synchronizedEvent = eventRepository.save(inputCleaner.clean(unsynchronizedEvent));
        updateContact(contact, synchronizedEvent);

        return dtoMapper.mapEventToDTO(synchronizedEvent);

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
        event.setMonthWhenOrganized(eventDTO.getMonthWhenOrganized());
        Event eventDb = eventRepository.save(inputCleaner.clean(event));
        return dtoMapper.mapEventToDTO(eventDb);
    }

    @Override
    public void delete(Long eventId, Long contactId) {

        Contact contact = ensureContactExists(contactId);
        Event event = ensureEventExists(eventId);
        removeEventFromContact(contact, event);
        eventRepository.deleteById(eventId);
    }

    private void updateContact(Contact contact, Event synchronizedEvent) {
        List<Event> contactEvents;
        if (contact.getEvents() == null) {
            contactEvents = new ArrayList<>();
        } else {
            contactEvents = contact.getEvents();
        }
        contactEvents.add(synchronizedEvent);
        contact.setEvents(contactEvents);
        contactRepository.save(contact);
    }

    private Event ensureEventExists(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No event with id: %s", id));
        } else {
            return eventOptional.get();
        }
    }

    private Contact ensureContactExists(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isEmpty()) {
            throw new ResourceNotFoundException("No contact with id: " + id);
        }
        return contactOptional.get();
    }

    private void removeEventFromContact(Contact contact, Event event) {
        List<Event> contactEvents = contact.getEvents();
        contactEvents.remove(event);
        contact.setEvents(contactEvents);
        contactRepository.save(contact);
    }

}
