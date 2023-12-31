package com.immpresariat.ArtAgencyApp.utils;

import com.google.api.client.util.DateTime;
import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.*;

import static org.junit.jupiter.api.Assertions.*;

import com.immpresariat.ArtAgencyApp.repository.ConcertRepository;
import com.immpresariat.ArtAgencyApp.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class DTOMapperTests {

    private Institution institution;
    private Event event;
    private Long contactPersonId;
    private ContactPerson contactPerson;
    @Mock
    private SongRepository songRepository;
    @InjectMocks
    private DTOMapper dtoMapper;

    @BeforeEach
    public void setup() {
        dtoMapper = new DTOMapper();

        Long institutionId = 1L;
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

        Long eventId = 1L;
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

    @DisplayName("JUnit test for mapToDTO (SongDTO) method")
    @Test
    public void givenSong_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Song song = Song.builder()
                .id(0L)
                .title("Piosenka")
                .description("Opis")
                .composers("Karol Modzelewski, Jan Orwat")
                .textAuthors("Wiesio Kiprowicz")
                .parts(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        SongDTO songDTO = dtoMapper.mapToDTO(song);

        //then - verify the output
        assertNotNull(songDTO);
        assertEquals(song.getId(), songDTO.getId());
        assertEquals(song.getTitle(), songDTO.getTitle());
        assertEquals(song.getDescription(), songDTO.getDescription());
        assertEquals(song.getComposers(), songDTO.getComposers());
        assertEquals(song.getTextAuthors(), songDTO.getTextAuthors());

    }

    @DisplayName("JUnit test for mapToEntity (Song) method")
    @Test
    public void givenSongDTO_whenMapToEntity_thenReturnSong() {
        //given - precondition or setup
        SongDTO songDTO = SongDTO.builder()
                .id(0L)
                .title("Piosenka")
                .description("Opis")
                .composers("Karol Modzelewski, Jan Orwat")
                .textAuthors("Wiesio Kiprowicz")
                .parts(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        Song song = dtoMapper.mapToEntity(songDTO);

        //then - verify the output
        assertNotNull(song);
        assertEquals(song.getId(), songDTO.getId());
        assertEquals(song.getTitle(), songDTO.getTitle());
        assertEquals(song.getDescription(), songDTO.getDescription());
        assertEquals(song.getComposers(), songDTO.getComposers());
        assertEquals(song.getTextAuthors(), songDTO.getTextAuthors());

    }

    @DisplayName("JUnit test for mapToDTO (PartDTO) method")
    @Test
    public void givenPart_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("bas")
                .build();

        Part part = Part.builder()
                .id(0L)
                .type("aplication/pdf")
                .instrument(instrument)
                .data(new byte[4])
                .build();

        //when - action or the behavior that we are going to test
        PartDTO partDTO = dtoMapper.mapToDTO(part);

        //then - verify the output
        assertNotNull(partDTO);
        assertEquals(part.getId(), partDTO.getId());
        assertEquals(part.getType(), partDTO.getType());
        assertEquals(part.getInstrument().getName(), partDTO.getInstrumentName());
        assertEquals("http://localhost/api/v1/parts/" + part.getId(), partDTO.getUrl());
    }

    @DisplayName("JUnit test for mapToDTO (ConcertDTO) method")
    @Test
    public void givenConcert_whenMapToDTO_thenReturnDTO() {
        //given - precondition or setup
        Concert concert = Concert.builder()
                .id(0L)
                .title("Koncert w Filharmonii")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .description("Description")
                .musicians(new ArrayList<>())
                .songs(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        ConcertDTO concertDTO = dtoMapper.mapToDTO(concert);

        //then - verify the output
        assertNotNull(concertDTO);
        assertEquals(concert.getId(), concertDTO.getId());
        assertEquals(concert.getStart(), concertDTO.getStart());
        assertEquals(concert.getEnd(), concertDTO.getEnd());
        assertEquals(concert.getDescription(), concertDTO.getDescription());
        assertEquals(concert.getTitle(), concertDTO.getTitle());
        assertEquals(concert.getMusicians(), concertDTO.getMusicians());
        assertEquals(concert.getSongs().size(), concertDTO.getSongs().size());

    }

    @DisplayName("JUnit test for mapToEntity (Concert) method")
    @Test
    public void givenConcertDTO_whenMapToEntity_thenReturnConcert() {
        //given - precondition or setup
        ConcertDTO concertDTO = ConcertDTO.builder()
                .id(0L)
                .title("Koncert w Filharmonii")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .description("Description")
                .musicians(new ArrayList<>())
                .songs(new ArrayList<>())
                .build();

        //when - action or the behavior that we are going to test
        Concert concert = dtoMapper.mapToEntity(concertDTO, songRepository);

        //then - verify the output
        assertNotNull(concert);
        assertEquals(concert.getId(), concertDTO.getId());
        assertEquals(concert.getTitle(), concertDTO.getTitle());
        assertEquals(concert.getStart(), concertDTO.getStart());
        assertEquals(concert.getEnd(), concertDTO.getEnd());
        assertEquals(concert.getDescription(), concertDTO.getDescription());
        assertEquals(concert.getMusicians(), concertDTO.getMusicians());
        assertEquals(concert.getSongs().size(), concertDTO.getSongs().size());
    }


}
