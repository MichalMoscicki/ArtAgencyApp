package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }


    @PostMapping("/api/v1/contacts/{contactId}/institutions")
    public ResponseEntity<InstitutionDTO> create(@RequestBody InstitutionDTO unsynchronizedDTO,
                                                 @PathVariable Long contactId){
        return new ResponseEntity<>(institutionService.create(unsynchronizedDTO, contactId), HttpStatus.CREATED);
    }


    @GetMapping("/api/v1/institutions/{institutionId}")
    public ResponseEntity<InstitutionDTO> getById(@PathVariable Long institutionId){
        return new ResponseEntity<>(institutionService.getById(institutionId), HttpStatus.OK);
    }


    @PutMapping("/api/v1/institutions/{institutionId} ")
    public ResponseEntity<InstitutionDTO> update(@RequestBody InstitutionDTO institutionDTO){
        return new ResponseEntity<>(institutionService.update(institutionDTO), HttpStatus.OK);
    }


    @DeleteMapping("/api/v1/institutions/{institutionId}")
    public ResponseEntity<String> update(@PathVariable Long institutionId){
        institutionService.deleteById(institutionId);
        return new ResponseEntity<>("Successfully deleted institution with id: " + institutionId, HttpStatus.OK);
    }



}
