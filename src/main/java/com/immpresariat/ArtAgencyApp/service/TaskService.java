package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.PageResponse;
import com.immpresariat.ArtAgencyApp.payload.TaskDTO;

public interface TaskService {

    TaskDTO create(TaskDTO unsyncTaskDTO);

    TaskDTO getById(Long id);

    PageResponse<TaskDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponse<TaskDTO> getActive(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskDTO update(TaskDTO taskDTO, Long id);

    String deleteById(Long id);


}
