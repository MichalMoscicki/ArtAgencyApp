package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Concert;
import com.immpresariat.ArtAgencyApp.payload.ConcertDTO;
import com.immpresariat.ArtAgencyApp.repository.*;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ConcertControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        concertRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenReturnConcertDTOObject() throws Exception {
        //given - precondition or setup
        ConcertDTO concertDTO = createConcertDTO(null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/concerts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(concertDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.title", CoreMatchers.notNullValue()));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetAll_thenReturnConcertObjectsList() throws Exception {
        //given - precondition or setup
        Concert concert1 = createConcert();
        Concert concert2 = createConcert();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/concerts"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(2)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/concerts/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is("No concert with id: " + id)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenReturnConcertDTOObject() throws Exception {
        //given - precondition or setup
        Concert concert = createConcert();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/concerts/%s", concert.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(concert.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;
        ConcertDTO concertDTO = createConcertDTO(id);
        String message = "No concert with id: " + id;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(patch("/api/v1/concerts/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(concertDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenTitleOnly_whenUpdate_thenReturnContactDTO() throws Exception {
        //given - precondition or setup
        Concert concert = createConcert();

        ConcertDTO concertDTO = ConcertDTO.builder()
                .title("Nowy tytuł")
                .build();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(patch("/api/v1/concerts/" + concert.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(concertDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", CoreMatchers.is(concertDTO.getTitle())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenId_whenDelete_thenConcertDeleted() throws Exception {
        //given - precondition or setup
        Concert concert = createConcert();
        Long concertId = concert.getId();
        String message = "Successfully deleted concert with id: " + concertId;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/concerts/" + concertId)
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));
    }

    private ConcertDTO createConcertDTO(Long id) {
        return ConcertDTO.builder()
                .id(id)
                .title("Tytuł")
                .date(LocalDate.now())
                .organizer(null)
                .songs(new ArrayList<>())
                .musicians(new ArrayList<>())
                .build();
    }

    private Concert createConcert() {
        Concert concert = Concert.builder()
                .title("Tytuł")
                .date(LocalDate.now())
                .organizer(null)
                .songs(new ArrayList<>())
                .musicians(new ArrayList<>())
                .build();

        return concertRepository.save(concert);
    }


}
