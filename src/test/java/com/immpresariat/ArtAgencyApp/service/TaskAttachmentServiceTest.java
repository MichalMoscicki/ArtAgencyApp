package com.immpresariat.ArtAgencyApp.service;

import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.TaskAttachmentDTO;
import com.immpresariat.ArtAgencyApp.repository.TaskAttachmentRepository;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import com.immpresariat.ArtAgencyApp.service.impl.TaskAttachmentServiceImpl;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskAttachmentServiceTest {

    @Mock
    DTOMapper dtoMapper;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskAttachmentRepository attachmentRepository;
    @InjectMocks
    TaskAttachmentServiceImpl attachmentService;

    @DisplayName("JUnit test for create method (negative scenario)")
    @Test
    public void givenUnsyncDTO_whenCreate_throwResourceNotFoundException() {
        //given - precondition or setup
        given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            attachmentService.create(new TaskAttachmentDTO(), anyLong());
        });

        //then - verify the output
        verify(taskRepository, times(1)).findById(anyLong());
        verify(dtoMapper, never()).mapToEntity(any(TaskAttachmentDTO.class));
        verify(attachmentRepository, never()).save(any(TaskAttachment.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(dtoMapper, never()).mapToDTO(any(TaskAttachment.class));
    }


    @DisplayName("JUnit test for create method (positive scenario)")
    @Test
    public void givenUnsyncDTO_whenCreate_thenReturnDTO() {
        //given - precondition or setup
        TaskAttachmentDTO attachmentDTO = new TaskAttachmentDTO();
        TaskAttachment attachment = new TaskAttachment();

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(new Task()));
        given(dtoMapper.mapToEntity(attachmentDTO)).willReturn(attachment);
        given(attachmentRepository.save(attachment)).willReturn(attachment);
        given(taskRepository.save(any(Task.class))).willReturn(new Task());
        given(dtoMapper.mapToDTO(attachment)).willReturn(attachmentDTO);

        //when - action or the behavior that we are going to test
        TaskAttachmentDTO taskDTODb = attachmentService.create(attachmentDTO, anyLong());

        //then - verify the output
        verify(taskRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToEntity(any(TaskAttachmentDTO.class));
        verify(attachmentRepository, times(1)).save(any(TaskAttachment.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(dtoMapper, times(1)).mapToDTO(any(TaskAttachment.class));

    }

    @DisplayName("JUnit test for getByIdMethod (negative scenario)")
    @Test
    public void getByIdShouldThrowException() {
        //given - precondition or setup
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            attachmentService.getById(anyLong());
        });

        //then - verify the output
        verify(dtoMapper, never()).mapToDTO(any(TaskAttachment.class));
    }

    @DisplayName("JUnit test for getByIdMethod (positive scenario)")
    @Test
    public void getByIdShouldExecuteAllMethods() {
        //given - precondition or setup
        TaskAttachmentDTO attachmentDTO = new TaskAttachmentDTO();
        TaskAttachment attachment = new TaskAttachment();
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.of(attachment));
        given(dtoMapper.mapToDTO(attachment)).willReturn(attachmentDTO);

        //when - action or the behavior that we are going to test
        TaskAttachmentDTO taskDTODb = attachmentService.getById(anyLong());


        //then - verify the output
        verify(dtoMapper, times(1)).mapToDTO(any(TaskAttachment.class));
        verify(dtoMapper, times(1)).mapToDTO(any(TaskAttachment.class));

    }


    @DisplayName("JUnit test for update (negative scenario)")
    @Test
    public void updateTaskShouldThrowException() {
        //given - precondition or setup
        given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            attachmentService.update(new TaskAttachmentDTO(), anyLong());
        });

        //then - verify the output
        verify(taskRepository, times(1)).findById(anyLong());
        verify(attachmentRepository, never()).findById(anyLong());
        verify(dtoMapper, never()).mapToEntity(any(TaskAttachmentDTO.class));
        verify(attachmentRepository, never()).save(any(TaskAttachment.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(dtoMapper, never()).mapToDTO(any(TaskAttachment.class));
    }

    @DisplayName("JUnit test for update (negative scenario)")
    @Test
    public void updateTaskAttachmentShouldThrowException() {
        //given - precondition or setup
        TaskAttachmentDTO attachmentDTO = new TaskAttachmentDTO();
        attachmentDTO.setId(0L);

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(new Task()));
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            attachmentService.update(attachmentDTO, anyLong());
        });

        //then - verify the output
        verify(taskRepository, times(1)).findById(anyLong());
        verify(attachmentRepository, times(1)).findById(anyLong());
        verify(dtoMapper, never()).mapToEntity(any(TaskAttachmentDTO.class));
        verify(attachmentRepository, never()).save(any(TaskAttachment.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(dtoMapper, never()).mapToDTO(any(TaskAttachment.class));
    }

    @DisplayName("JUnit test for getByIdMethod (positive scenario)")
    @Test
    public void updateShouldExecuteAllMethods() {
        //given - precondition or setup
        Task task = new Task();
        TaskAttachmentDTO attachmentDTO = new TaskAttachmentDTO();
        attachmentDTO.setId(0L);
        TaskAttachment attachment = new TaskAttachment();

        given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.of(attachment));
        given(dtoMapper.mapToEntity(attachmentDTO)).willReturn(attachment);
        given(attachmentRepository.save(any(TaskAttachment.class))).willReturn(attachment);
        given(taskRepository.save(any(Task.class))).willReturn(task);
        given(dtoMapper.mapToDTO(attachment)).willReturn(attachmentDTO);

        //when - action or the behavior that we are going to test
        TaskAttachmentDTO taskDTODb = attachmentService.update(attachmentDTO, anyLong());

        //then - verify the output
        verify(taskRepository, times(1)).findById(anyLong());
        verify(attachmentRepository, times(1)).findById(anyLong());
        verify(dtoMapper, times(1)).mapToEntity(any(TaskAttachmentDTO.class));
        verify(attachmentRepository, times(1)).save(any(TaskAttachment.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(dtoMapper, times(1)).mapToDTO(any(TaskAttachment.class));

    }

    @DisplayName("JUnit test for delete (negative scenario)")
    @Test
    public void deleteTaskShouldThrowException() {
        //given - precondition or setup
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when - action or the behavior that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> {
            attachmentService.deleteById(anyLong());
        });

        //then - verify the output
        verify(attachmentRepository, times(1)).findById(anyLong());
        verify(attachmentRepository, never()).save(any(TaskAttachment.class));
        verify(attachmentRepository, never()).deleteById(anyLong());
    }

    @DisplayName("JUnit test for delete (negative scenario)")
    @Test
    public void deleteShouldExecuteAllMethods() {
        //given - precondition or setup
        given(attachmentRepository.findById(anyLong())).willReturn(Optional.of(new TaskAttachment()));

        //when - action or the behavior that we are going to test
        attachmentService.deleteById(anyLong());


        //then - verify the output
        verify(attachmentRepository, times(1)).findById(anyLong());
        verify(attachmentRepository, times(1)).save(any(TaskAttachment.class));
        verify(attachmentRepository, times(1)).deleteById(anyLong());
    }
}
