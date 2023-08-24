package com.immpresariat.ArtAgencyApp.repository;

import com.immpresariat.ArtAgencyApp.models.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {

}
