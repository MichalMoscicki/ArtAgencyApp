package com.immpresariat.ArtAgencyApp.repository;

import com.immpresariat.ArtAgencyApp.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
