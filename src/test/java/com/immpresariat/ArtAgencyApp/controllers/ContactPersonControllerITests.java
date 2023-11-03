package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactPersonControllerITests {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContactPersonRepository contactPersonRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    ContactPersonService contactPersonService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        cleanDB();
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenReturnContactPersonDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        ContactPersonDTO unsyncContactPersonDTO = createSampleContactPersonDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/contact-people", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncContactPersonDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenContactPersonAddedToContact() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Date updated = contact.getUpdated();
        ContactPersonDTO unsyncContactPersonDTO = createSampleContactPersonDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/contact-people", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncContactPersonDTO)));

        //then - verify the output
        Contact updatedContact = contactRepository.findById(contactId).get();
        assertEquals(updatedContact.getContactPeople().size(), 1);
        assertNotEquals(updated, updatedContact.getUpdated());

    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Long contactPersonId = 0L;
        String message = String.format("No contactPerson with id: %s", contactPersonId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenReturnContactPersonDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        ContactPersonDTO syncDTO = contactPersonService.create(createSampleContactPersonDTO(null), contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, syncDTO.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(syncDTO.getFirstName())));
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();

        Long contactPersonId = 0L;
        ContactPersonDTO notExistingContactPerson = createSampleContactPersonDTO(contactPersonId);

        String message = String.format("No contactPerson with id: %s", contactPersonId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notExistingContactPerson)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void testUpdateContactPerson() throws Exception {
        // Given
        Contact contact = createSampleContact();
        ContactPerson unsyncContactPerson = createSampleContactPerson("Olof", "Palme", "+48111222333", "olof.palme@gmail.com", "dyrektor");
        ContactPerson syncContactPerson = contactPersonRepository.save(unsyncContactPerson);
        contact.setContactPeople(Collections.singletonList(syncContactPerson));
        contactRepository.save(contact);

        Long contactId = contact.getId();
        Long contactPersonId = syncContactPerson.getId();
        ContactPersonDTO updatedContactPersonDTO = createSampleContactPersonDTO(contactPersonId);
        Date updated = contact.getUpdated();

        // When
        ResultActions response = mockMvc.perform(
                put(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContactPersonDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(contactPersonId.intValue())))
                .andExpect(jsonPath("$.firstName", is(updatedContactPersonDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedContactPersonDTO.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedContactPersonDTO.getEmail())))
                .andExpect(jsonPath("$.phone", is(updatedContactPersonDTO.getPhone())))
                .andExpect(jsonPath("$.role", is(updatedContactPersonDTO.getRole())));

        Contact updatedContact = contactRepository.findById(contactId).get();
        assertNotEquals(updated, updatedContact.getUpdated());
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenDelete_thenContactPersonDeleted() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();

        ContactPersonDTO syncDTO = contactPersonService.create(createSampleContactPersonDTO(null), contactId);
        Long contactPersonId = syncDTO.getId();

        String message = "Successfully deleted contactPerson with id: " + contactPersonId;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenNoContact_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long contactId = 0L;
        Long contactPersonId = 1L;
        String message = String.format("No contact with id: %s", contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenNoContactPerson_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Long contactPersonId = 1L;
        String message = String.format("No contactPerson with id: %s", contactPersonId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/contact-people/%s", contactId, contactPersonId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    private void cleanDB() {
        List<Contact> contacts = contactRepository.findAll();
        for (Contact contact : contacts) {
            contact.setContactPeople(new ArrayList<>());
            contactRepository.save(contact);
        }
        contactPersonRepository.deleteAll();
    }

    private Contact createSampleContact() {
        Contact contact = Contact.builder()
                .title("Opener Festival")
                .alreadyCooperated(true)
                .updated(new Date())
                .build();

        return contactRepository.save(contact);
    }

    private ContactPersonDTO createSampleContactPersonDTO(Long contactPersonId) {
        return ContactPersonDTO.builder()
                .id(contactPersonId)
                .firstName("Jan")
                .lastName("Kowalski")
                .phone("+48791272305")
                .email("jan.kowalski@gmail.com")
                .role("dyrektor")
                .build();
    }

    private ContactPerson createSampleContactPerson(String firstName, String lastName, String phone, String email, String role) {
        return ContactPerson.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .email(email)
                .role(role)
                .build();
    }

}
