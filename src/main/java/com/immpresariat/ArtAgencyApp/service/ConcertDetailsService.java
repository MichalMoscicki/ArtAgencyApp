package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ConcertDetailsDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConcertDetailsService {

    ConcertDetailsDTO create(ConcertDetailsDTO concertDetailsDTO, Long concertId);
    ConcertDetailsDTO getById(Long id);
    ConcertDetailsDTO update(ConcertDetailsDTO concertDetailsDTO);
    void deleteById(Long id);
}
