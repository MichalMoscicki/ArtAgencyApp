package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        institutionId = 1L;
        institution = Institution.builder()
                .id(institutionId)
                .name("DK Praga")
                .notes("Super miejsce")
                .city("Warszawa")
                .category("dom kultury")
                .build();

        eventId = 1L;
        event = Event.builder()
                .id(eventId)
                .name("Dni Miasta")
                .description("")
                .build();

        contactPersonId = 1L;
        contactPerson = ContactPerson.builder()
                .id(contactPersonId)
                .email("test@test.pl")
                .phone("+48111222333")
                .firstName("Jan")
                .lastName("Kowalski")
                .role("Dyrektor")
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

    @DisplayName("JUnit test for mapContactToDTO method")
    @Test
    public void givenContactObject_whenMapContactToDTO_thenContactDTOObject() throws MalformedURLException {
        //given - precondition or setup
        Contact contact = new Contact();
        contact.setTitle("Nowy Kontakt");
        contact.setAlreadyCooperated(false);
        contact.setDescription("Opis");
        contact.setUpdated(new Date());
        contact.setPhone("+48111222333");
        contact.setEmail("abc@gmail.com");
        contact.setWebPage("http://www.pksgrojec.pl/rozklad_new/tpo_5129722.html");
        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);
        List<Event> events = new ArrayList<>();
        events.add(event);
        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson);
        contact.setEvents(events);
        contact.setInstitutions(institutions);
        contact.setContactPeople(contactPeople);

        //when - action or the behavior that we are going to test
        ContactDTO contactDTO = dtoMapper.mapContactToDTO(contact);

        //then - verify the output
        assertNotNull(contactDTO);
        assertEquals(contact.getUpdated(), contactDTO.getUpdated());
        assertEquals(contact.isAlreadyCooperated(), contactDTO.isAlreadyCooperated());
        assertEquals(contact.getDescription(), contactDTO.getDescription());
        assertEquals(contact.getTitle(), contactDTO.getTitle());
        assertEquals(institutions.size(), contactDTO.getInstitutions().size());
        assertEquals(events.size(), contactDTO.getEvents().size());
        assertEquals(contactPeople.size(), contactDTO.getContactPeople().size());
        assertEquals(contact.getEmail(), contactDTO.getEmail());
        assertEquals(contact.getWebPage(), contactDTO.getWebPage());
        assertEquals(contact.getPhone(), contactDTO.getPhone());

    }

    @DisplayName("JUnit test for mapDTOToContact method")
    @Test
    public void givenContactDTOObject_whenMapContactToDTO_thenContactObject() throws MalformedURLException {
        //given - precondition or setup
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setTitle("Nowy Kontakt");
        contactDTO.setAlreadyCooperated(false);
        contactDTO.setDescription("Opis");
        contactDTO.setUpdated(new Date());
        contactDTO.setPhone("+48111222333");
        contactDTO.setEmail("abc@gmail.com");
        contactDTO.setWebPage("http://www.pksgrojec.pl/rozklad_new/tpo_5129722.html");
        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);
        List<Event> events = new ArrayList<>();
        events.add(event);
        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson);
        contactDTO.setEvents(events);
        contactDTO.setInstitutions(institutions);
        contactDTO.setContactPeople(contactPeople);

        //when - action or the behavior that we are going to test
        Contact contact = dtoMapper.mapDTOToContact(contactDTO);

        //then - verify the output
        assertNotNull(contact);
        assertEquals(contact.getUpdated(), contactDTO.getUpdated());
        assertEquals(contact.isAlreadyCooperated(), contactDTO.isAlreadyCooperated());
        assertEquals(contact.getDescription(), contactDTO.getDescription());
        assertEquals(contact.getTitle(), contactDTO.getTitle());
        assertEquals(institutions.size(), contact.getInstitutions().size());
        assertEquals(events.size(), contact.getEvents().size());
        assertEquals(contactPeople.size(), contact.getContactPeople().size());
        assertEquals(contact.getEmail(), contactDTO.getEmail());
        assertEquals(contact.getWebPage(), contactDTO.getWebPage());
        assertEquals(contact.getPhone(), contactDTO.getPhone());

    }

}
