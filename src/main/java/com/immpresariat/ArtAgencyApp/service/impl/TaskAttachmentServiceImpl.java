package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.TaskAttachmentDTO;
import com.immpresariat.ArtAgencyApp.repository.TaskAttachmentRepository;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import com.immpresariat.ArtAgencyApp.service.TaskAttachmentService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskAttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final DTOMapper dtoMapper;

    public TaskAttachmentServiceImpl(TaskAttachmentRepository attachmentRepository,
                                     TaskRepository taskRepository,
                                     DTOMapper dtoMapper) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public TaskAttachmentDTO create(TaskAttachmentDTO unsyncAttachmentDTO, Long taskId) {
        Task task = ensureTaskExists(taskId);
        TaskAttachment syncAttachment = attachmentRepository.save(dtoMapper.mapToEntity(unsyncAttachmentDTO));
        task.setAttachment(syncAttachment);
        taskRepository.save(task);
        return dtoMapper.mapToDTO(syncAttachment);
    }

    @Override
    public TaskAttachmentDTO getById(Long attachmentId) {
        return dtoMapper.mapToDTO(ensureAttachmentExists(attachmentId));
    }

    @Override
    public TaskAttachmentDTO update(TaskAttachmentDTO updatedAttachmentDTO, Long taskId) {
        Task task = ensureTaskExists(taskId);
        ensureAttachmentExists(updatedAttachmentDTO.getId());
        TaskAttachment syncAttachment = attachmentRepository.save(dtoMapper.mapToEntity(updatedAttachmentDTO));
        task.setAttachment(syncAttachment);
        taskRepository.save(task);
        return dtoMapper.mapToDTO(syncAttachment);
    }

    @Override
    public void deleteById(Long id) {

        TaskAttachment attachment = ensureAttachmentExists(id);
        attachment.setContacts(null);
        attachmentRepository.save(attachment);
        attachmentRepository.deleteById(id);
    }

    TaskAttachment ensureAttachmentExists(Long id) {
        return attachmentRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("No taskAttachment with id: " + id));
    }

    Task ensureTaskExists(Long id) {
        return taskRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("No task with id: " + id));
    }
}
