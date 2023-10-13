package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.Part;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SongDTO {


    private Long id;
    private String title;
    private String description;
    private String composers;
    private String textAuthors;
    private List<Part> parts;

}
