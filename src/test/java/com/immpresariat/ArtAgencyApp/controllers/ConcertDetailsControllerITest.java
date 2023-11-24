package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Concert;
import com.immpresariat.ArtAgencyApp.models.ConcertDetails;
import com.immpresariat.ArtAgencyApp.payload.ConcertDetailsDTO;
import com.immpresariat.ArtAgencyApp.repository.ConcertDetailsRepository;
import com.immpresariat.ArtAgencyApp.repository.ConcertRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ConcertDetailsControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConcertDetailsRepository concertDetailsRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        concertDetailsRepository.deleteAll();
        concertRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenCreate_thenReturnDetailsDTOObject() throws Exception {
        //given - precondition or setup
        Concert concert = createConcert();
        ConcertDetailsDTO detailsDTO = createConcertDetailsDTO(null, null);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/concerts/%s/details", concert.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detailsDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.concertId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.address", CoreMatchers.is(detailsDTO.getAddress())))
                .andExpect(jsonPath("$.end", CoreMatchers.is(detailsDTO.getEnd().toString())))
                .andExpect(jsonPath("$.start", CoreMatchers.is(detailsDTO.getStart().toString())));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long concertId = 0L;
        Long detailsId = 0L;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/concerts/%s/details/%s", concertId, detailsId)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is("No concert details with id: " + concertId)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenGetById_thenReturnDetailsDTOObject() throws Exception {
        //given - precondition or setup
        ConcertDetails concertDetails = createDetails();

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/concerts/%s/details/%s", concertDetails.getConcert(), concertDetails.getId())));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(concertDetails.getId().intValue())))
                .andExpect(jsonPath("$.concertId", CoreMatchers.is(concertDetails.getConcert().getId().intValue())))
                .andExpect(jsonPath("$.address", CoreMatchers.is(concertDetails.getAddress())))
                .andExpect(jsonPath("$.end", CoreMatchers.is(concertDetails.getEnd().toString())))
                .andExpect(jsonPath("$.start", CoreMatchers.is(concertDetails.getStart().toString())));

    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void whenUpdate_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long concertId = 0L;
        Long detailsId = 0L;
        String message = "No concert details with id: " + detailsId;
        ConcertDetailsDTO detailsDTO = createConcertDetailsDTO(detailsId, concertId);

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/concerts/%s/details/%s",concertId, detailsId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detailsDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void _whenUpdate_thenReturnUpdatedDTO() throws Exception {
        //given - precondition or setup
        ConcertDetails concertDetails = createDetails();
        ConcertDetailsDTO detailsDTO = createConcertDetailsDTO(concertDetails.getId(), concertDetails.getConcert().getId());
        detailsDTO.setAddress("New Address");

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/concerts/%s/details/%s",concertDetails.getConcert().getId(), concertDetails.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detailsDTO)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", CoreMatchers.is("New Address")));
    }

    @Test
    @WithMockUser(username = "testuser@test.com", roles = "USER")
    public void givenId_whenDelete_thenConcertDeleted() throws Exception {
        //given - precondition or setup
        ConcertDetails concertDetails = createDetails();
        Long detailsId = concertDetails.getId();
        String message = "Successfully deleted concertDetails with id: " + detailsId;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/concerts/%s/details/%s",concertDetails.getConcert().getId(), detailsId))
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));
    }

    private Concert createConcert() {
        Concert concert = Concert.builder()
                .title("Tytuł")
                .address("Address")
                .date(LocalDate.now())
                .organizer(null)
                .songs(new ArrayList<>())
                .musicians(new ArrayList<>())
                .build();
        return concertRepository.save(concert);
    }
    public ConcertDetails createDetails(){
        Concert concert = createConcert();

        ConcertDetails concertDetails = ConcertDetails.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .address("Strażacka 5, Dom Kultury Aleksandrów")
                .concert(concert)
                .build();

        return concertDetailsRepository.save(concertDetails);
    }
    private ConcertDetailsDTO createConcertDetailsDTO(Long detailsId, Long concertId) {
        return ConcertDetailsDTO.builder()
                .id(detailsId)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .address("Strażacka 5, Dom Kultury Aleksandrów")
                .concertId(concertId)
                .build();
    }
}
