package com.arka.Arka_ERP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePrescriptionRequest(
        @NotBlank @Size(max = 100) String medicineName,
        @Size(max = 50) String dosage,
        Integer durationDays,
        String notes
) { }


