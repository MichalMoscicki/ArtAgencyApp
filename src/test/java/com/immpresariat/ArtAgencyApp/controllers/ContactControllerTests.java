package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ContactControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContactDTOService contactDTOService;
    @Autowired
    ObjectMapper objectMapper;

    private Institution institution;
    private List<ContactPersonDTO> contactPersonDTOList;
    private ContactPersonDTO contactPersonDTO1;
    private ContactPersonDTO contactPersonDTO2;
    private List<EventDTO> eventDTOList;
    private EventDTO eventDTO1;
    private EventDTO eventDTO2;
    private ContactDTO contactDTO;
    private List<ContactDTO> contactDTOS;


    @BeforeEach
    public void setup() {

        institution = Institution.builder()
                .id(0l)
                .category("Urząd miasta")
                .city("Wyszków")
                .name("Centrum Promocji Wyszkowa")
                .notes("Średnia organizacja")
                .alreadyCooperated(true)
                .build();

        contactPersonDTOList = new ArrayList<>();
        contactPersonDTO1 = ContactPersonDTO.builder()
                .id(0L)
                .role("dyrektor")
                .phone("+48111222333")
                .email("dyrektor@uww.pl")
                .lastName("Kowalski")
                .firstName("Jan")
                .build();
        contactPersonDTO2 = new ContactPersonDTO();
        contactPersonDTOList.add(contactPersonDTO1);
        contactPersonDTOList.add(contactPersonDTO2);

        eventDTOList = new ArrayList<>();
        eventDTO1 = EventDTO.builder()
                .id(0l)
                .name("Wianki")
                .description("Super duper melanż")
                .monthWhenOrganized(6)
                .build();
        eventDTO2 = new EventDTO();
        eventDTOList.add(eventDTO1);
        eventDTOList.add(eventDTO2);

        contactDTO = new ContactDTO();
        contactDTO.setInstitution(institution);
        contactDTO.setEventDTOS(eventDTOList);
        contactDTO.setContactPersonDTOS(contactPersonDTOList);

        contactDTOS = new ArrayList<>();
        contactDTOS.add(contactDTO);

    }

    @DisplayName("JUnit test for getById ContactDTO REST Api (positive scenario)")
    @Test
    public void givenInstitutionId_whenGetById_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        Long institutionId = 0l;
        given(contactDTOService.getByInstitutionID(institutionId)).willReturn(contactDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", institutionId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.institution.name", CoreMatchers.is(institution.getName())));

    }

    //TODO - jak zrobię global exceptionHandling, to wtedy ten test powinien przejść.
    /*
    @DisplayName("JUnit test for getById ContactDTO REST Api (positive scenario)")
    @Test
    public void givenInstitutionId_whenGetById_thenThrowError() throws Exception {
        //given - precondition or setup
        Long institutionId = 10l;
        given(contactDTOService.getByInstitutionID(institutionId)).willThrow(ResourceNotFoundException.class);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", institutionId)));
        System.out.println(response);

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

     */


    @DisplayName("JUnit test for JsonProperty check")
    @Test
    public void givenContactDTOObject_whenGet_thenReturnJsonWihProperFieldNames() throws Exception {
        Long institutionId = 0l;
        given(contactDTOService.getByInstitutionID(institutionId)).willReturn(contactDTO);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", institutionId)));

        System.out.println(response);
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactPeople", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.events", CoreMatchers.notNullValue()));

    }


    @DisplayName("JUnit test for getAll ContactDTO REST Api")
    @Test
    public void givenListOfContactDTO_whenGetAll_thenReturnContactDTOsList() throws Exception {
        //given - precondition or setup
        given(contactDTOService.getAll()).willReturn(contactDTOS);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());

    }

}
