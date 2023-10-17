package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.models.Song;
import com.immpresariat.ArtAgencyApp.payload.PartDTO;
import com.immpresariat.ArtAgencyApp.repository.InstrumentRepository;
import com.immpresariat.ArtAgencyApp.repository.PartRepository;
import com.immpresariat.ArtAgencyApp.repository.SongRepository;
import com.immpresariat.ArtAgencyApp.service.impl.PartServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartServiceTests {

    @Mock
    SongRepository songRepository;
    @Mock
    PartRepository partRepository;
    @Mock
    InstrumentRepository instrumentRepository;
    @Mock
    DTOMapper dtoMapper;
    @InjectMocks
    PartServiceImpl partService;

    @DisplayName("JUnit test for store method (negative scenario)")
    @Test
    public void givenSongId_whenStore_thenThrowNewResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        Instrument instrument = new Instrument();
        given(songRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            partService.store(any(MultipartFile.class), instrument, id);
        });

        //then - verify the output
        verify(partRepository, never()).save(any(Part.class));
        verify(songRepository, never()).save(any(Song.class));
        verify(instrumentRepository, never()).findById(anyLong());
        verify(dtoMapper, never()).mapToDTO(any(Part.class));

    }

    @DisplayName("JUnit test for store method (negative scenario- non unique instrument)")
    @Test
    public void givenInstrument_whenStore_thenThrowNewResourceAlreadyExistsException() {
        //given - precondition or setup
        Long id = 0L;
        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("tuba")
                .build();

        Part part = Part.builder()
                .id(0L)
                .type("pdf")
                .instrument(instrument)
                .name("SongTitle")
                .build();

        List<Part> parts = new ArrayList<>();
        parts.add(part);

        Song song = Song.builder()
                .id(0L)
                .title("SongTitle")
                .parts(parts)
                .description("Description")
                .composers("Composers")
                .textAuthors("TextAuthors")
                .build();

        given(songRepository.findById(anyLong())).willReturn(Optional.of(song));

        //when - action or the behavior that we are going to test
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            partService.store(any(MultipartFile.class), instrument, id);
        });

        //then - verify the output
        verify(songRepository, times(1)).findById(anyLong());
        verify(partRepository, never()).save(any(Part.class));
        verify(songRepository, never()).save(any(Song.class));
        verify(instrumentRepository, never()).findById(anyLong());
        verify(dtoMapper, never()).mapToDTO(any(Part.class));

    }

    @DisplayName("JUnit test for store method (positiveScenario)")
    @Test
    public void givenInstrumentIdFile_whenStore_thenFileSaved() throws IOException {
        //given - precondition or setup
        Long id = 0L;
        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("tuba")
                .build();

        List<Part> parts = new ArrayList<>();

        Song song = Song.builder()
                .id(0L)
                .title("SongTitle")
                .parts(parts)
                .description("Description")
                .composers("Composers")
                .textAuthors("TextAuthors")
                .build();

        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        given(songRepository.findById(anyLong())).willReturn(Optional.of(song));
        given(partRepository.save(any(Part.class))).willReturn(new Part());
        given(songRepository.save(any(Song.class))).willReturn(new Song());
        given(dtoMapper.mapToDTO(any(Part.class))).willReturn(new PartDTO());
        given(instrumentRepository.findById(instrument.getId())).willReturn(Optional.of(instrument));

        //when - action or the behavior that we are going to test
        PartDTO partDTO = partService.store(file, instrument, id);

        //then - verify the output
        assertNotNull(partDTO);
        verify(songRepository, times(1)).findById(anyLong());
        verify(partRepository, times(1)).save(any(Part.class));
        verify(songRepository, times(1)).save(any(Song.class));
        verify(instrumentRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Part.class));

    }

    @DisplayName("JUnit test for getById (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowNewResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        given(partRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            partService.getById(id);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for getById (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnPart() {
        //given - precondition or setup
        Long id = 0L;
        given(partRepository.findById(id)).willReturn(Optional.of(new Part()));

        //when - action or the behavior that we are going to test
        Part part = partService.getById(id);

        //then - verify the output
        assertNotNull(part);
    }

    @DisplayName("JUnit test for deleteById (negative scenario - no part)")
    @Test
    public void givenPartId_whenDeleteById_thenThrowNewResourceNotFoundException() {
        //given - precondition or setup
        Long partId = 0L;
        Long songId = 0L;
        given(partRepository.findById(partId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            partService.deleteById(partId, songId);
        });

        //then - verify the output
        verify(partRepository, times(1)).findById(anyLong());
        verify(songRepository, never()).findById(anyLong());
        verify(songRepository, never()).save(any(Song.class));
        verify(partRepository, never()).delete(any(Part.class));
    }

    @DisplayName("JUnit test for deleteById (negative scenario - no song)")
    @Test
    public void givenSongId_whenDeleteById_thenThrowNewResourceNotFoundException() {
        //given - precondition or setup
        Long partId = 0L;
        Long songId = 0L;
        given(partRepository.findById(partId)).willReturn(Optional.of(new Part()));
        given(songRepository.findById(partId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            partService.deleteById(partId, songId);
        });

        //then - verify the output
        verify(partRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(songRepository, never()).save(any(Song.class));
        verify(partRepository, never()).delete(any(Part.class));
    }

    @DisplayName("JUnit test for deleteById (positive scenario)")
    @Test
    public void givenSongIdPartId_whenDeleteById_thenObjectDeleted() {
        //given - precondition or setup
        Long partId = 0L;
        Long songId = 0L;

        Instrument instrument = Instrument.builder()
                .id(0L)
                .name("tuba")
                .build();

        Part part = Part.builder()
                .id(0L)
                .type("pdf")
                .instrument(instrument)
                .name("SongTitle")
                .build();

        List<Part> parts = new ArrayList<>();
        parts.add(part);

        Song song = Song.builder()
                .id(0L)
                .title("SongTitle")
                .parts(parts)
                .description("Description")
                .composers("Composers")
                .textAuthors("TextAuthors")
                .build();

        given(partRepository.findById(partId)).willReturn(Optional.of(part));
        given(songRepository.findById(partId)).willReturn(Optional.of(song));
        given(songRepository.save(any(Song.class))).willReturn(new Song());
        doNothing().when(partRepository).delete(part);

        //when - action or the behavior that we are going to test
        partService.deleteById(partId, songId);

        //then - verify the output
        verify(partRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).save(any(Song.class));
        verify(partRepository, times(1)).delete(any(Part.class));
    }
}