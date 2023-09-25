package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.TaskDTO;
import com.immpresariat.ArtAgencyApp.payload.TaskResponse;

public interface TaskService {

    TaskDTO create(TaskDTO unsyncTaskDTO);

    TaskDTO getById(Long id);

    TaskResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskResponse getActive(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskDTO update(TaskDTO taskDTO, Long id);

    String deleteById(Long id);


}
