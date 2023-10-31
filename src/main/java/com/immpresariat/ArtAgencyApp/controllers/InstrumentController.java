package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.InstrumentDTO;
import com.immpresariat.ArtAgencyApp.service.InstrumentService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @PostMapping("")
    public ResponseEntity<InstrumentDTO> create(@RequestBody @Valid InstrumentDTO instrumentDTO) {
        return new ResponseEntity<>(instrumentService.create(instrumentDTO), HttpStatus.CREATED);
    }

    @GetMapping("")
    public List<InstrumentDTO> getAll() {
        return instrumentService.getAll();
    }

    @GetMapping("/{instrumentId}")
    public ResponseEntity<InstrumentDTO> getById(@PathVariable Long instrumentId) {
        return new ResponseEntity<>(instrumentService.getById(instrumentId), HttpStatus.OK);
    }

    @PutMapping("/{instrumentId}")
    public ResponseEntity<InstrumentDTO> update(@RequestBody @Valid InstrumentDTO instrumentDTO) {
        return new ResponseEntity<>(instrumentService.update(instrumentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{instrumentId}")
    public ResponseEntity<String> delete(@PathVariable Long instrumentId) {
        instrumentService.deleteById(instrumentId);
        return new ResponseEntity<>("Successfully deleted instrument with id: " + instrumentId, HttpStatus.OK);
    }

}
