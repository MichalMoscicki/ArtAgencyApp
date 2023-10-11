package com.immpresariat.ArtAgencyApp.payload;

import com.immpresariat.ArtAgencyApp.models.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotImportedContactInfo {
    private String message;
    private Contact contact;

}
