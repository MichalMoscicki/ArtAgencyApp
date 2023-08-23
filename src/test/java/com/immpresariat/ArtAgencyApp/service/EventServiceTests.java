package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.impl.EventServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceTests {

    @Mock
    EventRepository eventRepository;
    @Mock
    InstitutionRepository institutionRepository;
    @Mock
    DTOMapper dtoMapper;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    EventServiceImpl eventService;

    Institution institution;
    Event event;
    EventDTO unsynchronizedEventDTO;
    EventDTO synchronizedEventDTO;

    @BeforeEach
    public void setup() {
        institution = Institution.builder()
                .id(1l)
                .city("Łomianki")
                .name("Dom Kultury Łomianki")
                .alreadyCooperated(true)
                .notes("Graliśmy tu z Marią")
                .build();

        event = Event.builder()
                .id(1l)
                .name("Dni Łomianek")
                .institution(institution)
                .description("Impreza plenerowa")
                .monthWhenOrganized(6)
                .build();

        unsynchronizedEventDTO = EventDTO.builder()
                .name("Dni Łomianek")
                .description("Impreza plenerowa")
                .monthWhenOrganized(6)
                .build();

        synchronizedEventDTO = EventDTO.builder()
                .id(0l)
                .monthWhenOrganized(unsynchronizedEventDTO.getMonthWhenOrganized())
                .description(unsynchronizedEventDTO.getDescription())
                .name(unsynchronizedEventDTO.getName())
                .build();
    }

    @DisplayName("JUnit test for EventService create method (negative scenario - no institution)")
    @Test
    public void givenUnsynchronizedEventDTOObject_whenCreate_thenThrowResourceNotFoundException() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
       assertThrows(ResourceNotFoundException.class, () -> {
            eventService.create(unsynchronizedEventDTO, institution.getId());
        });

        //then - verify the output
        Mockito.verify(eventRepository, never()).save(any(Event.class));

    }

    @DisplayName("JUnit test for EventService create method (negative scenario - event already exists) ")
    @Test
    public void givenUnsynchronizedEventDTOObject_whenCreate_thenThrowResourceAlreadyExistsException() {
        //given - precondition or setup
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));
        given(eventRepository.findEventByNameAndInstitution(unsynchronizedEventDTO.getName(), institution))
                .willReturn(Optional.of(event));

        //when - action or the behavior that we are going to test
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            eventService.create(unsynchronizedEventDTO, institution.getId());
        });

        //then - verify the output
        Mockito.verify(eventRepository, never()).save(any(Event.class));

    }

    @DisplayName("JUnit test for EventService create method (positive scenario) ")
    @Test
    public void givenUnsynchronizedEventDTOObject_whenCreate_thenReturnSynchronizedEventDTOObject() {
        //given - precondition or setup
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));
        given(eventRepository.findEventByNameAndInstitution(unsynchronizedEventDTO.getName(), institution))
                .willReturn(Optional.empty());

        given(dtoMapper.mapUnsyncInputDTOToEvent(unsynchronizedEventDTO)).willReturn(new Event());
        given(inputCleaner.clean(any(Event.class))).willReturn(new Event());
        given(eventRepository.save(any(Event.class))).willReturn(new Event());
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);


        //when - action or the behavior that we are going to test
        EventDTO SynchronizedEventDTO = eventService.create(unsynchronizedEventDTO, institution.getId());

        //then - verify the output
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findEventByNameAndInstitution(anyString(),any(Institution.class));
        verify(dtoMapper, times(1)).mapUnsyncInputDTOToEvent(any(EventDTO.class));
        verify(inputCleaner, times(1)).clean(any(Event.class));
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(dtoMapper, times(1)).mapEventToDTO(any(Event.class));

    }

    @DisplayName("JUnit test for EventService getAll method")
    @Test
    public void whenGetAll_thenReturnEventDTOsList() {
        //given - precondition or setup
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        given(eventRepository.findAll()).willReturn(eventList);
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(new EventDTO());


        //when - action or the behavior that we are going to test
        List<EventDTO> eventDTOS = eventService.getAll();

        //then - verify the output
        verify(eventRepository, times(1)).findAll();
        verify(dtoMapper, times(eventList.size())).mapEventToDTO(any(Event.class));
        assertEquals(eventList.size(), eventDTOS.size());

    }

    @DisplayName("JUnit test for EventService getAllByInstitutionId method (negative scenario)")
    @Test
    public void givenInstitutionId_whenGetAllByInstitutionId_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long institutionId = institution.getId();

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getAllByInstitutionId(institutionId);
        });

        //then - verify the output
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(0)).findAllByInstitutionId(institutionId);
        verify(dtoMapper, times(0)).mapEventToDTO(any(Event.class));
    }

    @DisplayName("JUnit test for EventService getAllByInstitutionId method (positive scenario)")
    @Test
    public void givenInstitutionId_whenGetAllByInstitutionId_thenReturnEventDTOsList() {
        //given - precondition or setup
        List<Event> events = new ArrayList<>();
        events.add(event);

        Long institutionId = institution.getId();
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));
        given(eventRepository.findAllByInstitutionId(institutionId)).willReturn(events);
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(new EventDTO());

        //when - action or the behavior that we are going to test
        List<EventDTO> eventDTOS = eventService.getAllByInstitutionId(institutionId);

        //then - verify the output
        assertNotNull(eventDTOS);
        assertEquals(events.size(), eventDTOS.size());
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findAllByInstitutionId(institutionId);
        verify(dtoMapper, times(events.size())).mapEventToDTO(any(Event.class));
    }



    @DisplayName("JUnit test for EventService getById method (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(eventRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getById(synchronizedEventDTO.getId());
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for EventService getById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnEventDTOObject() {
        //given - precondition or setup
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(event));
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = eventService.getById(synchronizedEventDTO.getId());

        //then - verify the output
        verify(eventRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapEventToDTO(any(Event.class));

    }

    @DisplayName("JUnit test for EventService update method (negative scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        EventDTO inputDTO = synchronizedEventDTO;
        given(eventRepository.findById(inputDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.update(inputDTO);
        });
        //then - verify the output

    }

    @DisplayName("JUnit test for EventService update method (positive scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenReturnEventDTO() {
        //given - precondition or setup
        EventDTO inputDTO = synchronizedEventDTO;
        given(eventRepository.findById(inputDTO.getId())).willReturn(Optional.of(event));
        given(dtoMapper.mapDTOToEvent(any(EventDTO.class))).willReturn(new Event());
        given(inputCleaner.clean(any(Event.class))).willReturn(new Event());
        given(eventRepository.save(any(Event.class))).willReturn(new Event());
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = eventService.update(synchronizedEventDTO);

        //then - verify the output
        assertNotNull(eventDTO);
        verify(eventRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapDTOToEvent(any(EventDTO.class));
        verify(inputCleaner, times(1)).clean(any(Event.class));
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(dtoMapper, times(1)).mapEventToDTO(any(Event.class));

    }
    @DisplayName("JUnit test for EventService delete method")
    @Test
    public void givenId_whenDelete_thenEventDeleted() {
        //given - precondition or setup
        Long id = synchronizedEventDTO.getId();
        doNothing().when(eventRepository).deleteById(id);


        //when - action or the behavior that we are going to test
        eventService.delete(id);

        //then - verify the output
        verify(eventRepository, Mockito.times(1)).deleteById(id);
    }

    @DisplayName("JUnit test for EventService delete method")
    @Test
    public void givenEventObject_whenDelete_thenEventDeleted() {
        //given - precondition or setup
        doNothing().when(eventRepository).delete(event);

        //when - action or the behavior that we are going to test
        eventService.delete(event);

        //then - verify the output
        verify(eventRepository, Mockito.times(1)).delete(event);
    }


}
