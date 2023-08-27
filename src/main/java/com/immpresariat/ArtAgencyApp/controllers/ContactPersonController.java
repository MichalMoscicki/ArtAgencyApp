package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.payload.EventDTO;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    public ContactPersonController(ContactPersonService contactPersonService) {
        this.contactPersonService = contactPersonService;
    }

    @PostMapping("/api/v1/contacts/{contactId}/contact-people")
    public ResponseEntity<ContactPersonDTO> create(@PathVariable Long contactId,
                                           @RequestBody ContactPersonDTO contactPersonDTO){
        return new ResponseEntity<>(contactPersonService.create(contactPersonDTO, contactId), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/contact-people/{contactPersonId}")
    public ResponseEntity<ContactPersonDTO> getById(@PathVariable Long contactPersonId){
        return new ResponseEntity<>(contactPersonService.getById(contactPersonId), HttpStatus.OK);
    }

    @PutMapping("/api/v1/contact-people/{contactPersonId}")
    public ResponseEntity<ContactPersonDTO> update(@RequestBody ContactPersonDTO contactPersonDTO){
        return new ResponseEntity<>(contactPersonService.update(contactPersonDTO), HttpStatus.OK);
    }


    @DeleteMapping("/api/v1/contact-people/{contactPersonId}")
    public ResponseEntity<String> delete(@PathVariable Long contactPersonId){
        contactPersonService.delete(contactPersonId);
        return new ResponseEntity<>("   Successfully deleted contactPerson with id: " + contactPersonId, HttpStatus.OK);
    }

}
