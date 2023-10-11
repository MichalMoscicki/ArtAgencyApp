package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.payload.InstrumentDTO;
import com.immpresariat.ArtAgencyApp.repository.InstrumentRepository;
import com.immpresariat.ArtAgencyApp.service.impl.InstrumentServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class InstrumentServiceTest {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    InstrumentRepository instrumentRepository;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    InstrumentServiceImpl instrumentService;

    @DisplayName("JUnit test for Instrument create method")
    @Test
    public void givenUnsyncDTO_whenCreate_thenReturnDTOObject() {
        //given - precondition or setup
        given(dtoMapper.mapToEntity(any(InstrumentDTO.class))).willReturn(new Instrument());
        given(inputCleaner.clean(any(Instrument.class))).willReturn(new Instrument());
        given(instrumentRepository.save(any(Instrument.class))).willReturn(new Instrument());
        given(dtoMapper.mapToDTO(any(Instrument.class))).willReturn(new InstrumentDTO());

        //when - action or the behavior that we are going to test
        InstrumentDTO instrumentDTO = instrumentService.create(new InstrumentDTO());

        //then - verify the output
        assertNotNull(instrumentDTO);
        verify(dtoMapper, times(1)).mapToEntity(any(InstrumentDTO.class));
        verify(inputCleaner, times(1)).clean(any(Instrument.class));
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Instrument.class));
    }


    @DisplayName("JUnit test for Instrument getById method (negative scenario)")
    @Test
    public void whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(instrumentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            instrumentService.getById(anyLong());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToDTO(any(Instrument.class));
    }


    @DisplayName("JUnit test for Instrument getById method (positive scenario)")
    @Test
    public void whenGetById_thenReturnObject() {
        //given - precondition or setup
        given(instrumentRepository.findById(anyLong())).willReturn(Optional.of(new Instrument()));
        given(dtoMapper.mapToDTO(any(Instrument.class))).willReturn(new InstrumentDTO());


        //when - action or the behavior that we are going to test
        InstrumentDTO instrumentDTO = instrumentService.getById(anyLong());

        //then - verify the output
        assertNotNull(instrumentDTO);
        verify(instrumentRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Instrument.class));
    }



    @DisplayName("JUnit test for getAll Instruments method")
    @Test
    public void givenListOfObjects_whenGetAll_thenReturnListOfTaskDTO() {
        //given - precondition or setup
        List<Instrument> mockContent = Collections.singletonList(new Instrument());

        when(instrumentRepository.findAll()).thenReturn(mockContent);
        when(dtoMapper.mapToDTO(any(Instrument.class))).thenReturn(new InstrumentDTO());

        //when - action or the behavior that we are going to test
        List<InstrumentDTO> result = instrumentService.getAll();

        //then - verify the output
        verify(instrumentRepository, times(1)).findAll();
        verify(dtoMapper, times(mockContent.size())).mapToDTO(any(Instrument.class));

    }


    @DisplayName("JUnit test for update Instrument method (negative scenario)")
    @Test
    public void givenInstrumentDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        InstrumentDTO instrumentDTO = InstrumentDTO.builder()
                .id(0L)
                .name("TestTask")
                .build();

        given(instrumentRepository.findById(instrumentDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            instrumentService.update(instrumentDTO);
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(InstrumentDTO.class));
        verify(inputCleaner, never()).clean(any(Instrument.class));
        verify(instrumentRepository, never()).save(any(Instrument.class));
        verify(dtoMapper, never()).mapToDTO(any(Instrument.class));
    }

    @DisplayName("JUnit test for update Instrument method (positive scenario)")
    @Test
    public void givenInstrumentDTOObject_whenUpdate_thenReturnUpdatedObject() {
        //given - precondition or setup
        InstrumentDTO instrumentDTO = InstrumentDTO.builder()
                .id(0L)
                .name("TestTask")
                .build();

        given(instrumentRepository.findById(instrumentDTO.getId())).willReturn(Optional.of(new Instrument()));
        given(dtoMapper.mapToEntity(any(InstrumentDTO.class))).willReturn(new Instrument());
        given(inputCleaner.clean(any(Instrument.class))).willReturn(new Instrument());
        given(instrumentRepository.save(any(Instrument.class))).willReturn(new Instrument());
        given(dtoMapper.mapToDTO(any(Instrument.class))).willReturn(new InstrumentDTO());

        //when - action or the behavior that we are going to test
        InstrumentDTO updatedInstrumentDTO = instrumentService.update(instrumentDTO);

        //then - verify the output
        assertNotNull(updatedInstrumentDTO);
        verify(instrumentRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToEntity(any(InstrumentDTO.class));
        verify(inputCleaner, times(1)).clean(any(Instrument.class));
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Instrument.class));
    }

    @DisplayName("JUnit test for Instrument delete method")
    @Test
    public void givenId_whenDeleteById_thenTaskDeleted() {
        //given - precondition or setup
        Long id = 0L;
        doNothing().when(instrumentRepository).deleteById(id);

        //when - action or the behavior that we are going to test
        instrumentService.deleteById(id);

        //then - verify the output
        verify(instrumentRepository, times(1)).deleteById(id);
    }


}
