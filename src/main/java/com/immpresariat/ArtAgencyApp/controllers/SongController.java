package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.SongDTO;
import com.immpresariat.ArtAgencyApp.service.SongService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping("")
    public ResponseEntity<SongDTO> create(@RequestBody @Valid SongDTO songDTO) {
        return new ResponseEntity<>(songService.create(songDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{songId}")
    public ResponseEntity<SongDTO> getById(@PathVariable Long songId) {
        return new ResponseEntity<>(songService.getById(songId), HttpStatus.OK);
    }

    @GetMapping
    public PageResponse<SongDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.TITLE, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                        @RequestParam(value = "title", required = false) String title
    ) {
        if(title != null){
            title = title.toLowerCase();
        }
        return songService.getAll(pageNo, pageSize, sortBy, sortDir, title);
    }

    @PatchMapping("/{songId}")
    public ResponseEntity<SongDTO> update(@RequestBody @Valid SongDTO songDTO) {
        return new ResponseEntity<>(songService.update(songDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<String> delete(@PathVariable Long songId) {
        songService.deleteById(songId);
        return new ResponseEntity<>("Successfully deleted song with id: " + songId, HttpStatus.OK);
    }

}
