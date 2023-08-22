package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EventControllerTests {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;
    @Autowired
    ObjectMapper objectMapper;
    EventDTO eventDTO;
    List<EventDTO> eventDTOs;

    @BeforeEach
    public void setup() {
        eventDTO = EventDTO.builder()
                .id(0L)
                .name("Dożmynki")
                .monthWhenOrganized(1)
                .description("Cool event")
                .build();

        eventDTOs = new ArrayList<>();
        eventDTOs.add(eventDTO);

    }
/*
    @DisplayName("JUnit test for getAll Event REST Api")
    @Test
    public void givenEventDTOsList_whenGetAll_thenReturnStatusOk() throws Exception {
        // given
        when(eventService.getAll()).thenReturn(eventDTOs);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/events"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(eventDTOs.size())));
    }

    @DisplayName("JUnit test for getById Institution REST Api (negative scenario)")
    @Test
    public void givenId_whenFindById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        given(institutionService.getById(anyLong())).willThrow(new ResourceNotFoundException("No institution with given id"));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", institutionDTO.getId())));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @DisplayName("JUnit test for getById Institution REST Api (positive scenario)")
    @Test
    public void givenId_whenFindById_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        given(institutionService.getById(anyLong())).willReturn(institutionDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", institutionDTO.getId())));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(0)));

    }

    @DisplayName("JUnit test for create InstitutionRestApi (negative scenario)")
    @Test
    public void givenInstitutionDTOObject_whenCreate_thenThrowsResourceAlreadyExistsException() throws Exception {
        //given - precondition or setup
        given(institutionService.create(institutionDTO)).willThrow(new ResourceAlreadyExistsException("Resource Already Exists"));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/institutions/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(institutionDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("JUnit test for create InstitutionRestApi (positive scenario)")
    @Test
    public void givenInstitutionDTOObject_whenCreate_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        given(institutionService.create(institutionDTO)).willReturn(institutionDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/institutions/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(institutionDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(0)));
    }


    @DisplayName("JUnit test for update InstitutionRestApi (negative scenario)")
    @Test
    public void givenInstitutionDTOObject_whenUpdate_thenThrowsResourceNotFoundException() throws Exception {
        //given - precondition or setup
        given(institutionService.update(institutionDTO)).willThrow(new ResourceNotFoundException("Resource Not Found"));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/institutions/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(institutionDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @DisplayName("JUnit test for update InstitutionRestApi (positive scenario)")
    @Test
    public void givenInstitutionDTOObject_whenUpdate_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        given(institutionService.update(institutionDTO)).willReturn(institutionDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/institutions/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(institutionDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(0)));
    }


    @DisplayName("JUnit test for delete InstitutionRestApi")
    @Test
    public void givenInstitutionId_whenDelete_thenInstitutionDeleted() throws Exception {
        //given - precondition or setup
        Long id = 0L;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/institutions/0"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(String.format("Institution with id: %s deleted successfully", id))));

    }

 */
}
