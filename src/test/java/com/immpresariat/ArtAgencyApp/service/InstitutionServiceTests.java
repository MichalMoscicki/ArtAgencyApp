package com.immpresariat.ArtAgencyApp.service;
import  static org.junit.jupiter.api.Assertions.assertThrows;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.impl.InstitutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private EventService eventService;
    @Mock

    private ContactPersonService contactPersonService;


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
    public void givenInstitutionObject_whenSaveInstitution_thenReturnInstitutionObject() {
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
    public void givenExistingNameAndCity_whenSaveInstitution_thenThrowsException() {
        //given - precondition or setup
        given(institutionRepository.findInstitutionByNameAndCity(institution.getName(), institution.getCity())).willReturn(Optional.of(institution));

        //when - action or the behavior that we are going to test
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            institutionService.create(institution);
        });

        //then - verify the output
        Mockito.verify(institutionRepository, never()).save(any(Institution.class));

    }

    @DisplayName("JUnit test for institution getAll method (positive scenario)")
    @Test
    public void givenListOfInstitutions_whenGetAll_thenReturnListOfInstitutions() {
        //given - precondition or setup
        Institution secondInstitution = Institution.builder()
                .id(2L)
                .name("Klub Morświn")
                .city("Świonujćie")
                .build();

        List<Institution> institutions = new ArrayList<>();
        institutions.add(institution);
        institutions.add(secondInstitution);
        given(institutionRepository.findAll()).willReturn(institutions);

        //when - action or the behavior that we are going to test
        List<Institution> institutionsDB = institutionService.getAll();

        //then - verify the output
        assertThat(institutions).isNotNull();
        assertThat(institutionsDB.size()).isEqualTo(institutions.size());

    }

    @DisplayName("JUnit test for institution getAll method (negative scenario)")
    @Test
    public void givenListOfInstitutions_whenGetAll_thenReturnEmptyList() {
        //given - precondition or setup
        List<Institution> institutions = new ArrayList<>();
        given(institutionRepository.findAll()).willReturn(institutions);

        //when - action or the behavior that we are going to test
        List<Institution> institutionsDB = institutionService.getAll();

        //then - verify the output
        assertThat(institutions).isNotNull();
        assertThat(institutionsDB).isEmpty();

    }

    @DisplayName("JUnit test for institution update method (positive scenario)")
    @Test
    public void givenIdAndUpdatedInstitution_whenUpdateInstitution_thenReturnUpdatedInstitutions() {
        //given - precondition or setup
        Long id = institution.getId();
        Institution updatedInstitution = institution;
        updatedInstitution.setName("updated");
        given(institutionRepository.findById(id)).willReturn(Optional.of(institution));
        given(institutionRepository.save(updatedInstitution)).willReturn(updatedInstitution);

        //when - action or the behavior that we are going to test
        Institution updatedInstitutionDb = institutionService.update(id, updatedInstitution);

        //then - verify the output
        assertThat(updatedInstitutionDb).isNotNull();
        assertThat(updatedInstitutionDb).isEqualTo(updatedInstitution);

    }

    @DisplayName("JUnit test for institution update method (negative scenario)")
    @Test
    public void givenInstitutionId_whenUpdateInstitution_thenThrowsException() {
        //given - precondition or setup
        Long id = 2l;
        Institution updatedInstitution = institution;
        updatedInstitution.setName("updated");
        given(institutionRepository.findById(id)).willReturn(Optional.empty());


        //when - action or the behavior that we are going to test
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            institutionService.update(id, updatedInstitution);
        });

        //then - verify the output
        Mockito.verify(institutionRepository, never()).save(any(Institution.class));

    }


    @DisplayName("JUnit test for delete institution method (institution Object with associated data")
    @Test
    public void givenInstitutionWithAssociateData_whenDeleteInstitution_thenInstitutionAndDataDeleted() {
        //given - precondition or setup
        Long institutionId = institution.getId();
        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(new ContactPerson());
        given(contactPersonService.getAllByInstitutionId(institutionId)).willReturn(contactPeople);

        List<Event> events = new ArrayList<>();
        events.add(new Event());
        given(eventService.getAllByInstitutionId(institutionId)).willReturn(events);

        //when - action or the behavior that we are going to test
        institutionService.delete(institutionId);

        //then - verify the output
        Mockito.verify(contactPersonService, times(1)).delete(any(ContactPerson.class));
        Mockito.verify(eventService, times(1)).delete(any(Event.class));
        Mockito.verify(institutionRepository, times(1)).deleteById(institutionId);
    }

    @DisplayName("JUnit test for delete institution method (institution Object without associated data")
    @Test
    public void givenInstitutionWithNoAssociateData_whenDeleteInstitution_thenInstitutionDeleted() {
        //given - precondition or setup
        Long institutionId = institution.getId();
        given(contactPersonService.getAllByInstitutionId(institutionId)).willReturn(new ArrayList<>());
        given(eventService.getAllByInstitutionId(institutionId)).willReturn(new ArrayList<>());

        //when - action or the behavior that we are going to test
        institutionService.delete(institutionId);

        //then - verify the output
        Mockito.verify(contactPersonService, never()).delete(any(ContactPerson.class));
        Mockito.verify(eventService, never()).delete(any(Event.class));
        Mockito.verify(institutionRepository, times(1)).deleteById(institutionId);
    }

}