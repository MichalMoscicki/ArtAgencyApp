package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class DTOMapperTests {

    DTOMapper dtoMapper;

    Long institutionId;
    Institution institution;

    Long eventId;
    Event event;

    Long contactPersonId;
    ContactPerson contactPerson;



    @BeforeEach
    public void setup() {
        dtoMapper = new DTOMapper();

        institutionId = 1l;
        institution = Institution.builder()
                .id(institutionId)
                .name("DK Praga")
                .notes("Super miejsce")
                .alreadyCooperated(true)
                .city("Warszawa")
                .category("dom kultury")
                .build();

        eventId = 1l;
        event = Event.builder()
                .id(eventId)
                .name("Dni Miasta")
                .institution(institution)
                .description("")
                .build();

        contactPersonId = 1l;
        contactPerson = ContactPerson.builder()
                .id(contactPersonId)
                .email("test@test.pl")
                .phone("+48111222333")
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
                .institution(institution)
                .build();

    }


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

    @DisplayName("JUnit test for map unsync DTO to Institution")
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
        Institution unsynchronizedInstitution = dtoMapper.mapUnsyncDTOToInstitution(inputInstitutionDTO);

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
    public void givenInstitutionDTOObject_whenMapDTOToInstitution_thenInstitutionObject() {
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

    @DisplayName("JUnit test for map Event to DTO")
    @Test
    public void givenEventObject_whenMapEventToDto_thenReturnEventDTOObject() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = dtoMapper.mapEventToDTO(event);

        //then - verify the output
        assertEquals(event.getId(), eventDTO.getId());
        assertEquals(event.getName(), eventDTO.getName());
        assertEquals(event.getMonthWhenOrganized(), eventDTO.getMonthWhenOrganized());
        assertEquals(event.getDescription(), eventDTO.getDescription());


    }

    @DisplayName("JUnit test for map unsync DTO to Event")
    @Test
    public void givenEventDTOWithNoId_whenMapInputDTOToEvent_thenReturnEventObject() {
        //given - precondition or setup
        EventDTO inputEventDTO = EventDTO.builder()
                .name(event.getName())
                .monthWhenOrganized(event.getMonthWhenOrganized())
                .description(event.getDescription())
                .build();


        //when - action or the behavior that we are going to test
        Event unsynchronizedEvent = dtoMapper.mapUnsyncInputDTOToEvent(inputEventDTO);


        //then - verify the output
        assertNotNull(unsynchronizedEvent);
        assertNull(unsynchronizedEvent.getId());
        assertEquals(inputEventDTO.getName(), unsynchronizedEvent.getName());
        assertEquals(inputEventDTO.getMonthWhenOrganized(), unsynchronizedEvent.getMonthWhenOrganized());
        assertEquals(inputEventDTO.getDescription(), unsynchronizedEvent.getDescription());

    }


    @DisplayName("JUnit test map DTO to Event")
    @Test
    public void givenEventDTOObject_whenMapDTOToEvent_thenEventObject() {
        //given - precondition or setup
        EventDTO eventDTO = EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .monthWhenOrganized(event.getMonthWhenOrganized())
                .description(event.getDescription())
                .build();

        //when - action or the behavior that we are going to test
        Event synchronizedEvent = dtoMapper.mapDTOToEvent(eventDTO);

        //then - verify the output
        assertNotNull(synchronizedEvent);
        assertEquals(eventDTO.getId(), synchronizedEvent.getId());
        assertEquals(eventDTO.getName(), synchronizedEvent.getName());
        assertEquals(eventDTO.getDescription(), synchronizedEvent.getDescription());
        assertEquals(eventDTO.getMonthWhenOrganized(), synchronizedEvent.getMonthWhenOrganized());

    }

    @DisplayName("JUnit test for mapContactPerson to DTO")
    @Test
    public void givenContactPersonObject_whenMapContactPersonToDto_thenReturnContactPersonDTOObject() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        ContactPersonDTO contactPersonDTO = dtoMapper.mapContactPersonToDTO(contactPerson);

        //then - verify the output
        assertEquals(contactPerson.getId(), contactPersonDTO.getId());
        assertEquals(contactPerson.getFirstName(), contactPersonDTO.getFirstName());
        assertEquals(contactPerson.getLastName(), contactPersonDTO.getLastName());
        assertEquals(contactPerson.getRole(), contactPersonDTO.getRole());
        assertEquals(contactPerson.getEmail(), contactPersonDTO.getEmail());
        assertEquals(contactPerson.getPhone(), contactPersonDTO.getPhone());

    }
    @DisplayName("JUnit test for map DTO to ContactPerson")
    @Test
    public void givenContactPersonDTOObject_whenMapDtoToContactPerson_thenReturnContactPersonObject() {
        //given - precondition or setup
        ContactPersonDTO contactPersonDTO = ContactPersonDTO.builder()
                .id(contactPersonId)
                .email("test@test.pl")
                .phone("+48111222333")
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
                .build();

        //when - action or the behavior that we are going to test
        ContactPerson contactPerson = dtoMapper.mapDTOToContactPerson(contactPersonDTO);

        //then - verify the output
        assertEquals(contactPersonDTO.getId(), contactPersonDTO.getId());
        assertEquals(contactPerson.getFirstName(), contactPersonDTO.getFirstName());
        assertEquals(contactPerson.getLastName(), contactPersonDTO.getLastName());
        assertEquals(contactPerson.getRole(), contactPersonDTO.getRole());
        assertEquals(contactPerson.getEmail(), contactPersonDTO.getEmail());
        assertEquals(contactPerson.getPhone(), contactPersonDTO.getPhone());

    }

    @DisplayName("JUnit test for map unsync DTO to ContactPerson")
    @Test
    public void givenUnsyncContactPersonDTOObject_whenMapDtoToContactPerson_thenReturnContactPersonObject() {
        //given - precondition or setup
        ContactPersonDTO UnsyncContactPersonDTO = ContactPersonDTO.builder()
                .email("test@test.pl")
                .phone("+48111222333")
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
                .build();

        //when - action or the behavior that we are going to test
        ContactPerson contactPerson = dtoMapper.mapUnsyncDTOToContactPerson(UnsyncContactPersonDTO);

        //then - verify the output
        assertNull(contactPerson.getId());
        assertEquals(contactPerson.getFirstName(), UnsyncContactPersonDTO.getFirstName());
        assertEquals(contactPerson.getLastName(), UnsyncContactPersonDTO.getLastName());
        assertEquals(contactPerson.getRole(), UnsyncContactPersonDTO.getRole());
        assertEquals(contactPerson.getEmail(), UnsyncContactPersonDTO.getEmail());
        assertEquals(contactPerson.getPhone(), UnsyncContactPersonDTO.getPhone());

    }

}
