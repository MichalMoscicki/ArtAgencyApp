package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContactController.class)
public class ContactControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContactService contactService;
    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("JUnit test for create Contact REST Api")
    @Test
    public void whenCreate_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        when(contactService.create()).thenReturn(new ContactDTO());

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("JUnit test for getAll Contact REST Api")
    @Test
    public void whenGetAll_thenStatusOK() throws Exception {
        //given - precondition or setup
        when(contactService.getAll()).thenReturn(new ArrayList<>());

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for getById Contact REST Api (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        when(contactService.getById(id)).thenThrow(new ResourceNotFoundException("No contact with given id"));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts/" + id));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @DisplayName("JUnit test for getById Contact REST Api (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        when(contactService.getById(id)).thenReturn(new ContactDTO());

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts/" + id));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());

    }

}
