package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.payload.PartDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PartService {

    PartDTO store(MultipartFile file, Instrument instrument, Long songId) throws IOException;
    Part getById(Long id);
    void deleteById(Long partId, Long songId);

}
