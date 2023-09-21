package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.ContactResponse;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.service.impl.ContactServiceImpl;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTests {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    ContactRepository contactRepository;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    ContactServiceImpl contactService;

    @DisplayName("JUnit test for Contact getAll method")
    @Test
    public void given_whenGetAll_thenReturnListOfContactDTO() {
        //given - precondition or setup
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.DEFAULT_SORT_BY).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Contact> mockPage = new PageImpl<>(Collections.singletonList(new Contact()));
        List<ContactDTO> mockContent = Collections.singletonList(new ContactDTO());

        when(contactRepository.findAll(pageable)).thenReturn(mockPage);
        when(dtoMapper.mapContactToDTO(any(Contact.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        ContactResponse result = contactService.getAll(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION);

        //then - verify the output
        assertEquals(mockContent, result.getContent());
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
        given(inputCleaner.clean(any(Contact.class))).willReturn(new Contact());


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
        given(inputCleaner.clean(any(Contact.class))).willReturn(new Contact());


        //when - action or the behavior that we are going to test
        ContactDTO contactDTODb = contactService.update(contactDTO);

        //then - verify the output
        assertNotNull(contactDTODb);
        verify(dtoMapper, times(1)).mapDTOToContact(any(ContactDTO.class));
        verify(contactRepository, times(1)).save(any(Contact.class));
        verify(dtoMapper, times(1)).mapContactToDTO(any(Contact.class));
    }


}


