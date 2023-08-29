package com.immpresariat.ArtAgencyApp.service;


import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.impl.InstitutionServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class InstitutionServiceTests {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    ContactRepository contactRepository;
    @Mock
    DTOMapper dtoMapper;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    InstitutionServiceImpl institutionService;
    Institution institution;
    InstitutionDTO unsyncInstitutionDTO;
    InstitutionDTO synchronizedInstitutionDTO;

    @BeforeEach
    public void setup() {
        institution = Institution.builder()
                .id(0L)
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .build();

        unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .build();

        synchronizedInstitutionDTO =  InstitutionDTO.builder()
                .id(0L)
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
                .alreadyCooperated(true)
                .category("DK")
                .build();
    }

    @DisplayName("JUnit test for InstitutionService create method (negative scenario) ")
    @Test
    public void givenContactId_whenCreate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        given(contactRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.create(unsyncInstitutionDTO, id);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for InstitutionService create method (positive scenario) ")
    @Test
    public void givenUnsyncInstitutionDTOObject_whenCreate_thenReturnSynchronizedEventDTOObject() {
        //given - precondition or setup
        Long id = 0L;
        given(contactRepository.findById(id)).willReturn(Optional.of(new Contact()));
        given(dtoMapper.mapUnsyncDTOToInstitution(unsyncInstitutionDTO)).willReturn(new Institution());
        given(inputCleaner.clean(any(Institution.class))).willReturn(new Institution());
        given(institutionRepository.save(any(Institution.class))).willReturn(new Institution());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);


        //when - action or the behavior that we are going to test
        InstitutionDTO dbInstitutionDTO = institutionService.create(unsyncInstitutionDTO, id);

        //then - verify the output
        assertNotNull(dbInstitutionDTO);
        verify(dtoMapper, times(1)).mapUnsyncDTOToInstitution(any(InstitutionDTO.class));
        verify(inputCleaner, times(1)).clean(any(Institution.class));
        verify(institutionRepository, times(1)).save(any(Institution.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapInstitutionToDTO(any(Institution.class));

    }


    @DisplayName("JUnit test for InstitutionService getById method (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0L;
        given(institutionRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.getById(id);
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for InstitutionService getById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnInstitutionDTOObject() {
        //given - precondition or setup
        Long id = 0L;
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTO = institutionService.getById(id);

        //then - verify the output
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapInstitutionToDTO(any(Institution.class));

    }

    /*
    @DisplayName("JUnit test for InstitutionService update method (negative scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        EventDTO inputDTO = synchronizedEventDTO;
        given(eventRepository.findById(inputDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.update(inputDTO);
        });
        //then - verify the output

    }

    @DisplayName("JUnit test for InstitutionService update method (positive scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenReturnEventDTO() {
        //given - precondition or setup
        EventDTO inputDTO = synchronizedEventDTO;
        given(eventRepository.findById(inputDTO.getId())).willReturn(Optional.of(event));
        given(inputCleaner.clean(any(Event.class))).willReturn(new Event());
        given(eventRepository.save(any(Event.class))).willReturn(new Event());
        given(dtoMapper.mapEventToDTO(any(Event.class))).willReturn(synchronizedEventDTO);

        //when - action or the behavior that we are going to test
        EventDTO eventDTO = eventService.update(synchronizedEventDTO);

        //then - verify the output
        assertNotNull(eventDTO);
        verify(eventRepository, times(1)).findById(anyLong());
        verify(inputCleaner, times(1)).clean(any(Event.class));
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(dtoMapper, times(1)).mapEventToDTO(any(Event.class));

    }
      */

    @DisplayName("JUnit test for InstitutionService delete method")
    @Test
    public void givenId_whenDelete_thenInstitutionDeleted() {
        //given - precondition or setup
        Long id = 0L;
        doNothing().when(institutionRepository).deleteById(id);

        //when - action or the behavior that we are going to test
        institutionService.deleteById(id);

        //then - verify the output
        verify(institutionRepository, Mockito.times(1)).deleteById(id);
    }

    /*

    @DisplayName("JUnit test for InstitutionService update method (negative scenario)")
    @Test
    public void givenInstitutionId_whenUpdateInstitution_thenThrowsException() {
        //given - precondition or setup
        synchronizedInstitutionDTO.setNotes("Notatki, żeby sprawdzić zmianę");
        InstitutionDTO updatedInstitutionDTO = synchronizedInstitutionDTO;
        given(institutionRepository.findById(anyLong())).willReturn(Optional.empty());


        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.update(updatedInstitutionDTO);
        });

        //then - verify the output
        Mockito.verify(institutionRepository, never()).save(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService update method (positive scenario)")
    @Test
    public void givenIdAndUpdatedInstitution_whenUpdateInstitution_thenReturnUpdatedInstitutionDTO() {

        //given - precondition or setup
        synchronizedInstitutionDTO.setNotes("Notatki, żeby sprawdzić zmianę");
        InstitutionDTO updatedInstitutionDTO = synchronizedInstitutionDTO;

        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(new Institution()));
        given(dtoMapper.mapDTOToInstitution(updatedInstitutionDTO)).willReturn(new Institution());
        given(inputCleaner.clean(any(Institution.class))).willReturn(new Institution());
        given(institutionRepository.save(any(Institution.class))).willReturn(new Institution());
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(updatedInstitutionDTO);


        //when - action or the behavior that we are going to test
        InstitutionDTO updatedInstitutionDTODb = institutionService.update(updatedInstitutionDTO);

        //then - verify the output
        assertThat(updatedInstitutionDTODb).isNotNull();
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapDTOToInstitution(any(InstitutionDTO.class));
        verify(inputCleaner, times(1)).clean(any(Institution.class));
        verify(institutionRepository, times(1)).save(any(Institution.class));
        verify(dtoMapper, times(1)).mapInstitutionToDTO(any(Institution.class));
        assertThat(updatedInstitutionDTO).isEqualTo(updatedInstitutionDTODb);

    }

     */


}