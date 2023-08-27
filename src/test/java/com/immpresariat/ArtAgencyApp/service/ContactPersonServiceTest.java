package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactPersonRepository;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.impl.ContactPersonServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactPersonServiceTest {

    @Mock
    ContactPersonRepository contactPersonRepository;
    @Mock
    ContactRepository contactRepository;
    @Mock
    DTOMapper dtoMapper;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    ContactPersonServiceImpl contactPersonService;

    ContactPerson contactPerson;
    ContactPersonDTO unsyncContactPersonDTO;
    ContactPersonDTO synchronizedContactPersonDTO;

    @BeforeEach
    public void setup() {

        contactPerson = ContactPerson.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .role("dyrektor")
                .email("jan@gmail.com")
                .phone("+48777666555")
                .build();

        unsyncContactPersonDTO = ContactPersonDTO.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .role("dyrektor")
                .email("jan@gmail.com")
                .phone("+48777666555")
                .build();

        synchronizedContactPersonDTO = ContactPersonDTO.builder()
                .id(0L)
                .firstName("Jan")
                .lastName("Kowalski")
                .role("dyrektor")
                .email("jan@gmail.com")
                .phone("+48777666555")
                .build();

    }

    @DisplayName("JUnit test for contactPersonService create method (negative scenario - no institution)")
    @Test
    public void givenInstitutionId_whenCreate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long contactId = 0L;
        given(contactRepository.findById(contactId)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            contactPersonService.create(unsyncContactPersonDTO, contactId);
        });

        //then - verify the output
        Mockito.verify(contactPersonRepository, never()).save(any(ContactPerson.class));

    }

    @DisplayName("JUnit test for EventService create method (positive scenario) ")
    @Test
    public void givenUnsyncContactPersonDTOObject_whenCreate_thenReturnSynchronizedContactPersonDTOObject() {
        //given - precondition or setup
        Long contactId = 0L;
        given(contactRepository.findById(anyLong())).willReturn(Optional.of(new Contact()));
        given(dtoMapper.mapUnsyncDTOToContactPerson(unsyncContactPersonDTO)).willReturn(new ContactPerson());
        given(inputCleaner.clean(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(contactPersonRepository.save(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapContactPersonToDTO(any(ContactPerson.class))).willReturn(synchronizedContactPersonDTO);

        //when - action or the behavior that we are going to test
        ContactPersonDTO synchronizedContactPersonDTO = contactPersonService.create(unsyncContactPersonDTO, contactId);

        //then - verify the output
        assertNotNull(synchronizedContactPersonDTO);
        verify(contactRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapUnsyncDTOToContactPerson(any(ContactPersonDTO.class));
        verify(inputCleaner, times(1)).clean(any(ContactPerson.class));
        verify(contactPersonRepository, times(1)).save(any(ContactPerson.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapContactPersonToDTO(any(ContactPerson.class));

    }


    @DisplayName("JUnit test for contactPersonService getAll method")
    @Test
    public void whenGetAll_thenReturnEventDTOsList() {
        //given - precondition or setup
        List<ContactPerson> contactPeople = new ArrayList<>();
        contactPeople.add(contactPerson);
        given(contactPersonRepository.findAll()).willReturn(contactPeople);
        given(dtoMapper.mapContactPersonToDTO(any(ContactPerson.class))).willReturn(new ContactPersonDTO());


        //when - action or the behavior that we are going to test
        List<ContactPersonDTO> contactPersonDTOS = contactPersonService.getAll();

        //then - verify the output
        verify(contactPersonRepository, times(1)).findAll();
        verify(dtoMapper, times(contactPeople.size())).mapContactPersonToDTO(any(ContactPerson.class));
        assertEquals(contactPeople.size(), contactPersonDTOS.size());

    }

    @DisplayName("JUnit test for contactPersonService getById method (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(contactPersonRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            contactPersonService.getById(synchronizedContactPersonDTO.getId());
        });

        //then - verify the output

    }

    @DisplayName("JUnit test for contactPersonService getById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnContactPersonDTOObject() {
        //given - precondition or setup
        given(contactPersonRepository.findById(anyLong())).willReturn(Optional.of(contactPerson));
        given(dtoMapper.mapContactPersonToDTO(any(ContactPerson.class))).willReturn(synchronizedContactPersonDTO);

        //when - action or the behavior that we are going to test
        ContactPersonDTO contactPersonDTO = contactPersonService.getById(synchronizedContactPersonDTO.getId());

        //then - verify the output
        verify(contactPersonRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapContactPersonToDTO(any(ContactPerson.class));

    }

    @DisplayName("JUnit test for contactPersonService update method (negative scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        ContactPersonDTO inputDTO = synchronizedContactPersonDTO;
        given(contactPersonRepository.findById(inputDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            contactPersonService.update(inputDTO);
        });
        //then - verify the output

    }

    @DisplayName("JUnit test for contactPersonService update method (positive scenario)")
    @Test
    public void givenEventDTOObject_whenUpdate_thenReturnEventDTO() {
        //given - precondition or setup
        ContactPersonDTO inputDTO = synchronizedContactPersonDTO;
        given(contactPersonRepository.findById(inputDTO.getId())).willReturn(Optional.of(contactPerson));

        given(inputCleaner.clean(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(contactPersonRepository.save(any(ContactPerson.class))).willReturn(new ContactPerson());
        given(dtoMapper.mapContactPersonToDTO(any(ContactPerson.class))).willReturn(synchronizedContactPersonDTO);

        //when - action or the behavior that we are going to test
        ContactPersonDTO contactPersonDTO = contactPersonService.update(synchronizedContactPersonDTO);

        //then - verify the output
        assertNotNull(contactPersonDTO);
        verify(contactPersonRepository, times(1)).findById(anyLong());
        verify(inputCleaner, times(1)).clean(any(ContactPerson.class));
        verify(contactPersonRepository, times(1)).save(any(ContactPerson.class));
        verify(dtoMapper, times(1)).mapContactPersonToDTO(any(ContactPerson.class));

    }

    @DisplayName("JUnit test for contactPersonService delete method")
    @Test
    public void givenId_whenDelete_thenEventDeleted() {
        //given - precondition or setup
        Long id = synchronizedContactPersonDTO.getId();
        doNothing().when(contactPersonRepository).deleteById(id);


        //when - action or the behavior that we are going to test
        contactPersonService.delete(id);

        //then - verify the output
        verify(contactPersonRepository, Mockito.times(1)).deleteById(id);
    }

}
