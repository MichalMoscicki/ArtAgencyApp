package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.ConcertDetailsDTO;
import com.immpresariat.ArtAgencyApp.service.ConcertDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/concerts/{concertId}/details")
public class ConcertDetailsController {

    private final ConcertDetailsService service;

    public ConcertDetailsController(ConcertDetailsService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<ConcertDetailsDTO> create(@RequestBody ConcertDetailsDTO detailsDTO,
                                             @PathVariable Long concertId) {
        return new ResponseEntity<>(service.create(detailsDTO, concertId), HttpStatus.CREATED);
    }

    @PutMapping("/{detailsId}")
    public ResponseEntity<ConcertDetailsDTO> update(@RequestBody ConcertDetailsDTO detailsDTO) {
        return new ResponseEntity<>(service.update(detailsDTO), HttpStatus.OK);
    }

    @GetMapping("/{detailsId}")
    public ResponseEntity<ConcertDetailsDTO> getById(@PathVariable Long detailsId) {
        return new ResponseEntity<>(service.getById(detailsId), HttpStatus.OK);
    }

    @DeleteMapping("/{detailsId}")
    public ResponseEntity<String> delete(@PathVariable Long detailsId) {
        service.deleteById(detailsId);
        return new ResponseEntity<>("Successfully deleted concertDetails with id: " + detailsId, HttpStatus.OK);
    }
}
