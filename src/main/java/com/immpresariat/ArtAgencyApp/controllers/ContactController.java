package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {

    ContactRepository contactRepository;

//    public ContactController(ContactRepository contactRepository) {
//        this.contactRepository = contactRepository;
//    }
//
//    @GetMapping("")
//    public List<Contact> getAll() {
//        return contactRepository.findAll();
//    }

    /*
    ContactDTOService contactDTOService;

    public ContactController(ContactDTOService contactDTOService) {
        this.contactDTOService = contactDTOService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(contactDTOService.getByInstitutionID(id), HttpStatus.OK);
    }

    @GetMapping("")
    public List<ContactDTO> getAll() {
        return contactDTOService.getAll();
    }


 */

}
