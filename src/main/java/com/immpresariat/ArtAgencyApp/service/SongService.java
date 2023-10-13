package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.SongDTO;
import org.springframework.stereotype.Service;

@Service
public interface SongService {

    SongDTO create(SongDTO songDTO);
    PageResponse<SongDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir, String title);
    SongDTO getById(Long id);
    SongDTO update(SongDTO songDTO);
    SongDTO deleteById(Long id);


}
