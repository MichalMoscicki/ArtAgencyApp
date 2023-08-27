package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContactPersonController.class)
public class ContactPersonControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContactPersonService contactPersonService;
    @Autowired
    ObjectMapper objectMapper;

    ContactPersonDTO contactPersonDTO;
    List<ContactPersonDTO> contactPeopleDTOList;

    @BeforeEach
    public void setup() {
        contactPersonDTO = ContactPersonDTO.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
                .phone("+48791272394")
                .email("dyr@dyr.pl")
                .build();

        contactPeopleDTOList = new ArrayList<>();
        contactPeopleDTOList.add(contactPersonDTO);
    }


    @DisplayName("JUnit test for create ContactPerson REST Api")
    @Test
    public void givenContactPersonDTO_whenCreate_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long contactId = 0L;

        when(contactPersonService.create(contactPersonDTO, contactId)).thenReturn(contactPersonDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/contact-people", 0))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactPersonDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated());

    }

    @DisplayName("JUnit test for getById ContactPerson REST Api (negative scenario)")
    @Test
    public void givenId_whenGetById_thenReturnResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long contactPersonId = 0L;
        String message = "No contactPerson with id: " + contactPersonId;

        when(contactPersonService.getById(contactPersonId)).thenThrow(new ResourceNotFoundException(message));

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contact-people/%s", contactPersonId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @DisplayName("JUnit test for getById ContactPerson REST Api (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long eventId = 0L;

        when(contactPersonService.getById(eventId)).thenReturn(contactPersonDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contact-people/%s", eventId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("JUnit test for update ContactPerson REST Api (negative scenario)")
    @Test
    public void givenId_whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long contactPersonId = 0L;
        String message = "No contactPerson with id: " + contactPersonId;
        when(contactPersonService.update(contactPersonDTO)).thenThrow(new ResourceNotFoundException(message));


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/contact-people/%s", contactPersonId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactPersonDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));


    }

    @DisplayName("JUnit test for update ContactPerson REST Api (positive scenario)")
    @Test
    public void givenEventDTO_whenUpdate_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Long contactPersonId = 0L;
        when(contactPersonService.update(contactPersonDTO)).thenReturn(contactPersonDTO);


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/contact-people/%s", contactPersonId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactPersonDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(contactPersonId)));

    }

    @DisplayName("JUnit test for delete ContactPerson REST Api")
    @Test
    public void givenId_whenDelete_thenReturnMessage() throws Exception {
        //given - precondition or setup
        Long contactPersonId = 0L;
        String message = "Successfully deleted contactPerson with id: 0";

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contact-people/%s", contactPersonId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

        verify(contactPersonService, times(1)).delete(anyLong());


    }


}
