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

import java.util.ArrayList;
import java.util.List;
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
    Long contactId;

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

        synchronizedInstitutionDTO = InstitutionDTO.builder()
                .id(0L)
                .name("DK Głogów")
                .notes("")
                .city("Głogów")
                .category("DK")
                .build();

        contactId = 0L;
    }

    @DisplayName("JUnit test for InstitutionService create method (negative scenario) ")
    @Test
    public void givenContactId_whenCreate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.create(unsyncInstitutionDTO, contactId);
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(InstitutionDTO.class));
        verify(inputCleaner, never()).clean(any(Institution.class));
        verify(institutionRepository, never()).save(any(Institution.class));
        verify(contactRepository, never()).save(any(Contact.class));
        verify(dtoMapper, never()).mapToDTO(any(Institution.class));

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
        given(institutionRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.getById(contactId);
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToDTO(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService getById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnInstitutionDTOObject() {
        //given - precondition or setup
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));
        given(dtoMapper.mapToDTO(any(Institution.class))).willReturn(synchronizedInstitutionDTO);

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTO = institutionService.getById(contactId);

        //then - verify the output
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService update method (negative scenario - no contact)")
    @Test
    public void givenContactId_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.update(synchronizedInstitutionDTO, contactId);
        });
        //then - verify the output
        verify(institutionRepository, never()).findById(anyLong());
        verify(dtoMapper, never()).mapToEntity(any(InstitutionDTO.class));
        verify(inputCleaner, never()).clean(any(Institution.class));
        verify(institutionRepository, never()).save(any(Institution.class));
        verify(contactRepository, never()).save(any(Contact.class));
        verify(dtoMapper, never()).mapToDTO(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService update method (negative scenario - no institution)")
    @Test
    public void givenObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.of(new Contact()));
        given(institutionRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.update(synchronizedInstitutionDTO, contactId);
        });
        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(InstitutionDTO.class));
        verify(inputCleaner, never()).clean(any(Institution.class));
        verify(institutionRepository, never()).save(any(Institution.class));
        verify(contactRepository, never()).save(any(Contact.class));
        verify(dtoMapper, never()).mapToDTO(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService update method (positive scenario)")
    @Test
    public void givenObject_whenUpdate_thenReturnUpdatedObject() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.of(new Contact()));
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(new Institution()));
        given(dtoMapper.mapToEntity(any(InstitutionDTO.class))).willReturn(new Institution());
        given(inputCleaner.clean(any(Institution.class))).willReturn(new Institution());
        given(institutionRepository.save(any(Institution.class))).willReturn(new Institution());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapToDTO(any(Institution.class))).willReturn(new InstitutionDTO());

        //when - action or the behavior that we are going to test
        InstitutionDTO institutionDTO = institutionService.update(synchronizedInstitutionDTO, contactId);

        //then - verify the output
        verify(contactRepository, times(1)).findById(anyLong());
        verify(institutionRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToEntity(any(InstitutionDTO.class));
        verify(inputCleaner, times(1)).clean(any(Institution.class));
        verify(institutionRepository, times(1)).save(any(Institution.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Institution.class));

    }

    @DisplayName("JUnit test for InstitutionService delete method (negative scenario - no contact)")
    @Test
    public void givenContactId_whenDelete_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.empty());
        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.update(synchronizedInstitutionDTO, contactId);
        });
        //then - verify the output
        verify(institutionRepository, never()).findById(anyLong());
        verify(contactRepository, never()).save(any(Contact.class));
        verify(institutionRepository, never()).deleteById(anyLong());

    }

    @DisplayName("JUnit test for InstitutionService delete method (negative scenario - no institution)")
    @Test
    public void givenObject_whenDelete_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactRepository.findById(contactId)).willReturn(Optional.of(new Contact()));
        given(institutionRepository.findById(anyLong())).willReturn(Optional.empty());
        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.update(synchronizedInstitutionDTO, contactId);
        });
        //then - verify the output
        verify(contactRepository, never()).save(any(Contact.class));
        verify(institutionRepository, never()).deleteById(anyLong());

    }


    @DisplayName("JUnit test for InstitutionService update method (positive Scenario)")
    @Test
    public void givenObject_whenDelete_thenObjectDeleted() {
        //given - precondition or setup
        Contact contact = new Contact();
        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);
        contact.setInstitutions(institutions);

        given(contactRepository.findById(contactId)).willReturn(Optional.of(contact));
        given(institutionRepository.findById(anyLong())).willReturn(Optional.of(institution));

        //when - action or the behavior that we are going to test
        institutionService.delete(contactId, anyLong());

        //then - verify the output
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(institutionRepository, times(1)).deleteById(anyLong());

    }

}