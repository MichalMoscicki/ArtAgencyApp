package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        contactRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        ContactDTO unsyncContactDTO = ContactDTO.builder()
                .title("Opener")
                .alreadyCooperated(false)
                .build();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncContactDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.title", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.alreadyCooperated", CoreMatchers.is(unsyncContactDTO.isAlreadyCooperated())))
                .andExpect(jsonPath("$.updated", CoreMatchers.notNullValue()));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetAll_thenReturnContactObjectsList() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact("Opener Festival");
        Contact contact2 = createSampleContact("Opener Festival");

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(2)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenTwelveContactsObject_whenGetAll_thenReturnTenContactObjectsList() throws Exception {
        //given - precondition or setup
        for (int i = 0; i <= 11; i++) {
            createSampleContact("Opener Festival");
        }

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(10)))
                .andExpect(jsonPath("$.totalElements", CoreMatchers.is(12)))
                .andExpect(jsonPath("$.last", CoreMatchers.is(false)))
                .andExpect(jsonPath("$.pageSize", CoreMatchers.is(10)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenThreeContactsObject_whenGetAllDesc_thenReturnContactsInCorrectOrder() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact("First");
        Contact contact2 = createSampleContact("Second");
        Contact contact3 = createSampleContact("Third");
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", CoreMatchers.is(contact3.getTitle())))
                .andExpect(jsonPath("$.content[1].title", CoreMatchers.is(contact2.getTitle())))
                .andExpect(jsonPath("$.content[2].title", CoreMatchers.is(contact1.getTitle())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenThreeContactsObject_whenGetAllAsc_thenReturnContactsInCorrectOrder() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact("First");
        Contact contact2 = createSampleContact("Second");
        Contact contact3 = createSampleContact("Third");
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts?sortDir=asc"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", CoreMatchers.is(contact1.getTitle())))
                .andExpect(jsonPath("$.content[1].title", CoreMatchers.is(contact2.getTitle())))
                .andExpect(jsonPath("$.content[2].title", CoreMatchers.is(contact3.getTitle())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenThreeContactsObject_whenGetAllSortByName_thenReturnContactsInCorrectOrder() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact("First");
        Contact contact2 = createSampleContact("Second");
        Contact contact3 = createSampleContact("Third");
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts?sortBy=title"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", CoreMatchers.is(contact3.getTitle())))
                .andExpect(jsonPath("$.content[1].title", CoreMatchers.is(contact2.getTitle())))
                .andExpect(jsonPath("$.content[2].title", CoreMatchers.is(contact1.getTitle())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenThreeContactsObject_whenGetAllSortByNameAsc_thenReturnContactsInCorrectOrder() throws Exception {
        //given - precondition or setup
        Contact contact1 = createSampleContact("First");
        Contact contact2 = createSampleContact("Second");
        Contact contact3 = createSampleContact("Third");
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/contacts?sortBy=title&sortDir=asc"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", CoreMatchers.is(contact1.getTitle())))
                .andExpect(jsonPath("$.content[1].title", CoreMatchers.is(contact2.getTitle())))
                .andExpect(jsonPath("$.content[2].title", CoreMatchers.is(contact3.getTitle())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
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
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact("Opener Festival");
        int id = Math.toIntExact(contact.getId());

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        ContactDTO syncContactDTO = ContactDTO.builder()
                .id(0L)
                .title("Opener")
                .alreadyCooperated(false)
                .build();
        String message = "No contact with id: " + 0;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/contacts/" + 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(syncContactDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenReturnContactDTO() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact("Opener Festival");

        ContactDTO syncContactDTO = ContactDTO.builder()
                .id(contact.getId())
                .title("Opener")
                .alreadyCooperated(false)
                .build();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/contacts/" + contact.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(syncContactDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(syncContactDTO.getTitle())))
                .andExpect(jsonPath("$.alreadyCooperated", CoreMatchers.is(false)))
                .andExpect(jsonPath("$.updated", CoreMatchers.not(contact.getUpdated())));

    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
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

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "ADMIN")
    public void givenContactList_whenExport_thenStatusOk() throws Exception {
        Contact contact = createSampleContact("SampleContact");

        mockMvc.perform(get("/api/v1/contacts/export-json"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=businessContacts.json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    //todo how to do negative scenario?


//    @Test
//    public void givenNoFile_whenImport_thenReturnError() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "".getBytes());
//
//        ResultActions response = mockMvc.perform(
//                multipart("/api/v1/contacts/import")
//                        .file(file)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//        );
//
//        response.andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(content().string(containsString("File not provided")));
//    }
    //todo test importowania: brak pliku, plik bez jsonów, poprawny plik z różnymi rodzajami danych


    private Contact createSampleContact(String name) {
        Contact contact = Contact.builder()
                .title(name)
                .updated(new Date())
                .alreadyCooperated(true)
                .build();

        return contactRepository.save(contact);
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
                        .category("Dk")
                        .build()
        );
        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);

        Contact contact = createSampleContact("Opener Festival");
        contact.setContactPeople(contactPeople);
        contact.setInstitutions(institutions);
        contact.setEvents(eventsList);

        return contactRepository.save(contact);

    }
}
