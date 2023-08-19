package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {

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
