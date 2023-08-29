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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
        cleanDB();
    }

    @Test
    public void whenCreate_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        InstitutionDTO unsyncInstitutionDTO = createSampleInstitutionDTO(null);

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
    public void whenCreate_thenInstitutionAddedToContact() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        InstitutionDTO unsyncInstitutionDTO = createSampleInstitutionDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/contacts/%s/institutions", contactId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncInstitutionDTO)));

        //then - verify the output
        Contact updatedContact = contactRepository.findById(contactId).get();
        assertEquals(updatedContact.getInstitutions().size(), 1);

    }


        @Test
        public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
            //given - precondition or setup
            Contact contact = createSampleContact();
            Long contactId = contact.getId();
            Long institutionId = 0L;
            String message = String.format("No institution with id: %s", institutionId);

            //when - action or the behavior that we are going to test
            ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId)));


            //then - verify the output
            response.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
        }


    @Test
    public void whenGetById_thenReturnInstitutionDTOObject() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        InstitutionDTO syncDTO = institutionService.create(createSampleInstitutionDTO(null), contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/contacts/%s/institutions/%s", contactId, syncDTO.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(syncDTO.getName())));
    }


   @Test
   public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
       //given - precondition or setup
       Contact contact = createSampleContact();
       Long contactId = contact.getId();

       Long institutionId = 0L;
       InstitutionDTO notExistingInstitution = createSampleInstitutionDTO(institutionId);

       String message = String.format("No institution with id: %s", institutionId);

       //when - action or the behavior that we are going to test
       ResultActions response = mockMvc.perform(put(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(notExistingInstitution)));


       //then - verify the output
       response.andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
   }

       @Test
       public void testUpdateInstitution() throws Exception {
           // Given
           Contact contact = createSampleContact();
           Institution unsyncInstitution = createSampleInstitution("Dk Głogów", "Głogów", true, "DK", "");
           Institution syncInstitution = institutionRepository.save(unsyncInstitution);
           contact.setInstitutions(Collections.singletonList(syncInstitution));
           contactRepository.save(contact);

           Long contactId = contact.getId();
           Long institutionId = syncInstitution.getId();
           InstitutionDTO updatedInstitutionDTO = createSampleInstitutionDTO(institutionId);

           // When
           ResultActions response = mockMvc.perform(
                   put(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId))
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(updatedInstitutionDTO))
           );

           // Then
           response.andDo(print())
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.id", is(institutionId.intValue())))
                   .andExpect(jsonPath("$.name", is(updatedInstitutionDTO.getName())))
                   .andExpect(jsonPath("$.city", is(updatedInstitutionDTO.getCity())))
                   .andExpect(jsonPath("$.alreadyCooperated", is(updatedInstitutionDTO.isAlreadyCooperated())))
                   .andExpect(jsonPath("$.category", is(updatedInstitutionDTO.getCategory())))
                   .andExpect(jsonPath("$.notes", is(updatedInstitutionDTO.getNotes())));
       }

           @Test
           public void whenDelete_thenInstitutionDeleted() throws Exception {
               //given - precondition or setup
               Contact contact = createSampleContact();
               Long contactId = contact.getId();

               InstitutionDTO syncDTO = institutionService.create(createSampleInstitutionDTO(null), contactId);
               Long institutionId = syncDTO.getId();

               String message = "Successfully deleted institution with id: " + institutionId;

               //when - action or the behavior that we are going to test
               ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId)));

               //then - verify the output
               response.andDo(print())
                       .andExpect(status().isOk())
                       .andExpect(jsonPath("$", CoreMatchers.is(message)));

           }


    @Test
    public void givenNoContact_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long contactId = 0L;
        Long institutionId = 1L;
        String message = String.format("No contact with id: %s", contactId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenNoInstitution_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Contact contact = createSampleContact();
        Long contactId = contact.getId();
        Long institutionId = 0L;
        String message = String.format("No institution with id: %s", institutionId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/contacts/%s/institutions/%s", contactId, institutionId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    private Contact createSampleContact() {
        return contactRepository.save(new Contact());
    }

    private Institution createSampleInstitution(String name, String city, boolean alreadyCooperated, String category, String notes) {
        return Institution.builder()
                .name(name)
                .city(city)
                .alreadyCooperated(alreadyCooperated)
                .category(category)
                .notes(notes)
                .build();
    }

    private InstitutionDTO createSampleInstitutionDTO(Long institutionId) {
        return InstitutionDTO.builder()
                .id(institutionId)
                .name("Dk Chotomów")
                .city("Chotomów")
                .alreadyCooperated(false)
                .category("DK")
                .notes("Są notatki")
                .build();
    }

    private void cleanDB() {
        List<Contact> contacts = contactRepository.findAll();
        for (Contact contact : contacts) {
            contact.setInstitutions(new ArrayList<>());
            contactRepository.save(contact);
        }
        institutionRepository.deleteAll();
    }

}
