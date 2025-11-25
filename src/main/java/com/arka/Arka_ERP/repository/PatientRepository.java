package com.arka.Arka_ERP.repository;

import com.arka.Arka_ERP.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
            SELECT p FROM Patient p
            WHERE lower(concat(coalesce(p.firstName,''),' ',coalesce(p.lastName,''))) LIKE lower(concat('%', :query, '%'))
            ORDER BY p.firstName, p.lastName
            """)
    List<Patient> searchByName(@Param("query") String query);
}


