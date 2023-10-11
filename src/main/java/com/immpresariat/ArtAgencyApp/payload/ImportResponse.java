package com.immpresariat.ArtAgencyApp.payload;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ImportResponse {

    private Date timestamp;
    private int savedContacts;
    private List<NotImportedContactInfo> duplicatedContacts;
    private List<NotImportedContactInfo> contactsWithErrors;

}
