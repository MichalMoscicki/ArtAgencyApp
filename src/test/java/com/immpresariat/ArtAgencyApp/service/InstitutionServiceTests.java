package com.immpresariat.ArtAgencyApp.service;
import  static org.junit.jupiter.api.Assertions.assertThrows;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.impl.InstitutionServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InstitutionServiceTests {

    @Mock
    private InstitutionRepository institutionRepository;
    @InjectMocks
    private InstitutionServiceImpl institutionService;
    private Institution institution;

    @BeforeEach
    public void setup() {
        institution = Institution.builder().
                id(1L)
                .name("DK Łomianki")
                .city("Łomianki")
                .alreadyCooperated(true)
                .notes("Graliśmy tam z Marią")
                .build();
    }

    @DisplayName("JUnit test for save institution method")
    @Test
    public void givenInstitutionObject_whenSaveInstitution_thenReturnInstitutionObjcet() {
        //given - precondition or setup
        given(institutionRepository.save(institution)).willReturn(institution);

        //when - action or the behavior that we are going to test
        given(institutionRepository.findInstitutionByNameAndCity(institution.getName(), institution.getCity())).willReturn(Optional.empty());
        Institution institutionDB = institutionService.create(institution);

        //then - verify the output
        assertThat(institutionDB).isNotNull();
        assertThat(institutionDB).isEqualTo(institution);

    }

    @DisplayName("JUnit test for save institution method that throws an exception")
    @Test
    public void givenInstitutionObjecttt_whenSaveInstitution_thenReturnInstitutionObjcet() {
        //given - precondition or setup
        given(institutionRepository.findInstitutionByNameAndCity(institution.getName(), institution.getCity())).willReturn(Optional.of(institution));

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            institutionService.create(institution);
        });

        //then - verify the output
        Mockito.verify(institutionRepository, never()).save(any(Institution.class));

    }


}
