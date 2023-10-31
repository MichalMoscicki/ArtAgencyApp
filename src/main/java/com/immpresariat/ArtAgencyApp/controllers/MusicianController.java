package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.MusicianDTO;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.service.MusicianService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/musicians")
public class MusicianController {

    private final MusicianService musicianService;

    public MusicianController(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @PostMapping("")
    public ResponseEntity<MusicianDTO> create(@RequestBody @Valid MusicianDTO musicianDTO) {
        return new ResponseEntity<>(musicianService.create(musicianDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{musicianId}")
    public ResponseEntity<MusicianDTO> getById(@PathVariable Long musicianId) {
        return new ResponseEntity<>(musicianService.getById(musicianId), HttpStatus.OK);
    }

    @GetMapping
    public PageResponse<MusicianDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.LAST_NAME, required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                            @RequestParam(value = "instrument", required = false) String instrument
    ) {
        return musicianService.getAll(pageNo, pageSize, sortBy, sortDir, instrument);
    }

    @PutMapping("/{musicianId}")
    public ResponseEntity<MusicianDTO> update(@RequestBody @Valid MusicianDTO musicianDTO) {
        return new ResponseEntity<>(musicianService.update(musicianDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{musicianId}")
    public ResponseEntity<String> delete(@PathVariable Long musicianId) {
        musicianService.deleteById(musicianId);
        return new ResponseEntity<>("Successfully deleted musician with id: " + musicianId, HttpStatus.OK);
    }
}
