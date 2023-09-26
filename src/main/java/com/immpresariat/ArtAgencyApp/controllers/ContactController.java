package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ContactDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.service.ContactService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {

    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("")
    public PageResponse<ContactDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
                                   ) {
        return contactService.getAll(pageNo, pageSize, sortBy, sortDir);
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
