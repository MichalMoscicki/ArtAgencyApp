package com.immpresariat.ArtAgencyApp.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartDTO {
    private Long id;
    private String url;
    private String name;
    private String instrumentName;
    private String type;

}
