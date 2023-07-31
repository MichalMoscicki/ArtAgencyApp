package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import com.immpresariat.ArtAgencyApp.service.EventService;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DTOMapperTests {

    @Mock
    InstitutionService institutionService;
    @Mock
    EventService eventService;
    @Mock
    ContactPersonService contactPersonService;
    @InjectMocks
    DTOMapper dtoMapper;


    ContactDTO contactDTO;
    Long institutionId;
    Institution institution;
    ContactPerson personOne;
    ContactPerson personTwo;
    List<ContactPerson> contactPeople;
    List<ContactPersonDTO> contactPeopleDTO;
    Event eventOne;
    Event eventTwo;
    List<Event> events;
    List<EventDTO> eventDTOS;


    @BeforeEach
    public void setup() {
        institutionId = 1l;
        institution = Institution.builder()
                .id(institutionId)
                .name("DK Praga")
                .notes("Super miejsce")
                .alreadyCooperated(true)
                .city("Warszawa")
                .build();

        contactDTO = new ContactDTO();

        personOne = ContactPerson.builder()
                .id(1l)
                .email("test@test.pl")
                .phone("+48111222333")
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
                .institution(institution)
                .build();

        personTwo = new ContactPerson();

        contactPeople = new ArrayList<>();
        contactPeople.add(personOne);
        contactPeople.add(personTwo);

        contactPeopleDTO = new ArrayList<>();
        contactPeopleDTO.add(dtoMapper.mapContactPersonToDTO(personOne));
        contactPeopleDTO.add(dtoMapper.mapContactPersonToDTO(personTwo));
        contactDTO.setContactPersonDTOS(contactPeopleDTO);

        eventOne = Event.builder()
                .id(1l)
                .description("Dni ogórka")
                .monthWhenOrganized(8)
                .institution(institution)
                .name("Odpust na Stalowej")
                .build();
        eventTwo = new Event();

        events = new ArrayList<>();
        events.add(eventOne);
        events.add(eventTwo);

        eventDTOS = new ArrayList<>();
        eventDTOS.add(dtoMapper.mapEventToDTO(eventOne));
        eventDTOS.add(dtoMapper.mapEventToDTO(eventTwo));
        contactDTO.setEventDTOS(eventDTOS);

    }


    @DisplayName("JUnit test for Mapper mapToContactDTO method (positive scenario)")
    @Test
    public void givenInstitutionId_whenMapToContactDTO_thenContactDTOObject() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution));

        given(contactPersonService.getAllByInstitutionId(institutionId)).willReturn(contactPeople);

        given(eventService.getAllByInstitutionId(institutionId)).willReturn(events);

        //when - action or the behavior that we are going to test
        ContactDTO contactDTO = dtoMapper.mapToContactDTO(institutionId);

        //then - verify the output
        assertNotNull(contactDTO);
        assertEquals(contactPeople.size(), contactDTO.getContactPersonDTOS().size());
        assertEquals(events.size(), contactDTO.getEventDTOS().size());
        assertEquals(institution, contactDTO.getInstitution());

    }


    @DisplayName("JUnit test for Mapper mapToContactDTO method (negative scenario)")
    @Test
    public void givenInstitutionId_whenMapToContactDTO_thenThrowsError() {
        //given - precondition or setup
        Long institutionId = 1l;

        given(institutionService.getById(institutionId)).willReturn(Optional.empty());


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            dtoMapper.mapToContactDTO(institutionId);
        });

        //then - verify the output
    }


    @DisplayName("JUnit test for  Mapper  Mapper mapDTOToEvent method (positive scenario)")
    @Test
    public void givenEventDTO_whenMapDTOtoEvent_thenReturnEventObject() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution));
        EventDTO eventDTO = dtoMapper.mapEventToDTO(eventOne);

        //when - action or the behavior that we are going to test
        Event event = dtoMapper.mapDTOtoEvent(eventDTO, institutionId);

        //then - verify the output
        assertEquals(institutionId, event.getInstitution().getId());
        assertEquals(eventOne.getName(), event.getName());
        assertEquals(eventOne.getId(), event.getId());
        assertEquals(eventOne.getMonthWhenOrganized(), event.getMonthWhenOrganized());
        assertEquals(eventOne.getDescription(), event.getDescription());
        assertEquals(eventOne.getInstitution(), event.getInstitution());

    }

    @DisplayName("JUnit test for  Mapper  Mapper mapDTOToEvent method (negative scenario)")
    @Test
    public void givenEventDTOAndInvalidInstitutionId_whenMapDTOtoEvent_thenThrowsError() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.empty());
        EventDTO eventDTO = dtoMapper.mapEventToDTO(eventOne);


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            dtoMapper.mapDTOtoEvent(eventDTO, institutionId);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for  Mapper  Mapper mapDTOToContactPerson method (positive scenario)")
    @Test
    public void givenContactPersonDTO_whenMapDTOtoContactPerson_thenReturnEventObject() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution));
        ContactPersonDTO contactPersonDTO = dtoMapper.mapContactPersonToDTO(personOne);

        //when - action or the behavior that we are going to test
        ContactPerson contactPerson = dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId);

        //then - verify the output
        assertEquals(institution, contactPerson.getInstitution());
        assertEquals(personOne.getId(), contactPerson.getId());
        assertEquals(personOne.getFirstName(), contactPerson.getFirstName());
        assertEquals(personOne.getLastName(), contactPerson.getLastName());
        assertEquals(personOne.getRole(), contactPerson.getRole());
        assertEquals(personOne.getPhone(), contactPerson.getPhone());
        assertEquals(personOne.getEmail(), contactPerson.getEmail());
        assertEquals(personOne.getInstitution(), contactPerson.getInstitution());

    }

    @DisplayName("JUnit test for  Mapper  Mapper mapDTOToContactPerson method (negative scenario)")
    @Test
    public void givenContactPersonDTOAndInvalidInstitutionId_whenMapDTOtoContactPerson_thenThrowsError() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.empty());
        ContactPersonDTO contactPersonDTO = dtoMapper.mapContactPersonToDTO(personOne);


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            ContactPerson contactPerson = dtoMapper.mapDTOtoContactPerson(contactPersonDTO, institutionId);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for Mapper mapContactDTOToEvents method (positive scenario)")
    @Test
    public void givenContactDTOObject_whenMapContactDTOToEvents_thenReturnEventsList() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution));
        contactDTO.setInstitution(institution);


        //when - action or the behavior that we are going to test
        List<Event> eventList = dtoMapper.mapContactDTOToEvents(contactDTO);

        //then - verify the output
        assertEquals(eventDTOS.size(), eventList.size());
        assertEquals(institutionId, eventList.get(0).getInstitution().getId());

    }

    @DisplayName("JUnit test for Mapper mapContactDTOToEvents method (negative scenario)")
    @Test
    public void givenContactDTOObject_whenMapContactDTOToEvents_thenThrowError() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.empty());
        contactDTO.setInstitution(institution);


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            List<Event> eventList = dtoMapper.mapContactDTOToEvents(contactDTO);
        });

        //then - verify the output
    }

    @DisplayName("JUnit test for Mapper mapContactDTOToContactPeople method (positive scenario)")
    @Test
    public void givenContactDTOObject_whenMapContactDTOToContactPeople_thenReturnContactPersonList() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.of(institution));
        contactDTO.setInstitution(institution);

        //brakuje dtosów!

        //when - action or the behavior that we are going to test
        List<ContactPerson> contactPersonList = dtoMapper.mapContactDTOToContactPeople(contactDTO);

        //then - verify the output
        assertEquals(eventDTOS.size(), contactPersonList.size());
        assertEquals(institutionId, contactPersonList.get(0).getInstitution().getId());

    }

    @DisplayName("JUnit test for Mapper mapContactDTOToContactPeople method (negative scenario)")
    @Test
    public void givenContactDTOObject_whenMapContactDTOToContactPeople_thenThrowError() {
        //given - precondition or setup
        given(institutionService.getById(institutionId)).willReturn(Optional.empty());
        contactDTO.setInstitution(institution);


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            List<ContactPerson> contactPersonList = dtoMapper.mapContactDTOToContactPeople(contactDTO);
        });

        //then - verify the output

    }

}
