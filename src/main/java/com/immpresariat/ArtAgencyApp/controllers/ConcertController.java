package com.immpresariat.ArtAgencyApp.controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.immpresariat.ArtAgencyApp.googleCalendar.CalendarEventCreator;
import com.immpresariat.ArtAgencyApp.googleCalendar.GoogleCalendarConnector;
import com.immpresariat.ArtAgencyApp.payload.*;
import com.immpresariat.ArtAgencyApp.service.ConcertService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/v1/concerts")
public class ConcertController {

    private final ConcertService concertService;
    private final CalendarEventCreator eventCreator;

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Calendar service =
            new Calendar.Builder(HTTP_TRANSPORT, GoogleCalendarConnector.JSON_FACTORY, GoogleCalendarConnector.getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(GoogleCalendarConnector.APPLICATION_NAME)
                    .build();


    public ConcertController(ConcertService concertService, CalendarEventCreator eventCreator) throws GeneralSecurityException, IOException {
        this.concertService = concertService;
        this.eventCreator = eventCreator;
    }

    @GetMapping("")
    public PageResponse<ConcertDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_START_DATE, required = false) String sortBy,
                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return concertService.getAll(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("")
    public ResponseEntity<ConcertDTO> create(@RequestBody ConcertDTO unsyncConcertDTO) {
        return new ResponseEntity<>(concertService.create(unsyncConcertDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConcertDTO> update(@RequestBody ConcertDTO concertDTO,
                                             @PathVariable Long id) {
        return new ResponseEntity<>(concertService.update(concertDTO, id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(concertService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        concertService.deleteById(id);
        return new ResponseEntity<>("Successfully deleted concert with id: " + id, HttpStatus.OK);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirm(@PathVariable Long id) throws IOException {
        com.google.api.services.calendar.model.Calendar cal = service.calendars().get("primary").execute();
        Event event = eventCreator.createEvent(id);
        return new ResponseEntity<>(String.format("Event created: %s\n", event.getHtmlLink()), HttpStatus.OK);
    }
}
