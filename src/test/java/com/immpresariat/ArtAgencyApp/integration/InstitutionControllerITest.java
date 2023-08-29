package com.immpresariat.ArtAgencyApp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class InstitutionControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    InstitutionService institutionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        List<Contact> contacts = contactRepository.findAll();
        for (Contact contact : contacts) {
            contact.setInstitutions(new ArrayList<>());
            contactRepository.save(contact);
        }
        institutionRepository.deleteAll();
    }

    @Test
    public void whenCreate_thenInstitutionAddedToContact() throws Exception {
        //given - precondition or setup
        Contact contact = contactRepository.save(new Contact());
        Long contactId = contact.getId();

        InstitutionDTO unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/institutions", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncInstitutionDTO)));


        //then - verify the output
        Contact updatedContact = contactRepository.findById(contactId).get();
        assertEquals(updatedContact.getInstitutions().size(), 1);

    }

    @Test
    public void whenCreate_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = contactRepository.save(new Contact());
        Long contactId = contact.getId();

        InstitutionDTO unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/institutions", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncInstitutionDTO)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }

    @Test
    public void whenGetById_thenThrowError() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        String message = String.format("No institution with id: %s", id);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", id)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void whenGetById_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = contactRepository.save(new Contact());
        Long contactId = contact.getId();

        InstitutionDTO unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();

       InstitutionDTO syncDTO = institutionService.create(unsyncInstitutionDTO, contactId);


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/institutions/%s", syncDTO.getId())));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(syncDTO.getName())));
    }

    //todo napisać update servis
    @Test
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0l;
        String message = String.format("No institution with id: %s", id);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/institutions/%s", id)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    //todo tę tęst
    @Test
    public void whenUpdate_thenReturnUpdatedInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = contactRepository.save(new Contact());
        Long contactId = contact.getId();

        Institution unsyncInstitution = Institution.builder()
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();
        Institution syncInstitution = institutionRepository.save(unsyncInstitution);
        List<Institution> institutions = new ArrayList<>();
        institutions.add(syncInstitution);
        contact.setInstitutions(institutions);
        contactRepository.save(contact);

        Long id = syncInstitution.getId();

        InstitutionDTO updatedInstitution = InstitutionDTO.builder()
                .id(id)
                .name("Dk Chotomów")
                .city("Chotomów")
                .alreadyCooperated(false)
                .category("DK")
                .notes("Są notatki")
                .build();


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/institutions/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInstitution)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(jsonPath("$.name", CoreMatchers.is(updatedInstitution.getName())))
                .andExpect(jsonPath("$.city", CoreMatchers.is(updatedInstitution.getCity())))
                .andExpect(jsonPath("$.alreadyCooperated", CoreMatchers.is(updatedInstitution.isAlreadyCooperated())))
                .andExpect(jsonPath("$.category", CoreMatchers.is(updatedInstitution.getCategory())))
                .andExpect(jsonPath("$.notes", CoreMatchers.is(updatedInstitution.getNotes())));
    }

    //todo rozkminić jak wyrzucać również z kontaktu tę instytucję
    @Test
    public void whenDeleteById_thenInstitutionDeleted() throws Exception {
        //given - precondition or setup
        Contact contact = contactRepository.save(new Contact());
        Long contactId = contact.getId();

        InstitutionDTO unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("Dk Głogów")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .notes("")
                .build();

        InstitutionDTO syncDTO = institutionService.create(unsyncInstitutionDTO, contactId);


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/institutions/%s", syncDTO.getId())));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.name", CoreMatchers.is(syncDTO.getName())));
    }


    //TODO jak wszystkie testy przejdą, zrób solidny refactor;

}
