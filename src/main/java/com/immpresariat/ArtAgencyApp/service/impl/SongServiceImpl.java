package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.models.Song;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.SongDTO;
import com.immpresariat.ArtAgencyApp.repository.PartRepository;
import com.immpresariat.ArtAgencyApp.repository.SongRepository;
import com.immpresariat.ArtAgencyApp.service.SongService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final PartRepository partRepository;
    private final DTOMapper dtoMapper;
    private final InputCleaner inputCleaner;

    public SongServiceImpl(SongRepository songRepository,
                           PartRepository partRepository,
                           DTOMapper dtoMapper,
                           InputCleaner inputCleaner) {
        this.songRepository = songRepository;
        this.partRepository = partRepository;
        this.dtoMapper = dtoMapper;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public SongDTO create(SongDTO songDTO) {
        Song song = dtoMapper.mapToEntity(songDTO);
        return dtoMapper.mapToDTO(songRepository.save(inputCleaner.clean(song)));
    }

    @Override
    public PageResponse<SongDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir, String title) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Song> page;
        if (title == null) {
            page = songRepository.findAll(pageable);
        } else {
            page = songRepository.findAllByTitle(title, pageable);
        }
        List<Song> songs = page.getContent();
        List<SongDTO> content = songs.stream().map(dtoMapper::mapToDTO).toList();

        return PageResponse.createResponse(page, content);
    }

    @Override
    public SongDTO getById(Long id) {
        return dtoMapper.mapToDTO(ensureSongExists(id));
    }

    @Override
    public SongDTO update(SongDTO songDTO) {
        ensureSongExists(songDTO.getId());
        Song song = dtoMapper.mapToEntity(songDTO);
        return dtoMapper.mapToDTO(songRepository.save(inputCleaner.clean(song)));
    }

    @Override
    public SongDTO deleteById(Long id) {
        Song song = ensureSongExists(id);
        deleteConnectedParts(song);
        songRepository.deleteById(id);
        return null;
    }

    private void deleteConnectedParts(Song song) {
        if (song.getParts() != null) {
            List<Part> parts = song.getParts();
            song.setParts(null);
            songRepository.save(song);
            partRepository.deleteAll(parts);
        }
    }

    private Song ensureSongExists(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isEmpty()) {
            throw new ResourceNotFoundException("No song with id: " + id);
        }
        return songOptional.get();
    }
}
