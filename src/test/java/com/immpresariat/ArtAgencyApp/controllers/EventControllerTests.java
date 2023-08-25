package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class)
public class EventControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    EventDTO eventDTO;
    List<EventDTO> eventDTOList;

    @BeforeEach
    public void setup(){
        eventDTO = EventDTO.builder()
                .id(0L)
                .name("Dożmynki")
                .monthWhenOrganized(1)
                .description("")
                .build();

        eventDTOList = new ArrayList<>();
        eventDTOList.add(eventDTO);

    }

    @DisplayName("JUnit test for create Event REST Api")
    @Test
    public void whenCreate_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        EventDTO unsyncEventDTO = eventDTO;
        unsyncEventDTO.setId(null);

       // when(eventService.create(unsyncEventDTO)).thenReturn(eventDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    //find by id
    //delete
    //update



}
