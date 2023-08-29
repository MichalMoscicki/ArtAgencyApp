package com.immpresariat.ArtAgencyApp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InstitutionController.class)
public class InstitutionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InstitutionService institutionService;
    @Autowired
    ObjectMapper objectMapper;

    InstitutionDTO institutionDTO;

    @BeforeEach
    public void setup() {
        institutionDTO = InstitutionDTO.builder()
                .id(0L)
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();

    }

    @DisplayName("JUnit test for create Institution REST Api")
    @Test
    public void givenInstitutionDTOObject_whenCreate_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Long contactId = 0l;

        given(institutionService.create(institutionDTO, contactId)).willReturn(institutionDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/contacts/0/institutions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(institutionDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(0)));
    }

    @DisplayName("JUnit test for getById Institution REST Api (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        String message = "No institution with given id";
        given(institutionService.getById(id)).willThrow(new ResourceNotFoundException(message));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", id)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));

    }

    @DisplayName("JUnit test for getById Institution REST Api (positive scenario)")
    @Test
    public void givenId_whenFindById_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        given(institutionService.getById(id)).willReturn(institutionDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", id)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
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


    @DisplayName("JUnit test for delete Institution REST Api")
    @Test
    public void givenInstitutionId_whenDelete_thenInstitutionDeleted() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        String message = "Successfully deleted institution with id: " + id;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/institutions/0"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

    }

}
