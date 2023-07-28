package com.immpresariat.ArtAgencyApp.repository;

import com.immpresariat.ArtAgencyApp.models.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    @Query("select i from Institution i where i.name=:name and i.city=:city")
    Optional<Institution> findInstitutionByNameAndCity(String name, String city);

}
