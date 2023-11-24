package com.immpresariat.ArtAgencyApp.repository;

import com.immpresariat.ArtAgencyApp.models.ConcertDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertDetailsRepository extends JpaRepository<ConcertDetails, Long> {

}
