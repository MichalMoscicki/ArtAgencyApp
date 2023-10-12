package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.InstrumentDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface InstrumentService {

    InstrumentDTO create(InstrumentDTO unsyncDTO);

    InstrumentDTO getById(Long id);

    List<InstrumentDTO> getAll();

    InstrumentDTO update(InstrumentDTO instrumentDTO);

    void deleteById(Long id);


}
