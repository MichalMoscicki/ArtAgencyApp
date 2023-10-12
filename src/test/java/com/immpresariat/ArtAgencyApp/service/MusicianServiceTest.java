package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Musician;
import com.immpresariat.ArtAgencyApp.payload.MusicianDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.repository.MusicianRepository;
import com.immpresariat.ArtAgencyApp.service.impl.MusicianServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class MusicianServiceTest {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    MusicianRepository musicianRepository;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    MusicianServiceImpl musicianService;


    @DisplayName("JUnit test for Musician create method")
    @Test
    public void givenUnsyncMusicianDTO_whenCreate_thenReturnMusicianDTOObject() {
        //given - precondition or setup
        given(dtoMapper.mapToEntity(any(MusicianDTO.class))).willReturn(new Musician());
        given(inputCleaner.clean(any(Musician.class))).willReturn(new Musician());
        given(musicianRepository.save(any(Musician.class))).willReturn(new Musician());
        given(dtoMapper.mapToDTO(any(Musician.class))).willReturn(new MusicianDTO());

        //when - action or the behavior that we are going to test
        MusicianDTO musicianDTO = musicianService.create(new MusicianDTO());

        //then - verify the output
        assertNotNull(musicianDTO);
        verify(dtoMapper, times(1)).mapToEntity(any(MusicianDTO.class));
        verify(inputCleaner, times(1)).clean(any(Musician.class));
        verify(musicianRepository, times(1)).save(any(Musician.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Musician.class));
    }


    @DisplayName("JUnit test for Musician getById method (negative scenario)")
    @Test
    public void whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(musicianRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            musicianService.getById(anyLong());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToDTO(any(Musician.class));
    }


    @DisplayName("JUnit test for Task getById method (positive scenario)")
    @Test
    public void whenGetById_thenReturnMusicianObject() {
        //given - precondition or setup
        given(musicianRepository.findById(anyLong())).willReturn(Optional.of(new Musician()));
        given(dtoMapper.mapToDTO(any(Musician.class))).willReturn(new MusicianDTO());

        //when - action or the behavior that we are going to test
        MusicianDTO musicianDTO = musicianService.getById(anyLong());

        //then - verify the output
        assertNotNull(musicianDTO);
        verify(musicianRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Musician.class));
    }

    @DisplayName("JUnit test for getAll method (null instrument)")
    @Test
    public void given_whenGetAll_thenReturnListOfMusicianDTO() {
        //given - precondition or setup
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.DEFAULT_SORT_BY).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Musician> mockPage = new PageImpl<>(Collections.singletonList(new Musician()));
        List<MusicianDTO> mockContent = Collections.singletonList(new MusicianDTO());

        when(musicianRepository.findAll(pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Musician.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        PageResponse<MusicianDTO> result = musicianService.getAll(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION, null);

        //then - verify the output
        assertEquals(mockContent, result.getContent());

    }

    @DisplayName("JUnit test for getAll method (present instrument)")
    @Test
    public void givenInstrumentName_whenGetAll_thenReturnListOfMusicianDTO() {
        //given - precondition or setup
        String instrumentName = "piano";
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.DEFAULT_SORT_BY).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Musician> mockPage = new PageImpl<>(Collections.singletonList(new Musician()));
        List<MusicianDTO> mockContent = Collections.singletonList(new MusicianDTO());

        when(musicianRepository.findAllByInstrument(instrumentName, pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Musician.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        PageResponse<MusicianDTO> result = musicianService.getAll(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION, instrumentName);

        //then - verify the output
        assertEquals(mockContent, result.getContent());

    }


    @DisplayName("JUnit test for Musician getById method (negative scenario)")
    @Test
    public void givenMusicianDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        MusicianDTO musicianDTO = MusicianDTO.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48111222333")
                .notes("Notatki")
                .instruments(new ArrayList<>())
                .build();

        given(musicianRepository.findById(musicianDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            musicianService.update(musicianDTO);
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(MusicianDTO.class));
        verify(inputCleaner, never()).clean(any(Musician.class));
        verify(musicianRepository, never()).save(any(Musician.class));
        verify(dtoMapper, never()).mapToDTO(any(Musician.class));
    }


    @DisplayName("JUnit test for Task getById method (positive scenario)")
    @Test
    public void givenContactDTOObject_whenUpdate_thenReturnUpdatedObject() {
        //given - precondition or setup
        MusicianDTO musicianDTO = MusicianDTO.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .phone("+48111222333")
                .notes("Notatki")
                .instruments(new ArrayList<>())
                .build();

        given(musicianRepository.findById(musicianDTO.getId())).willReturn(Optional.of(new Musician()));
        given(dtoMapper.mapToEntity(musicianDTO)).willReturn(new Musician());
        given(inputCleaner.clean(any(Musician.class))).willReturn(new Musician());
        given(musicianRepository.save(any(Musician.class))).willReturn(new Musician());
        given(dtoMapper.mapToDTO(any(Musician.class))).willReturn(new MusicianDTO());

        //when - action or the behavior that we are going to test
        MusicianDTO updatedDto = musicianService.update(musicianDTO);

        //then - verify the output
        verify(dtoMapper, times(1)).mapToEntity(any(MusicianDTO.class));
        verify(inputCleaner, times(1)).clean(any(Musician.class));
        verify(musicianRepository, times(1)).save(any(Musician.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Musician.class));
    }


    @DisplayName("JUnit test for Musician delete method (negative scenario)")
    @Test
    public void givenId_whenDeleteById_thenReturnResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        given(musicianRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            musicianService.deleteById(id);
        });

        //then - verify the output
        verify(musicianRepository, never()).save(any(Musician.class));
        verify(musicianRepository, never()).deleteById(id);
    }


    @DisplayName("JUnit test for TaskService delete method (positive scenario without attachments)")
    @Test
    public void givenId_whenDeleteById_thenTaskDeleted() {
        //given - precondition or setup
        Long id = 0L;
        given(musicianRepository.findById(id)).willReturn(Optional.of(new Musician()));
        given(musicianRepository.save(any(Musician.class))).willReturn(new Musician());
        doNothing().when(musicianRepository).deleteById(id);

        //when - action or the behavior that we are going to test
        musicianService.deleteById(id);

        //then - verify the output
        verify(musicianRepository, times(1)).save(any(Musician.class));
        verify(musicianRepository, times(1)).deleteById(id);
    }

}
