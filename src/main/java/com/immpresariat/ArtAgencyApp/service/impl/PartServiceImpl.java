package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.models.Song;
import com.immpresariat.ArtAgencyApp.payload.PartDTO;
import com.immpresariat.ArtAgencyApp.repository.InstrumentRepository;
import com.immpresariat.ArtAgencyApp.repository.PartRepository;
import com.immpresariat.ArtAgencyApp.repository.SongRepository;
import com.immpresariat.ArtAgencyApp.service.PartService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final InstrumentRepository instrumentRepository;
    private final SongRepository songRepository;
    private final DTOMapper dtoMapper;

    public PartServiceImpl(PartRepository partRepository,
                           InstrumentRepository instrumentRepository,
                           SongRepository songRepository, DTOMapper dtoMapper) {
        this.partRepository = partRepository;
        this.instrumentRepository = instrumentRepository;
        this.songRepository = songRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public PartDTO store(MultipartFile file, Instrument instrument, Long songId) throws IOException {
        Song song = ensureSongExists(songId);
        Part part = new Part();

        ensureUniquePartInSong(instrument, song);

        part.setName(file.getOriginalFilename());
        part.setData(file.getBytes());
        part.setInstrument(ensureInstrumentExists(instrument.getId()));
        part.setType(file.getContentType());

        Part savedPart = partRepository.save(part);

        List<Part> songsParts = song.getParts();
        songsParts.add(savedPart);
        song.setParts(songsParts);
        songRepository.save(song);

        return dtoMapper.mapToDTO(savedPart);
    }

    private static void ensureUniquePartInSong(Instrument instrument, Song song) {
        boolean nonUnique = song.getParts().stream().anyMatch(el -> el.getInstrument().getName().equals(instrument.getName()));
        if(nonUnique){
            String message = String.format("Song: %s already has %s part", song.getTitle(), instrument.getName());
            throw new ResourceAlreadyExistsException(message);
        }
    }

    @Override
    public Part getById(Long id) {
        return ensurePartExists(id);
    }

    @Override
    public void deleteById(Long partId, Long songId) {
        Part part = ensurePartExists(partId);
        Song song = ensureSongExists(songId);

        removePartFromSong(part, song);
        partRepository.delete(part);
    }

    private void removePartFromSong(Part part, Song song) {
        List<Part> parts = song.getParts();
        parts.remove(part);
        song.setParts(parts);
        songRepository.save(song);
    }

    private Instrument ensureInstrumentExists(Long id) {
        Optional<Instrument> instrumentOptional = instrumentRepository.findById(id);
        if (instrumentOptional.isEmpty()) {
            throw new ResourceNotFoundException("No instrument with id: " + id);
        }
        return instrumentOptional.get();
    }

    private Song ensureSongExists(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isEmpty()) {
            throw new ResourceNotFoundException("No song with id: " + id);
        }
        return songOptional.get();
    }

    private Part ensurePartExists(Long id) {
        Optional<Part> partOptional = partRepository.findById(id);
        if (partOptional.isEmpty()) {
            throw new ResourceNotFoundException("No part with id: " + id);
        }
        return partOptional.get();
    }


}
