package com.tmgreyhat.esbobi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OBIFILEREPO extends JpaRepository<OBIFILE, Long> {


    @Query(value = "SELECT FILENAME FROM OBIFILE WHERE  OBIFILE .FILENAME = ?",nativeQuery = true)
    Optional<OBIFILE> findByFILENAME(String filename);
}
