package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class)
public class EventControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;
    @Autowired
    ObjectMapper objectMapper;

    EventDTO eventDTO;
    List<EventDTO> eventDTOList;

    @BeforeEach
    public void setup() {
        eventDTO = EventDTO.builder()
                .id(0L)
                .name("Dożmynki")
                .monthWhenOrganized(1)
                .description("")
                .build();

        eventDTOList = new ArrayList<>();
        eventDTOList.add(eventDTO);

    }

    //todo a co jak nie ma takiego kontaktu? negatywny scenariusz wydaje się potrzebny

    @DisplayName("JUnit test for create Event REST Api")
    @Test
    public void whenCreate_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long contactId = 0L;
        EventDTO unsyncEventDTO = eventDTO;
        unsyncEventDTO.setId(null);

        when(eventService.create(unsyncEventDTO, contactId)).thenReturn(eventDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/events", 0))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("JUnit test for getById Event REST Api (negative scenario)")
    @Test
    public void givenId_whenGetById_thenReturnResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long eventId = 0L;
        String message = "No event with id: " + eventId;

        when(eventService.getById(eventId)).thenThrow(new ResourceNotFoundException(message));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/events/%s", eventId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @DisplayName("JUnit test for getById Event REST Api (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long eventId = 0L;

        when(eventService.getById(eventId)).thenReturn(eventDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/events/%s", eventId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("JUnit test for update Event REST Api (negative scenario)")
    @Test
    public void givenId_whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long eventId = 0L;
        String message = "No event with id: " + eventId;
        when(eventService.update(eventDTO)).thenThrow(new ResourceNotFoundException(message));


        //when - action or the behavior that we are going to test
      ResultActions response = mockMvc.perform(put(String.format("/api/v1/events/%s", eventId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));


    }

    @DisplayName("JUnit test for update Event REST Api (positive scenario)")
    @Test
    public void givenEventDTO_whenUpdate_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long eventId = 0L;
        when(eventService.update(eventDTO)).thenReturn(eventDTO);


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/events/%s", eventId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(eventId)));


    }


//    @DisplayName("JUnit test for delete Event REST Api")
//    @Test
//    public void givenId_whenDelete_thenReturnMessage() throws Exception {
//        //given - precondition or setup
//        Long eventId = 0L;
//        String message = "Successfully deleted event with id: 0";
//
//        //when - action or the behavior that we are going to test
//        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/events/%s", eventId)));
//
//        //then - verify the output
//        response.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", CoreMatchers.is(message)));
//
//        verify(eventService, times(1)).delete(anyLong());
//
//
//    }



}
