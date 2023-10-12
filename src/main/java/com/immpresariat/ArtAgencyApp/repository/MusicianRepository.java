package com.immpresariat.ArtAgencyApp.repository;

import com.immpresariat.ArtAgencyApp.models.Musician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MusicianRepository extends JpaRepository<Musician, Long> {

    //todo to może nie działać, trzeba się upewnić
    @Query("SELECT m FROM Musician m " +
            "JOIN m.instruments i " +
            "WHERE lower(i.name) = lower(:instrument)")
    Page<Musician> findAllByInstrument(@Param("instrument") String instrument, Pageable pageable);

}
