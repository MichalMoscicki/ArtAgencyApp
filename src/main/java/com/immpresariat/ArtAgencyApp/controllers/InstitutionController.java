package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<InstitutionDTO> getAll(){
        return institutionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getById(@PathVariable Long id){
        return new ResponseEntity<>(institutionService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<InstitutionDTO> create(@RequestBody InstitutionDTO unsynchronizedDTO){
        return new ResponseEntity<>(institutionService.create(unsynchronizedDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> update(@RequestBody InstitutionDTO institutionDTO){
        return new ResponseEntity<>(institutionService.update(institutionDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id){
        institutionService.deleteWithAssociatedData(id);
        return new ResponseEntity<>(String.format("Institution with id: %s deleted successfully", id), HttpStatus.OK);
    }



}
