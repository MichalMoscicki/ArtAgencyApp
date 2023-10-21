package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.MusicianDTO;
import com.immpresariat.ArtAgencyApp.repository.*;
import com.immpresariat.ArtAgencyApp.service.impl.MusicianServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MusicianControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MusicianRepository musicianRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    MusicianServiceImpl musicianService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        cleanDB();
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenReturnDTOObject() throws Exception {
        //given - precondition or setup
        MusicianDTO unsyncDTO = createMusicianDTOWithInstrument(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/musicians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long instrumentId = 0L;
        String message = String.format("No musician with id: %s", instrumentId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/musicians/%s", instrumentId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
     @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Musician musician = createMusicianWithInstrument();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/musicians/%s", musician.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(musician.getFirstName())));
    }

    @Test
     @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup


        Long id = 0L;
        MusicianDTO notExistingMusician = createMusicianDTOWithInstrument(id);

        String message = String.format("No musician with id: %s", id);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/musicians/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notExistingMusician)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void testUpdateEvent() throws Exception {
        // Given
        Musician musician = createMusicianWithInstrument();
        MusicianDTO updatedMusicianDTO = createMusicianDTOWithInstrument(musician.getId());
        updatedMusicianDTO.setFirstName("Karol");

        // When
        ResultActions response = mockMvc.perform(
                put(String.format("/api/v1/musicians/%s", musician.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMusicianDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(updatedMusicianDTO.getId().intValue())))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedMusicianDTO.getFirstName().trim())));

    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenDelete_thenEventDeleted() throws Exception {
        //given - precondition or setup
        Musician musician = createMusicianWithInstrument();
        Instrument instrument = musician.getInstruments().get(0);

        String message = "Successfully deleted musician with id: " + musician.getId();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/musicians/%s", musician.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

        Assertions.assertEquals(musicianRepository.findById(musician.getId()), Optional.empty());
        Assertions.assertNotNull(instrumentRepository.findById(instrument.getId()));

    }


    private void cleanDB() {
        musicianRepository.deleteAll();
        instrumentRepository.deleteAll();
    }

    private Musician createMusicianWithInstrument() {

        Instrument instrument = instrumentRepository.save(
                Instrument.builder()
                        .name("Fortepian")
                        .build()
        );
        List<Instrument> instruments = new ArrayList<>();
        instruments.add(instrument);


        Musician musician = Musician.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48111222333")
                .notes("Notatki")
                .instruments(instruments)
                .build();


        return musicianRepository.save(musician);
    }


    private MusicianDTO createMusicianDTOWithInstrument(Long id) {

        Instrument instrument = instrumentRepository.save(
                Instrument.builder()
                        .name("Fortepian")
                        .build()
        );
        List<Instrument> instruments = new ArrayList<>();
        instruments.add(instrument);


        return MusicianDTO.builder()
                .id(id)
                .firstName(" Jan ")
                .lastName(" Kowalski ")
                .email(" jan@kowalski.pl ")
                .phone(" +48111222333 ")
                .notes(" Notatki ")
                .instruments(instruments)
                .build();
    }
}
