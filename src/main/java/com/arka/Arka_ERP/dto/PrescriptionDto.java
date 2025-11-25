package com.arka.Arka_ERP.dto;

public record PrescriptionDto(
        Long id,
        String medicineName,
        String dosage,
        Integer durationDays,
        String notes
) { }


