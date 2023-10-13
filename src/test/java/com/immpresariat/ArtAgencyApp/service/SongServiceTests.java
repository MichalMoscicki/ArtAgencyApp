package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.models.Song;
import com.immpresariat.ArtAgencyApp.payload.SongDTO;
import com.immpresariat.ArtAgencyApp.repository.PartRepository;
import com.immpresariat.ArtAgencyApp.repository.SongRepository;
import com.immpresariat.ArtAgencyApp.service.impl.SongServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Musician;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class SongServiceTests {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    SongRepository songRepository;
    @Mock
    PartRepository partRepository;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    SongServiceImpl songService;


    @DisplayName("JUnit test for Song create method")
    @Test
    public void givenUnsyncSongDTO_whenCreate_thenReturnSongDTOObject() {
        //given - precondition or setup
        given(dtoMapper.mapToEntity(any(SongDTO.class))).willReturn(new Song());
        given(inputCleaner.clean(any(Song.class))).willReturn(new Song());
        given(songRepository.save(any(Song.class))).willReturn(new Song());
        given(dtoMapper.mapToDTO(any(Song.class))).willReturn(new SongDTO());

        //when - action or the behavior that we are going to test
        SongDTO songDTO = songService.create(new SongDTO());

        //then - verify the output
        assertNotNull(songDTO);
        verify(dtoMapper, times(1)).mapToEntity(any(SongDTO.class));
        verify(inputCleaner, times(1)).clean(any(Song.class));
        verify(songRepository, times(1)).save(any(Song.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Song.class));
    }


    @DisplayName("JUnit test for song getById method (negative scenario)")
    @Test
    public void whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(songRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getById(anyLong());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToDTO(any(Musician.class));
    }


    @DisplayName("JUnit test for Song getById method (positive scenario)")
    @Test
    public void whenGetById_thenReturnSongObject() {
        //given - precondition or setup
        given(songRepository.findById(anyLong())).willReturn(Optional.of(new Song()));
        given(dtoMapper.mapToDTO(any(Song.class))).willReturn(new SongDTO());

        //when - action or the behavior that we are going to test
        SongDTO songDTO = songService.getById(anyLong());

        //then - verify the output
        assertNotNull(songDTO);
        verify(songRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Song.class));
    }

    @DisplayName("JUnit test for getAll method (null title)")
    @Test
    public void given_whenGetAll_thenReturnListOfSongDTO() {
        //given - precondition or setup
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.TITLE).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Song> mockPage = new PageImpl<>(Collections.singletonList(new Song()));
        List<SongDTO> mockContent = Collections.singletonList(new SongDTO());

        when(songRepository.findAll(pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Song.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        PageResponse<SongDTO> result = songService.getAll(pageNo, pageSize, AppConstants.TITLE, AppConstants.DEFAULT_SORT_DIRECTION, null);

        //then - verify the output
        assertEquals(mockContent, result.getContent());

    }


    @DisplayName("JUnit test for getAll method (present title)")
    @Test
    public void givenTitle_whenGetAll_thenReturnListOfSongDTO() {
        //given - precondition or setup
        String title = "Happy Birthday";
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.TITLE).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Song> mockPage = new PageImpl<>(Collections.singletonList(new Song()));
        List<SongDTO> mockContent = Collections.singletonList(new SongDTO());

        when(songRepository.findAllByTitle(title, pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Song.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        PageResponse<SongDTO> result = songService.getAll(pageNo, pageSize, AppConstants.TITLE, AppConstants.DEFAULT_SORT_DIRECTION, title);

        //then - verify the output
        assertEquals(mockContent, result.getContent());

    }


    @DisplayName("JUnit test for Song update method (negative scenario)")
    @Test
    public void givenSongDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        SongDTO songDTO = SongDTO.builder()
                .id(0L)
                .title("Piosenka")
                .description("Opis")
                .composers("Karol Modzelewski, Jan Orwat")
                .textAuthors("Wiesio Kiprowicz")
                .parts(new ArrayList<>())
                .build();

        given(songRepository.findById(songDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.update(songDTO);
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(SongDTO.class));
        verify(inputCleaner, never()).clean(any(Song.class));
        verify(songRepository, never()).save(any(Song.class));
        verify(dtoMapper, never()).mapToDTO(any(Song.class));
    }


    @DisplayName("JUnit test for Song update method (positive scenario)")
    @Test
    public void givenSongDTOObject_whenUpdate_thenReturnUpdatedObject() {
        //given - precondition or setup
        SongDTO songDTO = SongDTO.builder()
                .id(0L)
                .title("Piosenka")
                .description("Opis")
                .composers("Karol Modzelewski, Jan Orwat")
                .textAuthors("Wiesio Kiprowicz")
                .parts(new ArrayList<>())
                .build();

        given(songRepository.findById(songDTO.getId())).willReturn(Optional.of(new Song()));
        given(dtoMapper.mapToEntity(songDTO)).willReturn(new Song());
        given(inputCleaner.clean(any(Song.class))).willReturn(new Song());
        given(songRepository.save(any(Song.class))).willReturn(new Song());
        given(dtoMapper.mapToDTO(any(Song.class))).willReturn(new SongDTO());

        //when - action or the behavior that we are going to test
        SongDTO updatedDto = songService.update(songDTO);

        //then - verify the output
        verify(dtoMapper, times(1)).mapToEntity(any(SongDTO.class));
        verify(inputCleaner, times(1)).clean(any(Song.class));
        verify(songRepository, times(1)).save(any(Song.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Song.class));
    }


    @DisplayName("JUnit test for Song delete method (negative scenario)")
    @Test
    public void givenId_whenDeleteById_thenReturnResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        given(songRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.deleteById(id);
        });

        //then - verify the output
        verify(songRepository, never()).save(any(Song.class));
        verify(partRepository, never()).deleteAll(anyCollection());
        verify(songRepository, never()).deleteById(id);
    }

    @DisplayName("JUnit test for Song without parts delete method (positive scenario)")
    @Test
    public void givenId_whenDeleteById_thenSongDeleted() {
        //given - precondition or setup
        Long id = 0L;
        given(songRepository.findById(id)).willReturn(Optional.of(new Song()));
        doNothing().when(songRepository).deleteById(id);

        //when - action or the behavior that we are going to test
        songService.deleteById(id);

        //then - verify the output
        verify(songRepository, times(1)).deleteById(id);
    }


    @DisplayName("JUnit test for Song with parts delete method (positive scenario)")
    @Test
    public void givenSongWithParts_whenDeleteById_thenSongDeleted() {
        Part part = new Part();
        List<Part> parts = new ArrayList<>();
        parts.add(part);

        Song song = Song.builder()
                .id(0L)
                .title("Piosenka")
                .description("Opis")
                .composers("Karol Modzelewski, Jan Orwat")
                .textAuthors("Wiesio Kiprowicz")
                .parts(parts)
                .build();

        given(songRepository.findById(song.getId())).willReturn(Optional.of(song));
        given(songRepository.save(any(Song.class))).willReturn(new Song());
        doNothing().when(songRepository).deleteById(song.getId());
        doNothing().when(partRepository).deleteAll(anyCollection());

        //when - action or the behavior that we are going to test
        songService.deleteById(song.getId());

        //then - verify the output

        verify(songRepository, times(1)).save(any(Song.class));
        verify(partRepository, times(1)).deleteAll(anyCollection());
        verify(songRepository, times(1)).deleteById(song.getId());
    }

}
