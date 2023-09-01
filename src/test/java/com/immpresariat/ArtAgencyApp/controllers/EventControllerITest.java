package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EventControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    EventService eventService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        cleanDB();
    }

    @Test
    public void whenCreate_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        EventDTO unsyncEventDTO = createSampleEventDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/events", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncEventDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }

    @Test
    public void whenCreate_thenEventAddedToContact() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        EventDTO unsyncEventDTO = createSampleEventDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/events", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncEventDTO)));

        //then - verify the output
        Contact updatedContact = contactRepository.findById(contactId).get();
        assertEquals(updatedContact.getEvents().size(), 1);

    }


    @Test
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Long eventId = 0L;
        String message = String.format("No event with id: %s", eventId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/events/%s", contactId, eventId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    public void whenGetById_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        EventDTO syncDTO = eventService.create(createSampleEventDTO(null), contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/events/%s", contactId, syncDTO.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(syncDTO.getName())));
    }


    @Test
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();

        Long institutionId = 0L;
        EventDTO notExistingEvent = createSampleEventDTO(institutionId);

        String message = String.format("No event with id: %s", institutionId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/contacts/%s/events/%s", contactId, institutionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notExistingEvent)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    public void testUpdateEvent() throws Exception {
        // Given
        Contact contact = createSampleContact();
        Event unsyncEvent = createSampleEvent("Event", "Niezbyt udany", 6);
        Event syncEvent = eventRepository.save(unsyncEvent);
        contact.setEvents(Collections.singletonList(syncEvent));
        contactRepository.save(contact);

        Long contactId = contact.getId();
        Long institutionId = syncEvent.getId();
        EventDTO updatedIEventDTO = createSampleEventDTO(institutionId);

        // When
        ResultActions response = mockMvc.perform(
                put(String.format("/api/v1/contacts/%s/events/%s", contactId, institutionId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedIEventDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(institutionId.intValue())))
                .andExpect(jsonPath("$.name", is(updatedIEventDTO.getName())))
                .andExpect(jsonPath("$.description", is(updatedIEventDTO.getDescription())))
                .andExpect(jsonPath("$.monthWhenOrganized", is(updatedIEventDTO.getMonthWhenOrganized())));
    }


    @Test
    public void whenDelete_thenEventDeleted() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();

        EventDTO syncDTO = eventService.create(createSampleEventDTO(null), contactId);
        Long eventId = syncDTO.getId();

        String message = "Successfully deleted event with id: " + eventId;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/events/%s", contactId, eventId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

    }

    @Test
    public void givenNoContact_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long contactId = 0L;
        Long eventId = 1L;
        String message = String.format("No contact with id: %s", contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/events/%s", contactId, eventId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenNoEvent_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Long eventId = 1L;
        String message = String.format("No event with id: %s", eventId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/events/%s", contactId, eventId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }



    private void cleanDB() {
        List<Contact> contacts = contactRepository.findAll();
        for (Contact contact : contacts) {
            contact.setEvents(new ArrayList<>());
            contactRepository.save(contact);
        }
        eventRepository.deleteAll();
    }

    private Contact createSampleContact() {
        return contactRepository.save(new Contact());
    }

    private EventDTO createSampleEventDTO(Long eventId) {
        return EventDTO.builder()
                .id(eventId)
                .name("Melanż")
                .description("Bardzo dobry")
                .monthWhenOrganized(6)
                .build();
    }

    private Event createSampleEvent(String name, String description, int monthWhenOrganised) {
        return Event.builder()
                .name(name)
                .description(description)
                .monthWhenOrganized(monthWhenOrganised)
                .build();
    }
}
