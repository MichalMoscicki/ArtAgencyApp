package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @BeforeEach
    public void setup() {
        contactRepository.deleteAll();
    }


    @Test
    public void whenCreate_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/contacts")
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }

    @Test
    public void whenGetAll_thenReturnContactObjectsList() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact();
        Contact contact2 = createSampleContact();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @Test
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is("No contact with id: " + id)));
    }

    @Test
    public void whenGetById_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        int id = Math.toIntExact(contact.getId());

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id)));
    }

    @Test
    public void givenContactWithAssociatedData_whenDelete_thenContactDeleted() throws Exception {
        //given - precondition or setup
        Contact contact = createContactWithAssociatedData();
        Long contactId = contact.getId();
        String message = "Successfully deleted contact with id: " + contactId;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/contacts/" + contactId)
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));
    }


    private Contact createSampleContact() {
        return contactRepository.save(new Contact());
    }

    private Contact createContactWithAssociatedData() {
        Event event = eventRepository.save(
                Event.builder()
                        .name("Melanż")
                        .description("Dobry")
                        .monthWhenOrganized(1).build()
        );
        List<Event> eventsList = new ArrayList<>();
        eventsList.add(event);


        ContactPerson contactPerson = contactPersonRepository.save(
                ContactPerson.builder()
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .role("dyrektor")
                        .email("jan@gmail.com")
                        .phone("+48111222333")
                        .build()
        );
        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson);

        Institution institution = institutionRepository.save(
                Institution.builder()
                        .name("Dk Głogów")
                        .city("Głogów")
                        .notes("SuperMiasto")
                        .alreadyCooperated(true)
                        .category("Dk")
                        .build()
        );
        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);

        Contact contact = createSampleContact();
        contact.setContactPeople(contactPeople);
        contact.setInstitutions(institutions);
        contact.setEvents(eventsList);

        return contactRepository.save(contact);

    }
}