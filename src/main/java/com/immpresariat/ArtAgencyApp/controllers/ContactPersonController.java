package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactPersonDTO;
import com.immpresariat.ArtAgencyApp.service.ContactPersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contacts/{contactId}/contact-people")
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    public ContactPersonController(ContactPersonService contactPersonService) {
        this.contactPersonService = contactPersonService;
    }

    @PostMapping()
    public ResponseEntity<ContactPersonDTO> create(@PathVariable Long contactId,
                                           @RequestBody ContactPersonDTO contactPersonDTO){
        return new ResponseEntity<>(contactPersonService.create(contactPersonDTO, contactId), HttpStatus.CREATED);
    }

    @GetMapping("{contactPersonId}")
    public ResponseEntity<ContactPersonDTO> getById(@PathVariable Long contactPersonId){
        return new ResponseEntity<>(contactPersonService.getById(contactPersonId), HttpStatus.OK);
    }

    @PutMapping("{contactPersonId}")
    public ResponseEntity<ContactPersonDTO> update(@PathVariable Long contactId,
                                                   @RequestBody ContactPersonDTO contactPersonDTO){
        return new ResponseEntity<>(contactPersonService.update(contactPersonDTO, contactId), HttpStatus.OK);
    }


    @DeleteMapping("{contactPersonId}")
    public ResponseEntity<String> delete(@PathVariable Long contactPersonId,
                                         @PathVariable Long contactId){
        contactPersonService.delete(contactPersonId, contactId);
        return new ResponseEntity<>("   Successfully deleted contactPerson with id: " + contactPersonId, HttpStatus.OK);
    }

}
