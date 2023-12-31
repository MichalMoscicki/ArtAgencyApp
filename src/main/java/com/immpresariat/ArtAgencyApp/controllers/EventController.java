package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/contacts/{contactId}/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    public ResponseEntity<EventDTO> create(@PathVariable Long contactId,
                                           @RequestBody EventDTO eventDTO){
        return new ResponseEntity<>(eventService.create(eventDTO, contactId), HttpStatus.CREATED);
    }

    @GetMapping("{eventId}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.getById(eventId), HttpStatus.OK);
    }

    @PutMapping("{eventId}")
    public ResponseEntity<EventDTO> update(@PathVariable Long contactId,
                                           @RequestBody EventDTO eventDTO){
        return new ResponseEntity<>(eventService.update(eventDTO, contactId), HttpStatus.OK);
    }

    @DeleteMapping("{eventId}")
    public ResponseEntity<String> delete(@PathVariable Long eventId,
                                         @PathVariable Long contactId){
        eventService.delete(eventId, contactId);
        return new ResponseEntity<>("   Successfully deleted event with id: " + eventId, HttpStatus.OK);
    }

}
