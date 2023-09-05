package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {

    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("")
    public List<ContactDTO> getAll() {
        return contactService.getAll();
    }

    @PostMapping("")
    public ResponseEntity<ContactDTO> create(@RequestBody ContactDTO unsyncContactDTO) {
        return new ResponseEntity<>(contactService.create(unsyncContactDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@RequestBody ContactDTO contactDTO) {
        return new ResponseEntity<>(contactService.update(contactDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(contactService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        contactService.deleteWithAssociatedData(id);
        return new ResponseEntity<>("Successfully deleted contact with id: " + id, HttpStatus.OK);
    }

}
