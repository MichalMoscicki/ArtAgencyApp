package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceTests {

    @Mock
    EventRepository eventRepository;
    @Mock
    ContactRepository contactRepository;
    @Mock
    DTOMapper dtoMapper;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    EventServiceImpl eventService;
    Event event;
    EventDTO unsynchronizedEventDTO;
    EventDTO synchronizedEventDTO;

    @BeforeEach
    public void setup() {
        event = Event.builder()
                .id(1l)
                .name("Dni Łomianek")
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


    @DisplayName("JUnit test for EventService create method (negative scenario) ")
    @Test
    public void givenContactId_whenCreate_thenDoNotSaveEvent() {
        //given - precondition or setup
        Long id = 0l;
        given(contactRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.create(unsynchronizedEventDTO, id);
        });


        //then - verify the output

    }


    @DisplayName("JUnit test for EventService create method (positive scenario) ")
    @Test
    public void givenUnsynchronizedEventDTOObject_whenCreate_thenReturnSynchronizedEventDTOObject() {
        //given - precondition or setup
        Long id = 0l;
        given(contactRepository.findById(id)).willReturn(Optional.of(new Contact()));
        given(dtoMapper.mapUnsyncInputDTOToEvent(unsynchronizedEventDTO)).willReturn(new Event());
        given(inputCleaner.clean(any(Event.class))).willReturn(new Event());
        given(eventRepository.save(any(Event.class))).willReturn(new Event());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);


        //when - action or the behavior that we are going to test
        EventDTO SynchronizedEventDTO = eventService.create(unsynchronizedEventDTO, id);

        //then - verify the output
        assertNotNull(SynchronizedEventDTO);
        verify(dtoMapper, times(1)).mapUnsyncInputDTOToEvent(any(EventDTO.class));
        verify(inputCleaner, times(1)).clean(any(Event.class));
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapEventToDTO(any(Event.class));

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
        given(inputCleaner.clean(any(Event.class))).willReturn(new Event());
        given(eventRepository.save(any(Event.class))).willReturn(new Event());
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = eventService.update(synchronizedEventDTO);

        //then - verify the output
        assertNotNull(eventDTO);
        verify(eventRepository, times(1)).findById(anyLong());
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



}
