package com.immpresariat.ArtAgencyApp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.*;
import com.immpresariat.ArtAgencyApp.payload.SongDTO;
import com.immpresariat.ArtAgencyApp.repository.*;
import com.immpresariat.ArtAgencyApp.service.impl.SongServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SongControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    SongServiceImpl songService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        cleanDB();
    }

    @Test
    public void whenCreate_thenReturnDTOObject() throws Exception {
        //given - precondition or setup
        SongDTO unsyncDTO = createSongDTOWithoutParts(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/songs")
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
        Long songId = 0L;
        String message = String.format("No song with id: %s", songId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/songs/%s", songId)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void whenGetById_thenReturnSongDTOObject() throws Exception {
        //given - precondition or setup
        Song song = createSongWithoutParts();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/songs/%s", song.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(song.getTitle())));
    }

    @Test
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup


        Long id = 0L;
        SongDTO notExistingSong = createSongDTOWithoutParts(id);

        String message = String.format("No song with id: %s", id);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(patch(String.format("/api/v1/songs/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notExistingSong)));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void testUpdate() throws Exception {
        // Given
        Song song = createSongWithoutParts();
        SongDTO updatedSongDTO = createSongDTOWithoutParts(song.getId());
        updatedSongDTO.setTitle("Nowy tytuł");

        // When
        ResultActions response = mockMvc.perform(
                patch(String.format("/api/v1/songs/%s", song.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSongDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(updatedSongDTO.getId().intValue())))
                .andExpect(jsonPath("$.title", CoreMatchers.is(updatedSongDTO.getTitle())));

    }

    @Test
    public void testUpdateOnlyOneFieldInDTO() throws Exception {
        // Given
        Song song = createSongWithoutParts();
        SongDTO updatedSongDTO = SongDTO.builder()
                .id(song.getId())
                .title("Nowy Tytuł")
                .build();

        // When
        ResultActions response = mockMvc.perform(
                patch(String.format("/api/v1/songs/%s", song.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSongDTO))
        );

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(updatedSongDTO.getId().intValue())))
                .andExpect(jsonPath("$.title", CoreMatchers.is(updatedSongDTO.getTitle())))
                .andExpect(jsonPath("$.textAuthors", CoreMatchers.is(song.getTextAuthors())));

    }

    @Test
    public void givenId_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        String message = String.format("No song with id: %s", id);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/songs/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));

    }

        @Test
    public void givenSongWithoutParts_whenDelete_thenSongDeleted() throws Exception {
        //given - precondition or setup
        Song song = createSongWithoutParts();

        String message = "Successfully deleted song with id: " + song.getId();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/songs/%s", song.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

        Assertions.assertEquals(songRepository.findById(song.getId()), Optional.empty());

    }

    @Test
    public void givenSongWithParts_whenDelete_thenSongDeleted() throws Exception {
        //given - precondition or setup
        Song song = createSongWithParts();
        Part part = song.getParts().get(0);

        String message = "Successfully deleted song with id: " + song.getId();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/songs/%s", song.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));

        Assertions.assertEquals(songRepository.findById(song.getId()), Optional.empty());
        Assertions.assertEquals(partRepository.findById(part.getId()), Optional.empty());

    }

    private void cleanDB() {
        songRepository.deleteAll();
        partRepository.deleteAll();
    }

    private Song createSongWithParts() {

        Instrument instrument = instrumentRepository.save(new Instrument(null, "Bas"));

        Part part = partRepository.save(
                Part.builder()
                        .name("name")
                        .data(new byte[1])
                        .instrument(instrument)
                        .type("type")
                .build());

        List<Part> parts = new ArrayList<>();
        parts.add(part);


        Song song = Song.builder()
                .title("Sto Lat")
                .textAuthors("Jan Kowalski")
                .description("Super numer")
                .composers("Comosers")
                .parts(parts)
                .build();
        return songRepository.save(song);
    }

    private Song createSongWithoutParts() {
        Song song = Song.builder()
                .title("Sto Lat")
                .textAuthors("Jan Kowalski")
                .description("Super numer")
                .composers("Comosers")
                .build();
        return songRepository.save(song);
    }

    private SongDTO createSongDTOWithoutParts(Long id) {
        return SongDTO.builder()
                .id(id)
                .title("Sto Lat")
                .textAuthors("Jan Kowalski")
                .description("Super numer")
                .composers("Comosers")
                .build();
    }
}