package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.service.ContactDTOService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {


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

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateById(@PathVariable Long id,
                                                 @Valid @RequestBody ContactDTO updatedContactDTO) {
        return new ResponseEntity<>(contactDTOService.update(updatedContactDTO), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ContactDTO> create(@Valid @RequestBody ContactDTO contactDTO) {
        return new ResponseEntity<>(contactDTOService.create(contactDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        contactDTOService.deleteById(id);
        return new ResponseEntity<>("Contact deleted succesfully", HttpStatus.OK);
    }

}
