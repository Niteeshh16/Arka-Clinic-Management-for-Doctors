package com.arka.Arka_ERP.repository;

import com.arka.Arka_ERP.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByVisitId(Long visitId);
}


