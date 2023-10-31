package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.TaskAttachmentDTO;
import com.immpresariat.ArtAgencyApp.service.TaskAttachmentService;
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
@RequestMapping("api/v1/tasks/{taskId}/attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService attachmentService;

    public TaskAttachmentController(TaskAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public ResponseEntity<TaskAttachmentDTO> create(@RequestBody TaskAttachmentDTO attachmentDTO,
                                                 @PathVariable Long taskId){
        return new ResponseEntity<>(attachmentService.create(attachmentDTO, taskId), HttpStatus.CREATED);

    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<TaskAttachmentDTO> getById(@PathVariable Long attachmentId){
        return new ResponseEntity<>(attachmentService.getById(attachmentId), HttpStatus.OK);

    }

    @PutMapping("/{attachmentId}")
    public ResponseEntity<TaskAttachmentDTO> update(@RequestBody TaskAttachmentDTO attachmentDTO,
                                                    @PathVariable Long taskId){
        return new ResponseEntity<>(attachmentService.update(attachmentDTO, taskId), HttpStatus.OK);

    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<String> delete(@PathVariable Long attachmentId,
                                                    @PathVariable Long taskId){
        attachmentService.deleteById(attachmentId);
        return new ResponseEntity<>("Successfully deleted attachment with id: " + attachmentId, HttpStatus.OK);

    }

}
