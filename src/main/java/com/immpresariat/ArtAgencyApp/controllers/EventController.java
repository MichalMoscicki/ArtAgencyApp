package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/api/v1/contacts/{contactId}/events")
    public ResponseEntity<EventDTO> create(@PathVariable Long contactId,
                                           @RequestBody EventDTO eventDTO){
        return new ResponseEntity<>(eventService.create(eventDTO, contactId), HttpStatus.CREATED);

    }

    @GetMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long eventId){
        return new ResponseEntity<>(eventService.getById(eventId), HttpStatus.OK);
    }

    //TODO event service zrwaca null, muszę ogarnąć czemu.
    @PutMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventDTO> update(@RequestBody EventDTO eventDTO){
        return new ResponseEntity<>(eventService.update(eventDTO), HttpStatus.OK);
    }


    @DeleteMapping("/api/v1/events/{eventId}")
    public ResponseEntity<String> delete(@PathVariable Long eventId){
        eventService.delete(eventId);
        return new ResponseEntity<>("   Successfully deleted event with id: " + eventId, HttpStatus.OK);
    }





}
