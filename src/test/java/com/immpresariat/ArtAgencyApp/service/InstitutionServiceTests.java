package com.immpresariat.ArtAgencyApp.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.EventRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InstitutionServiceTests {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ContactPersonRepository contactPersonRepository;
    @Mock
    private InputCleaner inputCleaner;
    @Mock
    private DTOMapper dtoMapper;


    @InjectMocks
    private InstitutionServiceImpl institutionService;
    private InstitutionDTO synchronizedInstitutionDTO;
    private InstitutionDTO unsynchronizedInstitutionDTO;

    @BeforeEach
    public void setup() {
        synchronizedInstitutionDTO = InstitutionDTO.builder()
                .id(1L)
                .name("DK Łomianki")
                .city("Łomianki")
                .category("dom kultury")
                .alreadyCooperated(true)
                .notes("Graliśmy tam z Marią")
                .build();

        unsynchronizedInstitutionDTO = InstitutionDTO.builder()
                .name("DK Łomianki")
                .city("Łomianki")
                .category("dom kultury")
                .alreadyCooperated(true)
                .notes("Graliśmy tam z Marią")
                .build();
    }

    @DisplayName("JUnit test for InstitutionService create method")
    @Test
    public void givenUnsynchronizedInstitutionDTOObject_whenCreateInstitution_thenReturnSynchronizedInstitutionObject() {
        //given - precondition or setup

        given(institutionRepository.findInstitutionByNameAndCity(unsynchronizedInstitutionDTO.getName(), unsynchronizedInstitutionDTO.getCity()))
                .willReturn(Optional.empty());
        given(dtoMapper.mapUnsyncDTOToInstitution(unsynchronizedInstitutionDTO)).willReturn(new Institution());
        given(inputCleaner.clean(any(Institution.class))).willReturn(new Institution());
        given(institutionRepository.save(any(Institution.class))).willReturn(new Institution());
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTOFromDB = institutionService.create(unsynchronizedInstitutionDTO);

        //then - verify the output
        assertThat(institutionDTOFromDB).isNotNull();
        assertThat(institutionDTOFromDB.getId()).isNotNull();
        assertThat(institutionDTOFromDB).isEqualTo(synchronizedInstitutionDTO);

    }

    @DisplayName("JUnit test for InstitutionService create method (negative scenario)")
    @Test
    public void givenUnsynchronizedInstitutionDTOObject_whenCreateInstitution_thenThrowsException() {
        //given - precondition or setup
        given(institutionRepository.findInstitutionByNameAndCity(unsynchronizedInstitutionDTO.getName(), unsynchronizedInstitutionDTO.getCity()))
                .willReturn(Optional.of(new Institution()));

        //when - action or the behavior that we are going to test
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            institutionService.create(unsynchronizedInstitutionDTO);
        });

        //then - verify the output
        Mockito.verify(institutionRepository, never()).save(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService getAll method (negative scenario)")
    @Test
    public void givenListOfInstitutions_whenGetAll_thenReturnEmptyList() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        List<InstitutionDTO> institutionsDB = institutionService.getAll();

        //then - verify the output
        assertThat(institutionsDB).isNotNull();
        assertThat(institutionsDB).isEmpty();

    }

    @DisplayName("JUnit test for InstitutionService getAll method (positive scenario)")
    @Test
    public void givenListOfInstitutions_whenGetAll_thenReturnListOfInstitutions() {
        //given - precondition or setup

        List<Institution> institutions = new ArrayList<>();
        Institution institution = Institution.builder()
                .id(1L)
                .name("DK Łomianki")
                .city("Łomianki")
                .category("dom kultury")
                .alreadyCooperated(true)
                .notes("Graliśmy tam z Marią")
                .build();
        institutions.add(institution);
        given(institutionRepository.findAll()).willReturn(institutions);
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);


        //when - action or the behavior that we are going to test
        List<InstitutionDTO> institutionsDB = institutionService.getAll();

        //then - verify the output
        assertThat(institutionsDB).isNotNull();
        assertThat(institutionsDB.size()).isEqualTo(institutions.size());
        assertThat(institutionsDB.get(institutionsDB.size() - 1).getId()).isEqualTo(institution.getId());

    }

    @DisplayName("JUnit test for InstitutionService findById method (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0l;
        given(institutionRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.getById(id);
        });

        //then - verify the output
    }

    @DisplayName("JUnit test for InstitutionService findById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnInstitutionDTO() {
        //given - precondition or setup
        Long id = synchronizedInstitutionDTO.getId();
        given(institutionRepository.findById(id)).willReturn(Optional.of(new Institution()));
        given(dtoMapper.mapInstitutionToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTODb = institutionService.getById(id);

        //then - verify the output
        assertThat(institutionDTODb).isNotNull();
        verify(institutionRepository, times(1)).findById(id);
        verify(dtoMapper, times(1)).mapInstitutionToDTO(any(Institution.class));
    }

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


    @DisplayName("JUnit test for InstitutionService delete method (Institution Object with associated data)")
    @Test
    public void givenInstitutionWithAssociateData_whenDeleteWithAssociatedData_thenInstitutionAndDataDeleted() {
        //given - precondition or setup
        Long id = synchronizedInstitutionDTO.getId();

        //when - action or the behavior that we are going to test
        institutionService.deleteWithAssociatedData(id);

        //then - verify the output
        verify(institutionRepository, times(1)).deleteById(id);
    }


}