package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Musician;
import com.immpresariat.ArtAgencyApp.payload.MusicianDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.repository.MusicianRepository;
import com.immpresariat.ArtAgencyApp.service.MusicianService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class MusicianServiceImpl implements MusicianService {

    private final MusicianRepository musicianRepository;
    private final DTOMapper dtoMapper;
    private final InputCleaner inputCleaner;

    public MusicianServiceImpl(MusicianRepository musicianRepository, DTOMapper dtoMapper, InputCleaner inputCleaner) {
        this.musicianRepository = musicianRepository;
        this.dtoMapper = dtoMapper;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public MusicianDTO create(MusicianDTO musicianDTO) {
        Musician musician = dtoMapper.mapToEntity(musicianDTO);
        Musician musicianDB = musicianRepository.save(inputCleaner.clean(musician));
        return dtoMapper.mapToDTO(musicianDB);
    }

    @Override
    public PageResponse<MusicianDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir, String instrument) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Musician> page;

        if (instrument == null) {
            page = musicianRepository.findAll(pageable);
        } else {
            page = musicianRepository.findAllByInstrument(instrument, pageable);
        }

        List<Musician> musicians = page.getContent();
        List<MusicianDTO> content = musicians.stream().map(dtoMapper::mapToDTO).toList();

        return PageResponse.createResponse(page, content);

    }

    @Override
    public MusicianDTO getById(Long id) {
        return dtoMapper.mapToDTO(ensureMusicianExists(id));
    }

    @Override
    public MusicianDTO update(MusicianDTO musicianDTO) {
        ensureMusicianExists(musicianDTO.getId());
        Musician musician = dtoMapper.mapToEntity(musicianDTO);
        return dtoMapper.mapToDTO(musicianRepository.save(inputCleaner.clean(musician)));
    }

    @Override
    public void deleteById(Long id) {
        Musician musician = ensureMusicianExists(id);
        musician.setInstruments(null);
        musicianRepository.save(musician);
        musicianRepository.deleteById(id);

    }

    private Musician ensureMusicianExists(Long id) {
        Optional<Musician> musicianOptional = musicianRepository.findById(id);
        if (musicianOptional.isEmpty()) {
            throw new ResourceNotFoundException("No musician with id: " + id);
        }
        return musicianOptional.get();
    }
}
