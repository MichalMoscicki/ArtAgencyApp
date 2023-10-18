package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Concert;
import com.immpresariat.ArtAgencyApp.payload.ConcertDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.repository.ConcertRepository;
import com.immpresariat.ArtAgencyApp.service.ConcertService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final DTOMapper dtoMapper;
    private final InputCleaner inputCleaner;

    public ConcertServiceImpl(ConcertRepository concertRepository, DTOMapper dtoMapper, InputCleaner inputCleaner) {
        this.concertRepository = concertRepository;
        this.dtoMapper = dtoMapper;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public ConcertDTO create(ConcertDTO concertDTO) {
        Concert concert = inputCleaner.clean(dtoMapper.mapToEntity(concertDTO));
        return dtoMapper.mapToDTO(concertRepository.save(concert));
    }

    @Override
    public ConcertDTO getById(Long id) {
        return dtoMapper.mapToDTO(ensureConcertExists(id));
    }

    @Override
    public PageResponse<ConcertDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Concert> page = concertRepository.findAll(pageable);
        List<Concert> concerts = page.getContent();
        List<ConcertDTO> content = concerts.stream().map(dtoMapper::mapToDTO).toList();
        return PageResponse.createResponse(page, content);
    }

    @Override
    public ConcertDTO update(ConcertDTO concertDTO, Long concertId) {
        Concert concertDB = ensureConcertExists(concertId);
        setNotUpdatedFields(concertDTO, concertDB);
        return dtoMapper.mapToDTO(concertRepository.save(concertDB));
    }

    @Override
    public void deleteById(Long id) {
        concertRepository.deleteById(id);

    }

    private Concert ensureConcertExists(Long id){
        return concertRepository.findById(id).orElseThrow( () -> {
           throw new ResourceNotFoundException("No concert with id: " + id);
        });
    }

    private void setNotUpdatedFields(ConcertDTO concertDTO, Concert concertDB){
        if(concertDTO.getId() != null){
            concertDB.setId(concertDTO.getId());
        }
        if(concertDTO.getTitle() != null){
            concertDB.setTitle(concertDTO.getTitle());
        }
        if(concertDTO.getOrganizer() != null){
            concertDB.setTitle(concertDTO.getTitle());
        }
        if(concertDTO.getMusicians() != null){
            concertDB.setMusicians(concertDTO.getMusicians());
        }
        if(concertDTO.getSongs() != null){
            concertDB.setSongs(concertDTO.getSongs());
        }
    }
}
