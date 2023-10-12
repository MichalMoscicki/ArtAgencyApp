package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.MusicianDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import org.springframework.stereotype.Service;

@Service
public interface MusicianService {

    MusicianDTO create(MusicianDTO musicianDTO);
    PageResponse<MusicianDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir, String instrument);
    MusicianDTO getById(Long id);
    MusicianDTO update(MusicianDTO musicianDTO);
    void deleteById(Long id);

}
