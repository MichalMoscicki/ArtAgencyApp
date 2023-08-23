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

import java.util.ArrayList;
import java.util.List;

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

    @DisplayName("JUnit test for")
    @Test
    public void given_when_then() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test

        //then - verify the output

    }
}
