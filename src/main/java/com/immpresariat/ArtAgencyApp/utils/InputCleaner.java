package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.Contact;
import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import com.immpresariat.ArtAgencyApp.models.Event;
import com.immpresariat.ArtAgencyApp.models.Institution;
import org.springframework.stereotype.Component;

@Component
public class InputCleaner {

    public Institution clean(Institution institution){
      institution.setName(institution.getName().trim());
      institution.setCity(institution.getCity().trim());
      institution.setCategory(institution.getCategory().trim());
      institution.setNotes(institution.getNotes().trim());
        if(institution.getEmail() != null){
            institution.setEmail(institution.getEmail().trim());
        }
        if(institution.getPhone() != null){
            institution.setPhone(institution.getPhone().trim());
        }
        if(institution.getWebPage() != null){
            institution.setWebPage(institution.getWebPage().trim());
        }
        return institution;
    }
    public Event clean(Event event){
        event.setName(event.getName().trim());
        event.setDescription(event.getDescription().trim());
        return event;
    }
    public ContactPerson clean(ContactPerson contactPerson){
        contactPerson.setFirstName(contactPerson.getFirstName().trim());
        contactPerson.setLastName(contactPerson.getLastName().trim());
        contactPerson.setEmail(contactPerson.getEmail().trim());
        contactPerson.setPhone(contactPerson.getPhone().trim());
        if(contactPerson.getRole() != null){
            contactPerson.setRole(contactPerson.getRole().trim().toLowerCase());
        }
        return contactPerson;
    }
    public Contact clean(Contact contact){
        contact.setTitle(contact.getTitle().trim());

        if(contact.getDescription() != null){
            contact.setDescription(contact.getDescription().trim());
        }
        return contact;
    }

}
