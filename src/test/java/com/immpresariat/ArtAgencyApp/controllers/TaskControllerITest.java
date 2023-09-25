package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.TaskDTO;
import com.immpresariat.ArtAgencyApp.repository.*;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskAttachmentRepository taskAttachmentRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
        taskAttachmentRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    public void givenUnsyncDTO_whenCreate_thenReturnSyncDTO() throws Exception {
        //given
        TaskDTO unsyncTaskDTO = dtoMapper.mapTaskToDTO(createTask(null, false));

        //when
        ResultActions response = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncTaskDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.title", CoreMatchers.is(unsyncTaskDTO.getTitle())))
                .andExpect(jsonPath("$.updated", CoreMatchers.notNullValue()));
    }

    @Test
    public void whenGetById_thenThrowResourceNotFoundException() throws Exception {
        //given - precondition or setup
        Long id = 0L;

        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/tasks/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is("No task with id: " + id)));
    }

    @Test
    public void whenGetById_thenReturnContactDTOObject() throws Exception {
        //given - precondition or setup
        Task task = taskRepository.save(createTaskWithAttachments(true));
        Long id = task.getId();


        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/tasks/%s", id)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(id.intValue())))
                .andExpect(jsonPath("$.attachment", CoreMatchers.notNullValue()));
    }


    @Test
    public void givenTaskList_when_getAll_returnTasks() throws Exception {
        taskRepository.save(createTask(null, false));
        taskRepository.save(createTask(null, true));

        ResultActions response = mockMvc.perform(get("/api/v1/tasks"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(2)));

    }

    @Test
    public void givenTaskList_whenGetActive_returnActive() throws Exception {
        Task task1 = taskRepository.save(createTask(null, false));
        Task task2 = taskRepository.save(createTask(null, true));

        ResultActions response = mockMvc.perform(get("/api/v1/tasks/active"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(1)));

    }

    @Test
    public void givenId_whenUpdate_thanThrowResourceNotFoundException() throws Exception {
        Long id = 0L;
        TaskDTO taskDTO = dtoMapper.mapTaskToDTO(createTask(null, false));

        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is("No task with id: " + id)));
    }

    @Test
    public void givenId_whenGetById_thanReturnUpdatedObject() throws Exception {
        String updatedTitle = "UpdatedTitle";
        Task task = taskRepository.save(createTask(null, false));
        TaskDTO updatedTaskDTO = dtoMapper.mapTaskToDTO(task);
        updatedTaskDTO.setTitle(updatedTitle);

        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s", task.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTaskDTO)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(task.getId().intValue())))
                .andExpect(jsonPath("$.title", CoreMatchers.is(updatedTitle)));

    }

    @Test
    public void givenTaskWithAssociatedData_whenDelete_thenCorrectResponse() throws Exception {
        Task task = taskRepository.save(createTask(null, false));

        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/tasks/%s", task.getId())));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is("Successfully deleted task with id: " + task.getId())));

    }

    @Test
    public void givenTaskWithAssociatedData_whenDelete_theTaskAndTaskAttachmentDeletedContactNotDeleted() throws Exception {
        Task task = taskRepository.save(createTaskWithAttachments(false));
        TaskAttachment attachment = task.getAttachment();
        Contact firstContact = attachment.getContacts().stream().findFirst().get();

        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/tasks/%s", task.getId())));

        Optional<Task> taskOptional = taskRepository.findById(task.getId());
        Optional<TaskAttachment> attachmentOptional = taskAttachmentRepository.findById(attachment.getId());
        Optional<Contact> contactOptional = contactRepository.findById(firstContact.getId());

        Assertions.assertTrue(taskOptional.isEmpty());
        Assertions.assertTrue(attachmentOptional.isEmpty());
        Assertions.assertTrue(contactOptional.isPresent());
    }

    private Task createTask(TaskAttachment taskAttachment, boolean active) {
        return Task.builder()
                .title("TestTask")
                .description("Task description")
                .activationDate(LocalDate.now().plusDays(3))
                .updated(null)
                .finished(false)
                .active(active)
                .attachment(taskAttachment)
                .priority(1)
                .build();
    }

    private Task createTaskWithAttachments(boolean active) {
        Contact contact = Contact.builder()
                .title("Test contact")
                .updated(new Date())
                .build();

        Contact dbContact = contactRepository.save(contact);
        Set<Contact> contacts = new HashSet<>();
        contacts.add(dbContact);

        TaskAttachment taskAttachment = TaskAttachment.builder()
                .contacts(contacts)
                .build();

        TaskAttachment dbTaskAttachment = taskAttachmentRepository.save(taskAttachment);
        return createTask(dbTaskAttachment, active);
    }
}
