package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.models.Instrument;
import com.immpresariat.ArtAgencyApp.models.Part;
import com.immpresariat.ArtAgencyApp.service.PartService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class PartController {

    private final PartService partService;
    private final ObjectMapper objectMapper;

    public PartController(PartService partService,
                          ObjectMapper objectMapper) {
        this.partService = partService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/api/v1/songs/{songId}/parts")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("instrument") String instrumentJson,
            @PathVariable Long songId) throws JsonProcessingException {

        Instrument instrument = objectMapper.readValue(instrumentJson, Instrument.class);
        String message;

        try {
            partService.store(file, instrument, songId);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            if(e instanceof ResourceAlreadyExistsException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
            }
            e.printStackTrace();
            // here error could be logged
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/api/v1/parts/{partId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long partId) {
        Part part = partService.getById(partId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + part.getName() + "\"")
                .body(part.getData());

    }

    @DeleteMapping("/api/v1/songs/{songId}/parts/{partId}")
    public ResponseEntity<String> delete(@PathVariable Long songId,
                                         @PathVariable Long partId) {

        try {
            partService.deleteById(partId, songId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Successfully deleted part with id: " + partId, HttpStatus.OK);
    }

}







