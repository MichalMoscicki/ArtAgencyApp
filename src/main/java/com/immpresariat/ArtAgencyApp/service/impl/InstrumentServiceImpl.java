package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.payload.InstrumentDTO;
import com.immpresariat.ArtAgencyApp.repository.InstrumentRepository;
import com.immpresariat.ArtAgencyApp.service.InstrumentService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

    private final InputCleaner inputCleaner;

    private final DTOMapper dtoMapper;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository, InputCleaner inputCleaner, DTOMapper dtoMapper) {
        this.instrumentRepository = instrumentRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public InstrumentDTO create(InstrumentDTO unsyncDTO) {
        if(instrumentRepository.existsByName(unsyncDTO.getName().trim().toLowerCase())){
            throw new ResourceAlreadyExistsException(String.format("Instrument with name: %s already exists!", unsyncDTO.getName()));
        }
        Instrument instrument = dtoMapper.mapToEntity(unsyncDTO);
        return dtoMapper.mapToDTO(instrumentRepository.save(inputCleaner.clean(instrument)));
    }

    @Override
    public InstrumentDTO getById(Long id) {
        Instrument instrument = ensureInstrumentExists(id);
        return dtoMapper.mapToDTO(instrument);
    }

    @Override
    public List<InstrumentDTO> getAll() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream().map(dtoMapper::mapToDTO).toList();
    }

    @Override
    public InstrumentDTO update(InstrumentDTO instrumentDTO) {
        ensureInstrumentExists(instrumentDTO.getId());
        Instrument instrument = dtoMapper.mapToEntity(instrumentDTO);
        return dtoMapper.mapToDTO(instrumentRepository.save(inputCleaner.clean(instrument)));
    }

    @Override
    public void deleteById(Long id) {
        instrumentRepository.deleteById(id);
    }

    private Instrument ensureInstrumentExists(Long id) {
        Optional<Instrument> instrumentOptional = instrumentRepository.findById(id);
        if (instrumentOptional.isEmpty()) {
            throw new ResourceNotFoundException("No instrument with id: " + id);
        }
        return instrumentOptional.get();
    }
}
