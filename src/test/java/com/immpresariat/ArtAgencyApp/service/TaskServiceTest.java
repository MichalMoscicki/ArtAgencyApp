package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.payload.TaskDTO;
import com.immpresariat.ArtAgencyApp.payload.TaskResponse;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import com.immpresariat.ArtAgencyApp.service.impl.TaskServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.AppConstants;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    TaskRepository taskRepository;
    @Mock
    InputCleaner inputCleaner;
    @InjectMocks
    TaskServiceImpl taskService;

    @DisplayName("JUnit test for Task create method")
    @Test
    public void givenUnsyncTaskDTO_whenCreate_thenReturnTaskDTOObject() {
        //given - precondition or setup
        given(dtoMapper.mapToEntity(any(TaskDTO.class))).willReturn(new Task());
        given(inputCleaner.clean(any(Task.class))).willReturn(new Task());
        given(taskRepository.save(any(Task.class))).willReturn(new Task());
        given(dtoMapper.mapToDTO(any(Task.class))).willReturn(new TaskDTO());

        //when - action or the behavior that we are going to test
        TaskDTO taskDTO = taskService.create(new TaskDTO());

        //then - verify the output
        assertNotNull(taskDTO);
        verify(dtoMapper, times(1)).mapToEntity(any(TaskDTO.class));
        verify(inputCleaner, times(1)).clean(any(Task.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Task.class));
    }

    @DisplayName("JUnit test for Task getById method (negative scenario)")
    @Test
    public void whenGetById_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getById(anyLong());
        });

        //then - verify the output
    }


    @DisplayName("JUnit test for Task getById method (positive scenario)")
    @Test
    public void whenGetById_thenReturnTaskObject() {
        //given - precondition or setup
        given(taskRepository.findById(anyLong())).willReturn(Optional.of(new Task()));
        given(dtoMapper.mapToDTO(any(Task.class))).willReturn(new TaskDTO());


        //when - action or the behavior that we are going to test
        TaskDTO taskDTO = taskService.getById(anyLong());

        //then - verify the output
        assertNotNull(taskDTO);
        verify(taskRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToDTO(any(Task.class));
    }


    @DisplayName("JUnit test for getAll method")
    @Test
    public void given_whenGetAll_thenReturnListOfTaskDTO() {
        //given - precondition or setup
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.DEFAULT_SORT_BY).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Task> mockPage = new PageImpl<>(Collections.singletonList(new Task()));
        List<TaskDTO> mockContent = Collections.singletonList(new TaskDTO());

        when(taskRepository.findAll(pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Task.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        TaskResponse result = taskService.getAll(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION);

        //then - verify the output
        assertEquals(mockContent, result.getContent());
    }

    @DisplayName("JUnit test for getActive method")
    @Test
    public void given_whenGetActive_thenReturnListOfTaskDTO() {
        //given - precondition or setup
        int pageNo = 0;
        int pageSize = 10;
        Sort sort = Sort.by(AppConstants.DEFAULT_SORT_BY).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Task> mockPage = new PageImpl<>(Collections.singletonList(new Task()));
        List<TaskDTO> mockContent = Collections.singletonList(new TaskDTO());

        when(taskRepository.findAllByActiveIsTrue(pageable)).thenReturn(mockPage);
        when(dtoMapper.mapToDTO(any(Task.class))).thenReturn(mockContent.get(0));

        //when - action or the behavior that we are going to test
        TaskResponse result = taskService.getActive(pageNo, pageSize, AppConstants.DEFAULT_SORT_BY, AppConstants.DEFAULT_SORT_DIRECTION);

        //then - verify the output
        assertEquals(mockContent, result.getContent());
    }
    @DisplayName("JUnit test for Task getById method (negative scenario)")
    @Test
    public void givenContactDTOObject_whenUpdate_thenThrowResourceNotFoundException() {
        //given - precondition or setup
        TaskDTO taskDTO = TaskDTO.builder()
                .id(0L)
                .title("TestTask")
                .priority(1)
                .build();

        given(taskRepository.findById(taskDTO.getId())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(taskDTO, taskDTO.getId());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToEntity(any(TaskDTO.class));
        verify(inputCleaner, never()).clean(any(Task.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(dtoMapper, never()).mapToDTO(any(Task.class));
    }

    @DisplayName("JUnit test for Task getById method (positive scenario)")
    @Test
    public void givenContactDTOObject_whenUpdate_thenReturnUpdatedObject() {
        //given - precondition or setup
        TaskDTO taskDTO = TaskDTO.builder()
                .id(0L)
                .title("TestTask")
                .priority(1)
                .build();

        given(taskRepository.findById(taskDTO.getId())).willReturn(Optional.of(new Task()));
        given(dtoMapper.mapToEntity(taskDTO)).willReturn(new Task());
        given(inputCleaner.clean(any(Task.class))).willReturn(new Task());
        given(taskRepository.save(any(Task.class))).willReturn(new Task());
        given(dtoMapper.mapToDTO(any(Task.class))).willReturn(new TaskDTO());

        //when - action or the behavior that we are going to test
        TaskDTO taskDTODb = taskService.update(taskDTO, taskDTO.getId());

        //then - verify the output
        verify(dtoMapper, times(1)).mapToEntity(any(TaskDTO.class));
        verify(inputCleaner, times(1)).clean(any(Task.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(dtoMapper, times(1)).mapToDTO(any(Task.class));
    }

    @DisplayName("JUnit test for TaskService delete method")
    @Test
    public void givenId_whenDeleteById_thenTaskDeleted() {
        //given - precondition or setup
        Long id = 0L;
        doNothing().when(taskRepository).deleteById(id);

        //when - action or the behavior that we are going to test
        taskService.deleteById(id);

        //then - verify the output
        verify(taskRepository, times(1)).deleteById(id);
    }
}
