package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.payload.TaskDTO;

public interface TaskService {

    TaskDTO create(TaskDTO unsyncTaskDTO);

    TaskDTO update(TaskDTO taskDTO);

    TaskDTO getById(Long id);

    void deleteById(Long id);

    //getAll
    //GetActive


}
