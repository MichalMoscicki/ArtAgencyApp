package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.payload.InstrumentDTO;
import com.immpresariat.ArtAgencyApp.repository.InstrumentRepository;
import com.immpresariat.ArtAgencyApp.service.impl.InstrumentServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class InstrumentControllerITest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    InstrumentServiceImpl instrumentService;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        cleanDB();
    }


    @Test
    public void whenCreate_thenReturnDTOObject() throws Exception {
        //given - precondition or setup
        InstrumentDTO unsyncDTO = createSampleInstrumentDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/instruments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }


    @Test
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long instrumentId = 0L;
        String message = String.format("No instrument with id: %s", instrumentId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/instruments/%s", instrumentId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    public void whenGetById_thenReturnEventDTOObject() throws Exception {
        //given - precondition or setup
        Instrument instrument = createSampleInstrument();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/instruments/%s", instrument.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(instrument.getName())));
    }


    @Test
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup


        Long instrumentId = 0L;
        InstrumentDTO notExistingInstrument = createSampleInstrumentDTO(instrumentId);

        String message = String.format("No instrument with id: %s", instrumentId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/instruments/%s", instrumentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notExistingInstrument)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    public void testUpdateEvent() throws Exception {
        // Given
        Instrument instrument = createSampleInstrument();
        InstrumentDTO updatedInstrumentDTO = createSampleInstrumentDTO(instrument.getId());
        updatedInstrumentDTO.setName("Kontrabas");

        // When
        ResultActions response = mockMvc.perform(
                put(String.format("/api/v1/instruments/%s", instrument.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInstrumentDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedInstrumentDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedInstrumentDTO.getName())));

    }


    @Test
    public void whenDelete_thenEventDeleted() throws Exception {
        //given - precondition or setup
        Instrument instrument = createSampleInstrument();
        Long id = instrument.getId();

        String message = "Successfully deleted instrument with id: " + id;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/instruments/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

    }


    private void cleanDB() {
        instrumentRepository.deleteAll();
    }

    private Instrument createSampleInstrument() {
        Instrument instrument = Instrument.builder()
                .name("Fortepian")
                .build();
        return instrumentRepository.save(instrument);
    }

    private InstrumentDTO createSampleInstrumentDTO(Long id) {
        return InstrumentDTO.builder()
                .id(id)
                .name("Fortepian")
                .build();
    }
}
