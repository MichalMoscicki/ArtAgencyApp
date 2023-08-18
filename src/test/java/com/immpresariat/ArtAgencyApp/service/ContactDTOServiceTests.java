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


    ContactDTO contactDTO;
    Institution institution1;
    Institution institution2;
    List<Institution> institutions;
    Long institutionId;
    List<ContactPersonDTO> contactPersonDTOS;
    ContactPersonDTO contactPersonDTO1;
    ContactPersonDTO contactPersonDTO2;
    List<EventDTO> eventDTOS;
    EventDTO eventDTO1;
    EventDTO eventDTO2;

    @BeforeEach
    public void setup() {
        institutionId = 0L;
        institution1 = Institution.builder()
                .id(institutionId)
                .category("Urząd miasta")
                .city("Wyszków")
                .name("Centrum Promocji Wyszkowa")
                .notes("Średnia organizacja")
                .alreadyCooperated(true)
                .build();


        institutions = new ArrayList<>();
        institution2 = new Institution();
        institution2.setId(1L);
        institutions.add(institution1);
        institutions.add(institution2);

        contactPersonDTOS = new ArrayList<>();
        contactPersonDTO1 = ContactPersonDTO.builder()
                .id(0L)
                .role("dyrektor")
                .phone("+48111222333")
                .email("dyrektor@uww.pl")
                .lastName("Kowalski")
                .firstName("Jan")
                .build();
        contactPersonDTO2 = new ContactPersonDTO();
        contactPersonDTOS.add(contactPersonDTO1);
        contactPersonDTOS.add(contactPersonDTO2);

        eventDTOS = new ArrayList<>();
        eventDTO1 = EventDTO.builder()
                .id(0l)
                .name("Wianki")
                .description("Super duper melanż")
                .monthWhenOrganized(6)
                .build();
        eventDTO2 = new EventDTO();
        eventDTOS.add(eventDTO1);
        eventDTOS.add(eventDTO2);

        contactDTO = new ContactDTO();
        contactDTO.setInstitution(institution1);
        contactDTO.setEventDTOS(eventDTOS);
        contactDTO.setContactPersonDTOS(contactPersonDTOS);

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
        given(institutionService.create(contactDTO.getInstitution()))
                .willReturn(institution1);
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

    @DisplayName("JUnit test for update ContactDTO with no EventDTOS")
    @Test
    public void givenUpdatedContactDTOObjectWithNoEventDTOS_whenUpdate_thenReturnContactDTO() {
        //given - precondition or setup
        contactDTO.setEventDTOS(new ArrayList<>());
        when(institutionService.update(anyLong(), any(Institution.class))).thenReturn(institution1);

        given(contactPersonService.update(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(dtoMapper.mapDTOtoContactPerson(any(ContactPersonDTO.class), anyLong())).willReturn(new ContactPerson());
        when(dtoMapper.mapToContactDTO(anyLong())).thenReturn(contactDTO);

        //when - action or the behavior that we are going to test
        ContactDTO result = contactDTOService.update(contactDTO);

        //then - verify the output
        assertEquals(contactDTO, result);
        verify(institutionService, times(1)).update(anyLong(), any(Institution.class));
        verify(contactPersonService, times(contactPersonDTOS.size())).update(any(ContactPerson.class));
        verify(dtoMapper, times(1)).mapToContactDTO(anyLong());

    }

}
