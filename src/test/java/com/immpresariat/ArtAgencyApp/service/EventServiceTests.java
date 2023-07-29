package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @InjectMocks
    EventServiceImpl eventService;

    Institution institution;
    Event event;

    @BeforeEach
    public void setup(){
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
    }

    @DisplayName("JUnit test for EventService create method (negative scenario) ")
    @Test
    public void givenExistingEvent_whenCreate_thenThrowsException() {
        //given - precondition or setup
        given(eventRepository.findEventByNameAndInstitution(event.getName(), event.getInstitution()))
                .willReturn(Optional.of(event));

        //when - action or the behavior that we are going to test
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            eventService.create(event);
        });

        //then - verify the output
        Mockito.verify(eventRepository, never()).save(any(Event.class));

    }

    @DisplayName("JUnit test for EventService create method (positive scenario) ")
    @Test
    public void givenEventObject_whenCreate_thenReturnEventObject() {
        //given - precondition or setup
        given(eventRepository.save(event)).willReturn(event);

        //when - action or the behavior that we are going to test
        Event eventDB = eventRepository.save(event);

        //then - verify the output
        Assertions.assertEquals(eventDB, event);

    }

}
