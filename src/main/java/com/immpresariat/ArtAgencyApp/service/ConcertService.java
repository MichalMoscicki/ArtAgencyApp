package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.ConcertDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import org.springframework.stereotype.Service;

@Service
public interface ConcertService {

    ConcertDTO create(ConcertDTO concertDTO);
    ConcertDTO getById(Long id);
    PageResponse<ConcertDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    ConcertDTO update(ConcertDTO concertDTO, Long concertId);
    void deleteById(Long id);

}
