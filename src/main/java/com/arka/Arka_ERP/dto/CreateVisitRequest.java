package com.arka.Arka_ERP.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateVisitRequest(
        @NotNull Long patientId,
        @NotNull LocalDateTime visitDate,
        String symptoms,
        String diagnosis,
        @Valid List<CreatePrescriptionRequest> prescriptions
) { }


