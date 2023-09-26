package com.immpresariat.ArtAgencyApp.controllers;

import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.TaskDTO;
import com.immpresariat.ArtAgencyApp.service.TaskService;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.create(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public PageResponse<TaskDTO> getAll(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return taskService.getAll(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/active")
    public PageResponse<TaskDTO> getActive(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                  @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return taskService.getActive(pageNo, pageSize, sortBy, sortDir);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@RequestBody @Valid TaskDTO taskDTO,
                                          @PathVariable Long id) {
        return new ResponseEntity<>(taskService.update(taskDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return new ResponseEntity<>("Successfully deleted task with id: " + id, HttpStatus.OK);
    }

}
