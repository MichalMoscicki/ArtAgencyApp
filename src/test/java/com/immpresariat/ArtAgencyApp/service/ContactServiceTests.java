package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.service.impl.ContactServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTests {

    @Mock
    DTOMapper dtoMapper;

    @Mock
    ContactRepository contactRepository;

    @InjectMocks
    ContactServiceImpl contactService;


   @DisplayName("JUnit test for Contact getAll method")
   @Test
   public void given_whenGetAll_thenReturnListOfContactDTO() {
       //given - precondition or setup
       List<Contact> contacts = new ArrayList<>();
       contacts.add(new Contact());
       given(contactRepository.findAll()).willReturn(contacts);
       given(dtoMapper.mapContactToDTO(any(Contact.class))).willReturn(new ContactDTO());

       //when - action or the behavior that we are going to test
        List<ContactDTO> contactDTOS = contactService.getAll();

       //then - verify the output
       assertNotNull(contactDTOS);
       assertEquals(contactDTOS.size(), contacts.size());
       verify(contactRepository, times(1)).findAll();
       verify(dtoMapper, times(contacts.size())).mapContactToDTO(any(Contact.class));
   }

    @DisplayName("JUnit test for Contact getById method (negative scenario)")
    @Test
    public void givenId_whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        Long id = 0l;
        given(contactRepository.findById(id)).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
                    contactService.getById(id);
                });

        //then - verify the output
    }

    @DisplayName("JUnit test for Contact getById method (positive scenario)")
    @Test
    public void givenId_whenGetById_thenReturnContactDTOObject() {
        //given - precondition or setup
        Long id = 0l;
        Contact contact = new Contact();
        given(contactRepository.findById(id)).willReturn(Optional.of(contact));
        given(dtoMapper.mapContactToDTO(any(Contact.class))).willReturn(new ContactDTO());

        //when - action or the behavior that we are going to test
        ContactDTO contactDTO = contactService.getById(id);

        //then - verify the output
        assertNotNull(contactDTO);
        verify(contactRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapContactToDTO(any(Contact.class));

    }

    @DisplayName("JUnit test for Contact create method")
    @Test
    public void givenUnsyncContactDTO_whenCreate_thenReturnContactDTOObject() {
        //given - precondition or setup
        ContactDTO uncynsContactDTO = ContactDTO.builder()
                .title("Opener Festival")
                .alreadyCooperated(false)
                .build();

        given(dtoMapper.mapDTOToContact(any(ContactDTO.class))).willReturn(new Contact());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapContactToDTO(any(Contact.class))).willReturn(new ContactDTO());


        //when - action or the behavior that we are going to test
        ContactDTO contactDTO = contactService.create(uncynsContactDTO);

        //then - verify the output
        assertNotNull(contactDTO);
        verify(dtoMapper, times(1)).mapDTOToContact(any(ContactDTO.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapContactToDTO(any(Contact.class));
    }

    @DisplayName("JUnit test for Contact Update method (negative Scenario)")
    @Test
    public void givenContactDTO_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        ContactDTO contactDTO = ContactDTO.builder()
                .id(0L)
                .title("Opener Festival")
                .alreadyCooperated(false)
                .build();

        given(contactRepository.findById(contactDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.getById(contactDTO.getId());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapDTOToContact(any(ContactDTO.class));
        verify(contactRepository, never()).save(any(Contact.class));
        verify(dtoMapper, never()).mapContactToDTO(any(Contact.class));
    }


    @DisplayName("JUnit test for Contact Update method (positive Scenario)")
    @Test
    public void givenContactDTO_whenUpdate_thenReturnContactDTOObject() {
        //given - precondition or setup
        ContactDTO contactDTO = ContactDTO.builder()
                .id(0L)
                .title("Opener Festival")
                .alreadyCooperated(false)
                .build();

        given(contactRepository.findById(contactDTO.getId())).willReturn(Optional.of(new Contact()));
        given(dtoMapper.mapDTOToContact(any(ContactDTO.class))).willReturn(new Contact());
        given(contactRepository.save(any(Contact.class))).willReturn(new Contact());
        given(dtoMapper.mapContactToDTO(any(Contact.class))).willReturn(new ContactDTO());


        //when - action or the behavior that we are going to test
        ContactDTO contactDTODb = contactService.update(contactDTO);

        //then - verify the output
        assertNotNull(contactDTODb);
        verify(dtoMapper, times(1)).mapDTOToContact(any(ContactDTO.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapContactToDTO(any(Contact.class));
    }


}


