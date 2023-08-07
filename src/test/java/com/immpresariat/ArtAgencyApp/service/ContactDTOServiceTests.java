package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.impl.ContactDTOServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ContactDTOServiceTests {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    InstitutionService institutionService;
    @Mock
    EventService eventService;
    @Mock
    ContactPersonService contactPersonService;

    @InjectMocks
    ContactDTOServiceImpl contactDTOService;

    Long institutionId;
    Institution institution1;
    Institution institution2;
    List<Institution> institutions;

    @BeforeEach
    public void setup() {
        institutionId = 0l;
        institutions = new ArrayList<>();
        institution1 = new Institution();
        institution1.setId(0L);
        institution2 = new Institution();
        institution2.setId(1L);
        institutions.add(institution1);
        institutions.add(institution2);
    }

    @DisplayName("JUnit test for ContactDTOService getAll method")
    @Test
    public void givenInstitutionsList_whenGetAll_thenReturnContactDTOList() {
        //given - precondition or setup
        given(institutionService.getAll()).willReturn(institutions);
        given(dtoMapper.mapToContactDTO(any(Long.class))).willReturn(new ContactDTO());


        //when - action or the behavior that we are going to test
        List<ContactDTO> contactDTOs = contactDTOService.getAll();

        //then - verify the output
        Assertions.assertEquals(institutions.size(), contactDTOs.size());

    }

    @DisplayName("JUnit test for ContactDTOService getByInstitutionID method (positive scenario)")
    @Test
    public void givenInstitutionId_whenGetByInstitutionID_thenReturnInstitutionObject() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution1));
        given(dtoMapper.mapToContactDTO(institutionId)).willReturn(new ContactDTO());

        //when - action or the behavior that we are going to test
        ContactDTO contactDTO = contactDTOService.getByInstitutionID(institutionId);

        //then - verify the output
        assertNotNull(contactDTO);
    }

    @DisplayName("JUnit test for ContactDTOService getByInstitutionID method (negative scenario)")
    @Test
    public void givenInstitutionId_whenGetByInstitutionID_thenThrowError() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            contactDTOService.getByInstitutionID(institutionId);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for create ContactDTO method")
    @Test
    public void givenContactDTOObject_whenCreate_thenReturnContactDTOObject() {
        //given - precondition or setup
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setInstitution(institution1);
        EventDTO eventDTO1 = new EventDTO();
        EventDTO eventDTO2 = new EventDTO();
        List<EventDTO> eventDTOS = new ArrayList<>();
        eventDTOS.add(eventDTO1);
        eventDTOS.add(eventDTO2);
        contactDTO.setEventDTOS(eventDTOS);

        ContactPersonDTO contactPersonDTO1 = new ContactPersonDTO();
        ContactPersonDTO contactPersonDTO2 = new ContactPersonDTO();
        List<ContactPersonDTO> contactPersonDTOS = new ArrayList<>();
        contactPersonDTOS.add(contactPersonDTO1);
        contactPersonDTOS.add(contactPersonDTO2);
        contactDTO.setContactPersonDTOS(contactPersonDTOS);

        given(institutionService.create(contactDTO.getInstitution()))
                .willReturn(new Institution());
        given(eventService.create(dtoMapper.mapDTOtoEvent(eventDTO1, institutionId)))
                .willReturn(new Event());
        given(contactPersonService.create(dtoMapper.mapDTOtoContactPerson(contactPersonDTO1, institutionId)))
                .willReturn(new ContactPerson());
        given(dtoMapper.mapToContactDTO(institutionId))
                .willReturn(new ContactDTO());

        //when - action or the behavior that we are going to test
        ContactDTO contactDTOFromDB = contactDTOService.create(contactDTO);

        //then - verify the output
        assertNotNull(contactDTOFromDB);
    }




    @DisplayName("JUnit test for update ContactDTO")
    @Test
    public void givenUpdatedContactDTOObject_whenUpdate_thenReturnContactDTO() {
        //given - precondition or setup

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setInstitution(institution1);
        EventDTO eventDTO1 = new EventDTO();
        EventDTO eventDTO2 = new EventDTO();
        List<EventDTO> eventDTOS = new ArrayList<>();
        eventDTOS.add(eventDTO1);
        eventDTOS.add(eventDTO2);
        contactDTO.setEventDTOS(eventDTOS);

        ContactPersonDTO contactPersonDTO1 = new ContactPersonDTO();
        ContactPersonDTO contactPersonDTO2 = new ContactPersonDTO();
        List<ContactPersonDTO> contactPersonDTOS = new ArrayList<>();
        contactPersonDTOS.add(contactPersonDTO1);
        contactPersonDTOS.add(contactPersonDTO2);
        contactDTO.setContactPersonDTOS(contactPersonDTOS);

        when(institutionService.update(anyLong(), any(Institution.class))).thenReturn(institution1);
        given(eventService.update(any(Event.class))).willReturn(new Event());
        given(dtoMapper.mapDTOtoEvent(any(EventDTO.class), anyLong())).willReturn(new Event());

        given(contactPersonService.update(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(dtoMapper.mapDTOtoContactPerson(any(ContactPersonDTO.class), anyLong())).willReturn(new ContactPerson());
        when(dtoMapper.mapToContactDTO(anyLong())).thenReturn(contactDTO);


        //when - action or the behavior that we are going to test
        ContactDTO result = contactDTOService.update(contactDTO);

        //then - verify the output
        assertEquals(contactDTO, result);
        verify(institutionService, times(1)).update(anyLong(), any(Institution.class));
        verify(eventService, times(eventDTOS.size())).update(any(Event.class));
        verify(contactPersonService, times(contactPersonDTOS.size())).update(any(ContactPerson.class));
        verify(dtoMapper, times(1)).mapToContactDTO(anyLong());

    }


    //piszę porządny kod testowy!
    //dwa testy - co jeżeli nie ma eventów albo contact persons? zarówno w creae jak i update
    //zrób refactor metody

}
