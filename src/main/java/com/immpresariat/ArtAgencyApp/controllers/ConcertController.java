package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.*;
import com.immpresariat.ArtAgencyApp.service.ConcertService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/concerts")
public class ConcertController {

    ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("")
    public PageResponse<ConcertDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_DATE, required = false) String sortBy,
                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return concertService.getAll(pageNo, pageSize, sortBy, sortDir);
    }

    @PostMapping("")
    public ResponseEntity<ConcertDTO> create(@RequestBody ConcertDTO unsyncConcerttDTO) {
        return new ResponseEntity<>(concertService.create(unsyncConcerttDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConcertDTO> update(@RequestBody ConcertDTO concertDTO,
                                             @PathVariable Long id) {
        return new ResponseEntity<>(concertService.update(concertDTO, id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(concertService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        concertService.deleteById(id);
        return new ResponseEntity<>("Successfully deleted concert with id: " + id, HttpStatus.OK);
    }
}
