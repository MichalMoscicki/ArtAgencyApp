package com.immpresariat.ArtAgencyApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import com.immpresariat.ArtAgencyApp.payload.TaskAttachmentDTO;
import com.immpresariat.ArtAgencyApp.repository.ContactRepository;
import com.immpresariat.ArtAgencyApp.repository.TaskAttachmentRepository;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import org.hamcrest.CoreMatchers;
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
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskAttachmentITest {

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
    public void givenUnsyncDTO_whenCreate_thenReturnResourceNotFoundException() throws Exception {
        //given
        Long taskId = 0L;
        String message = "No task with id: " + taskId;
        TaskAttachmentDTO unsyncDTO = dtoMapper.mapToDTO(createAttachment());

        //when
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/tasks/%s/attachments", taskId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenUnsyncDTO_whenCreate_thenReturnSyncDTO() throws Exception {
        //given
        Task task = taskRepository.save(createTask(null, false));
        TaskAttachmentDTO unsyncDTO = dtoMapper.mapToDTO(createAttachment());

        //when
        ResultActions response = mockMvc.perform(post(String.format("/api/v1/tasks/%s/attachments", task.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsyncDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.notNullValue()));
    }

    @Test
    public void givenId_whenGetById_thenReturnResourceNotFoundException() throws Exception {
        //given
        Long taskId = 0L;
        Long attachmentId = 0L;
        String message = "No taskAttachment with id: " + taskId;

        //when
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/tasks/%s/attachments/%s", taskId, attachmentId)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenId_whenGetById_thenReturnSyncDTO() throws Exception {
        //given
        TaskAttachment attachment = taskAttachmentRepository.save(createAttachment());
        Task task = taskRepository.save(createTask(attachment, false));

        //when
        ResultActions response = mockMvc.perform(get(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachment.getId())));

        //then
        response.andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void givenTaskId_whenUpdate_thenReturnResourceNotFoundException() throws Exception {
        //given
        Long taskId = 0L;
        Long attachmentId = 3L;
        String message = "No task with id: " + taskId;
        TaskAttachmentDTO updatedDTO = dtoMapper.mapToDTO(createAttachment());

        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s/attachments/%s", taskId, attachmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenTaskAttachmentId_whenUpdate_thenReturnResourceNotFoundException() throws Exception {
        //given
        Task task = taskRepository.save(createTask(null, false));
        Long attachmentId = 0L;
        String message = "No taskAttachment with id: " + attachmentId;
        TaskAttachmentDTO updatedDTO = dtoMapper.mapToDTO(createAttachment());
        updatedDTO.setId(attachmentId);

        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }


    @Test
    public void givenTaskAttachmentDTO_whenUpdate_thenAttachmentUpdated() throws Exception {
        //given
        TaskAttachment attachment = taskAttachmentRepository.save(createAttachment());
        Task task = taskRepository.save(createTask(attachment, false));

        Contact contact = Contact.builder()
                .title("Test contact2")
                .updated(new Date())
                .build();

        Contact dbContact = contactRepository.save(contact);

        TaskAttachmentDTO updatedDTO = dtoMapper.mapToDTO(attachment);
        Set<Contact> contacts = attachment.getContacts();
        contacts.add(dbContact);
       updatedDTO.setContacts(contacts);


        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachment.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacts.size()", CoreMatchers.is(2)));
    }

    @Test
    public void givenTaskAttachmentDTO_whenUpdate_thenAttachmentUpdatedEmptyContacts() throws Exception {
        //given
        TaskAttachment attachment = taskAttachmentRepository.save(createAttachment());
        Task task = taskRepository.save(createTask(attachment, false));


        TaskAttachmentDTO updatedDTO = dtoMapper.mapToDTO(attachment);
        Set<Contact> contacts = attachment.getContacts();
        contacts.removeAll(attachment.getContacts());
        updatedDTO.setContacts(contacts);


        //when
        ResultActions response = mockMvc.perform(put(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachment.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacts.size()", CoreMatchers.is(0)));
    }

    @Test
    public void givenTaskAttachmentDTO_whenDelete_thenThrowResourceNotFoundException() throws Exception {
        //given
        Task task = taskRepository.save(createTask(null, false));
        Long attachmentId = 0L;
        String message = "No taskAttachment with id: " + attachmentId;

        //when
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachmentId)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    public void givenTaskAttachmentDTO_whenDelete_thenAttachmentDeleted() throws Exception {
        //given
        TaskAttachment attachment = taskAttachmentRepository.save(createAttachment());
        Task task = taskRepository.save(createTask(null, false));
        String message = "Successfully deleted attachment with id: " + attachment.getId();


        //when
        ResultActions response = mockMvc.perform(delete(String.format("/api/v1/tasks/%s/attachments/%s", task.getId(), attachment.getId())));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(message)));
    }

    private TaskAttachment createAttachment(){
        Contact contact = Contact.builder()
                .title("Test contact")
                .updated(new Date())
                .build();

        Contact dbContact = contactRepository.save(contact);
        Set<Contact> contacts = new HashSet<>();
        contacts.add(dbContact);

        return TaskAttachment.builder()
                .contacts(contacts)
                .build();
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
