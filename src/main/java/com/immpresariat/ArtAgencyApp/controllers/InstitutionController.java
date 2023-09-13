package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/contacts/{contactId}/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }


    @PostMapping
    public ResponseEntity<InstitutionDTO> create(@RequestBody InstitutionDTO unsynchronizedDTO,
                                                 @PathVariable Long contactId){
        return new ResponseEntity<>(institutionService.create(unsynchronizedDTO, contactId), HttpStatus.CREATED);
    }


    @GetMapping("{institutionId}")
    public ResponseEntity<InstitutionDTO> getById(@PathVariable Long institutionId){
        return new ResponseEntity<>(institutionService.getById(institutionId), HttpStatus.OK);
    }


    @PutMapping("{institutionId}")
    public ResponseEntity<InstitutionDTO> update(@PathVariable Long contactId,
                                                 @RequestBody InstitutionDTO institutionDTO){
        return new ResponseEntity<>(institutionService.update(institutionDTO, contactId), HttpStatus.OK);
    }


    @DeleteMapping("{institutionId}")
    public ResponseEntity<String> update(@PathVariable Long institutionId,
                                         @PathVariable Long contactId){
        institutionService.delete(institutionId, contactId);
        return new ResponseEntity<>("Successfully deleted institution with id: " + institutionId, HttpStatus.OK);
    }



}
