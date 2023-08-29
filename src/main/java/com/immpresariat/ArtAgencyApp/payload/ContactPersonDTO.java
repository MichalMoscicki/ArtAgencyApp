package com.immpresariat.ArtAgencyApp.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactPersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String phone;
}
