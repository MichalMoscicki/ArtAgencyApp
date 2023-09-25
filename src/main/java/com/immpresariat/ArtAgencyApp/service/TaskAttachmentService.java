package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.TaskAttachmentDTO;

public interface TaskAttachmentService {

    TaskAttachmentDTO create(TaskAttachmentDTO unsyncAttachmentDTO, Long taskId);
    TaskAttachmentDTO update(TaskAttachmentDTO updatedAttachmentDTO, Long taskId);
    TaskAttachmentDTO getById(Long attachmentId);
    void deleteById(Long id);
}
