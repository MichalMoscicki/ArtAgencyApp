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

import static com.immpresariat.ArtAgencyApp.utils.AppConstants.*;

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
    public PageResponse<TaskDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir, String status) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Task> page = createPage(pageable, status);
        List<Task> tasks = page.getContent();
        List<TaskDTO> content = tasks.stream().map(dtoMapper::mapToDTO).toList();
        return PageResponse.createResponse(page, content);
    }

    @Override
    public TaskDTO update(TaskDTO updatedTaskDTO, Long id) {
        ensureTaskExists(id);
        if (updatedTaskDTO.isFinished()) {
            updatedTaskDTO.setActive(false);
        }
        updatedTaskDTO.setUpdated(new Date());
        Task syncTask = taskRepository.save(inputCleaner.clean(dtoMapper.mapToEntity(updatedTaskDTO)));
        return dtoMapper.mapToDTO(syncTask);
    }

    @Override
    public String deleteById(Long taskId) {
        Task task = ensureTaskExists(taskId);
        if (task.getAttachment() != null) {
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

    private Page<Task> createPage(Pageable pageable, String status) {
        Page<Task> page;
        if(status == null){
           return page = taskRepository.findAll(pageable);
        } else if (status.matches(EMPTY_OR_BLANK)){
            throw new IllegalArgumentException("Status must not be empty or blank. Check the path parameter (status=)");
        }

        switch(status.toLowerCase()){
            case ACTIVE -> page = taskRepository.findAllByActiveIsTrueAndFinishedIsFalse(pageable);
            //todo czy zostawić to dla czytelności?:
            case ALL -> page = taskRepository.findAll(pageable);
            case FINISHED -> page = taskRepository.findAllByActiveIsFalseAndFinishedIsTrue(pageable);
            case FUTURE -> page = taskRepository.findAllByActiveIsFalseAndFinishedIsFalse(pageable);
            //status nie może być empty or blank. Zrób tu sprawdzenie w regexie.
            default ->
                    throw new IllegalArgumentException(
                            String.format("Wrong status. Must be: active, all, finished, future (case insensitive) or null. Was: %s", status));
        }
        return page;
    }
}
