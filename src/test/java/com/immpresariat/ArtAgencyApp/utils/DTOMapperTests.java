package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.time.LocalDate;
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
                .phone("+48111222333")
                .email("abc@gmail.com")
                .webPage("http://www.pksgrojec.pl/rozklad_new/tpo_5129722.html")
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
        InstitutionDTO institutionDTO = dtoMapper.mapToDTO(institution);

        //then - verify the output
        assertEquals(institution.getId(), institutionDTO.getId());
        assertEquals(institution.getName(), institutionDTO.getName());
        assertEquals(institution.getCity(), institutionDTO.getCity());
        assertEquals(institution.getNotes(), institutionDTO.getNotes());
        assertEquals(institution.getCategory(), institutionDTO.getCategory());
        assertEquals(institution.getEmail(), institutionDTO.getEmail());
        assertEquals(institution.getWebPage(), institutionDTO.getWebPage());
        assertEquals(institution.getPhone(), institutionDTO.getPhone());


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
                .phone("+48111222333")
                .email("abc@gmail.com")
                .webPage("http://www.pksgrojec.pl/rozklad_new/tpo_5129722.html")
                .build();

        //when - action or the behavior that we are going to test
        Institution synchronizedInstitution = dtoMapper.mapToEntity(institutionDTO);

        //then - verify the output
        assertNotNull(synchronizedInstitution);
        assertEquals(institutionDTO.getId(), synchronizedInstitution.getId());
        assertEquals(institutionDTO.getName(), synchronizedInstitution.getName());
        assertEquals(institutionDTO.getCity(), synchronizedInstitution.getCity());
        assertEquals(institutionDTO.getCategory(), synchronizedInstitution.getCategory());
        assertEquals(institutionDTO.getNotes(), synchronizedInstitution.getNotes());
        assertEquals(institutionDTO.getEmail(), synchronizedInstitution.getEmail());
        assertEquals(institutionDTO.getWebPage(), synchronizedInstitution.getWebPage());
        assertEquals(institutionDTO.getPhone(), synchronizedInstitution.getPhone());

    }


    @DisplayName("JUnit test for map Event to DTO")
    @Test
    public void givenEventObject_whenMapEventToDto_thenReturnEventDTOObject() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = dtoMapper.mapToDTO(event);

        //then - verify the output
        assertEquals(event.getId(), eventDTO.getId());
        assertEquals(event.getName(), eventDTO.getName());
        assertEquals(event.getMonthWhenOrganized(), eventDTO.getMonthWhenOrganized());
        assertEquals(event.getDescription(), eventDTO.getDescription());


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
        Event synchronizedEvent = dtoMapper.mapToEntity(eventDTO);

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
        ContactPersonDTO contactPersonDTO = dtoMapper.mapToDTO(contactPerson);

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
        ContactPerson contactPerson = dtoMapper.mapToEntity(contactPersonDTO);

        //then - verify the output
        assertEquals(contactPersonDTO.getId(), contactPersonDTO.getId());
        assertEquals(contactPerson.getFirstName(), contactPersonDTO.getFirstName());
        assertEquals(contactPerson.getLastName(), contactPersonDTO.getLastName());
        assertEquals(contactPerson.getRole(), contactPersonDTO.getRole());
        assertEquals(contactPerson.getEmail(), contactPersonDTO.getEmail());
        assertEquals(contactPerson.getPhone(), contactPersonDTO.getPhone());

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
        ContactDTO contactDTO = dtoMapper.mapToDTO(contact);

        //then - verify the output
        assertNotNull(contactDTO);
        assertEquals(contact.getUpdated(), contactDTO.getUpdated());
        assertEquals(contact.isAlreadyCooperated(), contactDTO.isAlreadyCooperated());
        assertEquals(contact.getDescription(), contactDTO.getDescription());
        assertEquals(contact.getTitle(), contactDTO.getTitle());
        assertEquals(institutions.size(), contactDTO.getInstitutions().size());
        assertEquals(events.size(), contactDTO.getEvents().size());
        assertEquals(contactPeople.size(), contactDTO.getContactPeople().size());


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
        Contact contact = dtoMapper.mapToEntity(contactDTO);

        //then - verify the output
        assertNotNull(contact);
        assertEquals(contact.getUpdated(), contactDTO.getUpdated());
        assertEquals(contact.isAlreadyCooperated(), contactDTO.isAlreadyCooperated());
        assertEquals(contact.getDescription(), contactDTO.getDescription());
        assertEquals(contact.getTitle(), contactDTO.getTitle());
        assertEquals(institutions.size(), contact.getInstitutions().size());
        assertEquals(events.size(), contact.getEvents().size());
        assertEquals(contactPeople.size(), contact.getContactPeople().size());

    }

    @DisplayName("JUnit test for mapToTaskDTO method")
    @Test
    public void givenTask_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Task task = Task.builder()
                .id(0L)
                .title("Test")
                .description("Description")
                .activationDate(LocalDate.now())
                .active(true)
                .finished(false)
                .updated(new Date())
                .priority(3)
                .attachment(new TaskAttachment())
                .build();

        //when - action or the behavior that we are going to test
        TaskDTO taskDTO = dtoMapper.mapToDTO(task);

        //then - verify the output
        assertNotNull(taskDTO);
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getPriority(), taskDTO.getPriority());
        assertEquals(task.getActivationDate(), taskDTO.getActivationDate());
        assertEquals(task.isFinished(), taskDTO.isFinished());
        assertEquals(task.isActive(), taskDTO.isActive());
        assertEquals(task.getUpdated(), taskDTO.getUpdated());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getAttachment(), taskDTO.getAttachment());
        assertEquals(task.getTitle(), taskDTO.getTitle());

    }

    @DisplayName("JUnit test for mapToTaskDTO method")
    @Test
    public void givenTaskDTO_whenMapToTask_thenReturnTask() {
        //given - precondition or setup
        TaskDTO taskDTO = TaskDTO.builder()
                .id(0L)
                .title("Test")
                .description("Description")
                .activationDate(LocalDate.now())
                .active(true)
                .finished(false)
                .updated(new Date())
                .priority(3)
                .build();

        //when - action or the behavior that we are going to test
        Task task = dtoMapper.mapToEntity(taskDTO);

        //then - verify the output
        assertNotNull(taskDTO);
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getPriority(), taskDTO.getPriority());
        assertEquals(task.getActivationDate(), taskDTO.getActivationDate());
        assertEquals(task.isFinished(), taskDTO.isFinished());
        assertEquals(task.isActive(), taskDTO.isActive());
        assertEquals(task.getUpdated(), taskDTO.getUpdated());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getAttachment(), taskDTO.getAttachment());
        assertEquals(task.getTitle(), taskDTO.getTitle());

    }

    @DisplayName("JUnit test for mapToDTO (InstrumentDTO) method")
    @Test
    public void givenInstrument_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("Piano")
                .build();

        //when - action or the behavior that we are going to test
        InstrumentDTO instrumentDTO = dtoMapper.mapToDTO(instrument);

        //then - verify the output
        assertNotNull(instrumentDTO);
        assertEquals(instrument.getId(), instrumentDTO.getId());
        assertEquals(instrument.getName(), instrumentDTO.getName());
    }

    @DisplayName("JUnit test for mapToEntity (Instrument) method")
    @Test
    public void givenInstrumentDTO_whenMapToEntity_thenReturnInstrument() {
        //given - precondition or setup
        InstrumentDTO instrumentDTO = InstrumentDTO.builder()
                .id(0L)
                .name("Piano")
                .build();

        //when - action or the behavior that we are going to test
        Instrument instrument = dtoMapper.mapToEntity(instrumentDTO);

        //then - verify the output
        assertNotNull(instrumentDTO);
        assertEquals(instrument.getId(), instrumentDTO.getId());
        assertEquals(instrument.getName(), instrumentDTO.getName());
    }

    @DisplayName("JUnit test for mapToDTO (MusicianDTO) method")
    @Test
    public void givenMusician_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Musician musician = Musician.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48111222333")
                .notes("Notatki")
                .instruments(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        MusicianDTO musicianDTO = dtoMapper.mapToDTO(musician);

        //then - verify the output
        assertNotNull(musicianDTO);
        assertEquals(musician.getId(), musicianDTO.getId());
        assertEquals(musician.getFirstName(), musicianDTO.getFirstName());
        assertEquals(musician.getLastName(), musicianDTO.getLastName());
        assertEquals(musician.getPhone(), musicianDTO.getPhone());
        assertEquals(musician.getEmail(), musicianDTO.getEmail());
        assertEquals(musician.getNotes(), musicianDTO.getNotes());
        assertEquals(musician.getInstruments(), musicianDTO.getInstruments());

    }

    @DisplayName("JUnit test for mapToEntity (Musician) method")
    @Test
    public void givenMusicianDTO_whenMapToEntity_thenReturnMusician() {
        //given - precondition or setup
        MusicianDTO musicianDTO = MusicianDTO.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48111222333")
                .notes("Notatki")
                .instruments(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        Musician musician = dtoMapper.mapToEntity(musicianDTO);

        //then - verify the output
        assertNotNull(musicianDTO);
        assertEquals(musician.getId(), musicianDTO.getId());
        assertEquals(musician.getFirstName(), musicianDTO.getFirstName());
        assertEquals(musician.getLastName(), musicianDTO.getLastName());
        assertEquals(musician.getPhone(), musicianDTO.getPhone());
        assertEquals(musician.getEmail(), musicianDTO.getEmail());
        assertEquals(musician.getNotes(), musicianDTO.getNotes());
        assertEquals(musician.getInstruments(), musicianDTO.getInstruments());

    }
}
