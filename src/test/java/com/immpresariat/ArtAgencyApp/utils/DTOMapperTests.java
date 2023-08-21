package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
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
                .category("dom kultury")
                .build();

//        contactDTO = new ContactDTO();
//
//        personOne = ContactPerson.builder()
//                .id(1l)
//                .email("test@test.pl")
//                .phone("+48111222333")
//                .firstName("Jan")
//                .lastName("Kowalski")
//                .role("Dyrektor")
//                .institution(institution)
//                .build();
//
//        personTwo = new ContactPerson();
//
//        contactPeople = new ArrayList<>();
//        contactPeople.add(personOne);
//        contactPeople.add(personTwo);

//        contactPeopleDTO = new ArrayList<>();
//        contactPeopleDTO.add(dtoMapper.mapContactPersonToDTO(personOne));
//        contactPeopleDTO.add(dtoMapper.mapContactPersonToDTO(personTwo));
 //       contactDTO.setContactPersonDTOS(contactPeopleDTO);

//        eventOne = Event.builder()
//                .id(1l)
//                .description("Dni ogórka")
//                .monthWhenOrganized(8)
//                .institution(institution)
//                .name("Odpust na Stalowej")
//                .build();
//        eventTwo = new Event();
//
//        events = new ArrayList<>();
//        events.add(eventOne);
//        events.add(eventTwo);
//
//        eventDTOS = new ArrayList<>();
//        eventDTOS.add(dtoMapper.mapEventToDTO(eventOne));
//        eventDTOS.add(dtoMapper.mapEventToDTO(eventTwo));
//        contactDTO.setEventDTOS(eventDTOS);

    }

/*
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


 */


    @DisplayName("JUnit test for map Institution to DTO")
    @Test
    public void givenInstitutionObject_whenMapInstitutionToDto_thenReturnInstitutionDTOObject() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTO = dtoMapper.mapInstitutionToDTO(institution);

        //then - verify the output
        assertEquals(institution.getId(), institutionDTO.getId());
        assertEquals(institution.getName(), institutionDTO.getName());
        assertEquals(institution.getCity(), institutionDTO.getCity());
        assertEquals(institution.getNotes(), institutionDTO.getNotes());
        assertEquals(institution.getCategory(), institutionDTO.getCategory());
        assertEquals(institution.isAlreadyCooperated(), institutionDTO.isAlreadyCooperated());

    }

    @DisplayName("JUnit test for map input DTO to Institution")
    @Test
    public void givenInstitutionDTOWithNoId_whenMapInputDTOToInstitution_thenReturnInstitutionObject() {
        //given - precondition or setup
        InstitutionDTO inputInstitutionDTO = InstitutionDTO.builder()
                .name("DK Chotomów")
                .city("Chotomów")
                .notes("Cool miejsce")
                .category("DK")
                .alreadyCooperated(true)
                .build();


        //when - action or the behavior that we are going to test
        Institution unsynchronizedInstitution = dtoMapper.mapInputDTOToInstitution(inputInstitutionDTO);

        //then - verify the output
        assertNotNull(unsynchronizedInstitution);
        assertNull(unsynchronizedInstitution.getId());
        assertEquals(inputInstitutionDTO.getName(), unsynchronizedInstitution.getName());
        assertEquals(inputInstitutionDTO.getCity(), unsynchronizedInstitution.getCity());
        assertEquals(inputInstitutionDTO.getCategory(), unsynchronizedInstitution.getCategory());
        assertEquals(inputInstitutionDTO.getNotes(), unsynchronizedInstitution.getNotes());
        assertEquals(inputInstitutionDTO.isAlreadyCooperated(), unsynchronizedInstitution.isAlreadyCooperated());

    }


    @DisplayName("JUnit test map DTO to Institution")
    @Test
    public void given_when_then() {
        //given - precondition or setup
        InstitutionDTO institutionDTO = InstitutionDTO.builder()
                .id(0L)
                .name("DK Chotomów")
                .city("Chotomów")
                .notes("Cool miejsce")
                .category("DK")
                .alreadyCooperated(true)
                .build();

        //when - action or the behavior that we are going to test
        Institution synchronizedInstitution = dtoMapper.mapDTOToInstitution(institutionDTO);

        //then - verify the output
        assertNotNull(synchronizedInstitution);
        assertEquals(institutionDTO.getId(), synchronizedInstitution.getId());
        assertEquals(institutionDTO.getName(), synchronizedInstitution.getName());
        assertEquals(institutionDTO.getCity(), synchronizedInstitution.getCity());
        assertEquals(institutionDTO.getCategory(), synchronizedInstitution.getCategory());
        assertEquals(institutionDTO.getNotes(), synchronizedInstitution.getNotes());
        assertEquals(institutionDTO.isAlreadyCooperated(), synchronizedInstitution.isAlreadyCooperated());

    }


}
