package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Concert;
import com.immpresariat.ArtAgencyApp.models.ConcertDetails;
import com.immpresariat.ArtAgencyApp.payload.ConcertDetailsDTO;
import com.immpresariat.ArtAgencyApp.repository.ConcertDetailsRepository;
import com.immpresariat.ArtAgencyApp.repository.ConcertRepository;
import com.immpresariat.ArtAgencyApp.service.ConcertDetailsService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

@Service
public class ConcertDetailsServiceImpl implements ConcertDetailsService {

    private final ConcertDetailsRepository concertDetailsRepository;
    private final ConcertRepository concertRepository;
    private final DTOMapper dtoMapper;

    public ConcertDetailsServiceImpl(ConcertDetailsRepository concertDetailsRepository,
                                     ConcertRepository concertRepository,
                                     DTOMapper dtoMapper) {
        this.concertDetailsRepository = concertDetailsRepository;
        this.concertRepository = concertRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ConcertDetailsDTO create(ConcertDetailsDTO concertDetailsDTO, Long concertId) {
        ensureConcertExists(concertId);
        concertDetailsDTO.setConcertId(concertId);
        ConcertDetails concertDetails = concertDetailsRepository.save(dtoMapper.mapToEntity(concertDetailsDTO, concertRepository));
        return dtoMapper.mapToDTO(concertDetails);
    }

    @Override
    public ConcertDetailsDTO getById(Long id) {
        return dtoMapper.mapToDTO(ensureDetailsExists(id));
    }

    @Override
    public ConcertDetailsDTO update(ConcertDetailsDTO concertDetailsDTO) {
        ensureDetailsExists(concertDetailsDTO.getId());
        ConcertDetails concertDetails = concertDetailsRepository.save(dtoMapper.mapToEntity(concertDetailsDTO, concertRepository));
        return dtoMapper.mapToDTO(concertDetails);
    }

    @Override
    public void deleteById(Long id) {
        concertDetailsRepository.deleteById(id);
    }

    Concert ensureConcertExists(Long concertId){
        return concertRepository.findById(concertId).orElseThrow( () ->
                new ResourceNotFoundException("No concert with id: " + concertId));
    }

    ConcertDetails ensureDetailsExists(Long id){
        return concertDetailsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No concert details with id: " + id));
    }
}
