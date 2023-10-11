package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.*;

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

    @DisplayName("JUnit test for clean institution method")
    @Test
    public void givenInstitutionObject_whenClean_thenReturnCleanedInstitutionObject() {
        //given - precondition or setup
        Institution institution = Institution.builder()
                .name(" Dom Kultury ")
                .notes("   Notatki    ")
                .city(" Jakieś miasto  ")
                .category(" dom kultury ")
                .webPage(" https://www.google.com/search ")
                .phone(" +48938475970 ")
                .email(" abc@.gmail.com")
                .build();

        //when - action or the behavior that we are going to test
        Institution cleanedInstitution = inputCleaner.clean(institution);


        //then - verify the output
        assertEquals("Dom Kultury", cleanedInstitution.getName());
        assertEquals("Notatki", cleanedInstitution.getNotes());
        assertEquals("Jakieś miasto", cleanedInstitution.getCity());
        assertEquals("dom kultury", cleanedInstitution.getCategory());
        assertEquals("+48938475970", cleanedInstitution.getPhone());
        assertEquals("https://www.google.com/search", cleanedInstitution.getWebPage());
        assertEquals("abc@.gmail.com", cleanedInstitution.getEmail());

    }

    @DisplayName("JUnit test for clean event method")
    @Test
    public void givenEventObject_whenClean_thenReturnCleanedEventObject() {
        //given - precondition or setup
        Event event = Event.builder()
                .name("  Biba")
                .description("Cool biiiba  ")
                .build();


        //when - action or the behavior that we are going to test
        Event cleanedEvent = inputCleaner.clean(event);


        //then - verify the output
        assertEquals("Biba", cleanedEvent.getName());
        assertEquals("Cool biiiba", cleanedEvent.getDescription());

    }

    @DisplayName("JUnit test for clean contactPerson method")
    @Test
    public void givenContactPersonObject_whenClean_thenReturnCleanedContactPersonObject() {
        //given - precondition or setup
        ContactPerson contactPerson = ContactPerson.builder()
                .role(" Dyrektor")
                .email("mail@mail.pl ")
                .phone(" +48777222666 ")
                .firstName("   Jan  ")
                .lastName(" Kowalski   ")
                .build();

        //when - action or the behavior that we are going to test
        ContactPerson cleanedContactPerson = inputCleaner.clean(contactPerson);


        //then - verify the output
        assertEquals("dyrektor", cleanedContactPerson.getRole());
        assertEquals("mail@mail.pl", cleanedContactPerson.getEmail());
        assertEquals("+48777222666", cleanedContactPerson.getPhone());
        assertEquals("Jan", cleanedContactPerson.getFirstName());
        assertEquals("Kowalski", cleanedContactPerson.getLastName());

    }

    @DisplayName("JUnit test for clean contactPerson method (no phone and email)")
    @Test
    public void givenContactPersonObjectNoPhoneAndEmail_whenClean_thenReturnCleanedContactPersonObject() {
        //given - precondition or setup
        ContactPerson contactPerson = ContactPerson.builder()
                .firstName("   Jan  ")
                .lastName(" Kowalski   ")
                .build();

        //when - action or the behavior that we are going to test
        ContactPerson cleanedContactPerson = inputCleaner.clean(contactPerson);

        //then - verify the output
        assertEquals("Jan", cleanedContactPerson.getFirstName());
        assertEquals("Kowalski", cleanedContactPerson.getLastName());

    }

    @DisplayName("JUnit test for clean contact method (all fields)")
    @Test
    public void givenContactObject_whenClean_thenReturnCleanedContactObject() {
        //given - precondition or setup
        Contact contact = Contact.builder()
                .title(" DK Chotomów  ")
                .description("   Opis  ")
                .build();

        //when - action or the behavior that we are going to test
        Contact cleanedContact = inputCleaner.clean(contact);


        //then - verify the output
        assertEquals("DK Chotomów", cleanedContact.getTitle());
        assertEquals("Opis", cleanedContact.getDescription());

    }


    @DisplayName("JUnit test for clean contact method (only title)")
    @Test
    public void givenContactObjectWithTitleOnly_whenTrim_thenReturnCleanedContactObject() {
        //given - precondition or setup
        Contact contact = Contact.builder()
                .title(" DK Chotomów  ")
                .build();

        //when - action or the behavior that we are going to test
        Contact cleanedContact = inputCleaner.clean(contact);

        //then - verify the output
        assertNotNull(cleanedContact);
        assertEquals("DK Chotomów", cleanedContact.getTitle());
    }

    @DisplayName("JUnit test for clean task method (only title)")
    @Test
    public void givenTaskObjectWithTitleOnly_whenTrim_thenReturnCleanedTaskObject() {
        //given - precondition or setup
        Task task = Task.builder()
                .title("  Title ")
                .priority(1).build();

        //when - action or the behavior that we are going to test
        Task cleanedTask = inputCleaner.clean(task);

        //then - verify the output
        assertNotNull(task);
        assertEquals("Title", task.getTitle());
    }

    @DisplayName("JUnit test for clean instrument method (only title)")
    @Test
    public void givenInstrument_whenTrim_thenReturnCleanedIntrumentObject() {
        //given - precondition or setup
        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("   Piano   ")
                .build();

        //when - action or the behavior that we are going to test
        Instrument cleanedInstrument = inputCleaner.clean(instrument);

        //then - verify the output
        assertNotNull(instrument);
        assertEquals("Piano", instrument.getName());
    }



}