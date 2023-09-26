package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.TaskDTO;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import com.immpresariat.ArtAgencyApp.service.TaskAttachmentService;
import com.immpresariat.ArtAgencyApp.service.TaskService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskAttachmentService attachmentService;
    private final DTOMapper dtoMapper;
    private final InputCleaner inputCleaner;

    public TaskServiceImpl(TaskRepository taskRepository, TaskAttachmentService attachmentService, DTOMapper dtoMapper, InputCleaner inputCleaner) {
        this.taskRepository = taskRepository;
        this.attachmentService = attachmentService;
        this.dtoMapper = dtoMapper;
        this.inputCleaner = inputCleaner;
    }

    @Override
    public TaskDTO create(TaskDTO unsyncTaskDTO) {
        unsyncTaskDTO.setUpdated(new Date());
        Task syncTask = taskRepository.save(inputCleaner.clean(dtoMapper.mapToEntity(unsyncTaskDTO)));
        return dtoMapper.mapToDTO(syncTask);
    }

    @Override
    public TaskDTO getById(Long taskId) {
        return dtoMapper.mapToDTO(ensureTaskExists(taskId));
    }

    @Override
    public PageResponse<TaskDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Task> page = taskRepository.findAll(pageable);
        List<Task> tasks = page.getContent();
        List<TaskDTO> content =  tasks.stream().map(dtoMapper::mapToDTO).toList();
        return PageResponse.createResponse(page, content);
    }

    @Override
    public PageResponse<TaskDTO> getActive(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Task> page = taskRepository.findAllByActiveIsTrue(pageable);
        List<Task> tasks = page.getContent();
        List<TaskDTO> content =  tasks.stream().map(dtoMapper::mapToDTO).toList();
        return PageResponse.createResponse(page, content);
    }

    @Override
    public TaskDTO update(TaskDTO updatedTaskDTO, Long id) {
        ensureTaskExists(id);
        updatedTaskDTO.setUpdated(new Date());
        Task syncTask = taskRepository.save(inputCleaner.clean(dtoMapper.mapToEntity(updatedTaskDTO)));
        return dtoMapper.mapToDTO(syncTask);
    }

    @Override
    public String deleteById(Long taskId) {
        Task task = ensureTaskExists(taskId);
        if(task.getAttachment() != null){
            deleteAttachments(task);
        }
        taskRepository.deleteById(taskId);
        return "Successfully deleted task with id: " + taskId;
    }



     private void deleteAttachments(Task task) {
        TaskAttachment attachment = task.getAttachment();
        task.setAttachment(null);
        taskRepository.save(task);
        attachmentService.deleteById(attachment.getId());
    }

    private Task ensureTaskExists(Long taskId) {
        return taskRepository.findById(taskId).
                orElseThrow(() -> new ResourceNotFoundException("No task with id: " + taskId));
    }

    private static Sort createSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
}
