package com.immpresariat.ArtAgencyApp.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.immpresariat.ArtAgencyApp.models.TaskAttachment;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean finished;
    private boolean active;
    private LocalDate activationDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updated;
    private int priority;
    private TaskAttachment attachment;
}
