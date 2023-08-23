package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputCleanerTest {
    InputCleaner inputCleaner;

    @BeforeEach
    public void setup() {
        inputCleaner = new InputCleaner();
    }

    @DisplayName("JUnit test for trim institution method")
    @Test
    public void givenInstitutionObject_whenTrim_thenReturnTrimmedInstitutionObject() {
        //given - precondition or setup
        Institution institution = Institution.builder()
                .name(" Dom Kultury ")
                .notes("   Notatki    ")
                .city(" Jakieś miasto  ")
                .category(" dom kultury ")
                .build();

        //when - action or the behavior that we are going to test
        Institution trimmedInstitution = inputCleaner.clean(institution);


        //then - verify the output
        assertEquals("Dom Kultury", trimmedInstitution.getName());
        assertEquals("Notatki", trimmedInstitution.getNotes());
        assertEquals("Jakieś miasto", trimmedInstitution.getCity());
        assertEquals("dom kultury", trimmedInstitution.getCategory());

    }

    @DisplayName("JUnit test for trim event method")
    @Test
    public void givenEventObject_whenTrim_thenReturnTrimmedEventObject() {
        //given - precondition or setup
        Event event = Event.builder()
                .name("  Biba")
                .description("Cool biiiba  ")
                .build();


        //when - action or the behavior that we are going to test
        Event trimmedEvent = inputCleaner.clean(event);


        //then - verify the output
        assertEquals("Biba", trimmedEvent.getName());
        assertEquals("Cool biiiba", trimmedEvent.getDescription());

    }

    @DisplayName("JUnit test for trim contactPerson method")
    @Test
    public void givenContactPersonObject_whenTrim_thenReturnTrimmedContactPersonObject() {
        //given - precondition or setup
        ContactPerson contactPerson = ContactPerson.builder()
                .role(" Dyrektor")
                .email("mail@mail.pl ")
                .phone(" +48777222666 ")
                .firstName("   Jan  ")
                .lastName(" Kowalski   ")
                .build();

        //when - action or the behavior that we are going to test
        ContactPerson trimmedContactPerson = inputCleaner.clean(contactPerson);


        //then - verify the output
        assertEquals("dyrektor", trimmedContactPerson.getRole());
        assertEquals("mail@mail.pl", trimmedContactPerson.getEmail());
        assertEquals("+48777222666", trimmedContactPerson.getPhone());
        assertEquals("Jan", trimmedContactPerson.getFirstName());
        assertEquals("Kowalski", trimmedContactPerson.getLastName());

    }


}