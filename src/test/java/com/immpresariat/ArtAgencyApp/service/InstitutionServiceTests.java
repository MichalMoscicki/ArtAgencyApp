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
                .category("DK")
                .build();

        unsyncInstitutionDTO = InstitutionDTO.builder()
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
                .category("DK")
                .build();

        synchronizedInstitutionDTO =  InstitutionDTO.builder()
                .id(0L)
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
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
        given(dtoMapper.mapToEntity(unsyncInstitutionDTO)).willReturn(new Institution());
        given(inputCleaner.clean(any(Institution.class))).willReturn(new Institution());
        given(institutionRepository.save(any(Institution.class))).willReturn(new Institution());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);


        //when - action or the behavior that we are going to test
        InstitutionDTO dbInstitutionDTO = institutionService.create(unsyncInstitutionDTO, id);

        //then - verify the output
        assertNotNull(dbInstitutionDTO);
        verify(dtoMapper, times(1)).mapToEntity(any(InstitutionDTO.class));
        verify(inputCleaner, times(1)).clean(any(Institution.class));
        verify(institutionRepository, times(1)).save(any(Institution.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Institution.class));

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
        given(dtoMapper.mapToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTO = institutionService.getById(id);

        //then - verify the output
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Institution.class));

    }



}