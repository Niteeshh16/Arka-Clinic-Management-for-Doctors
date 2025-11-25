package com.arka.Arka_ERP.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VisitDto(
        Long id,
        LocalDateTime visitDate,
        String symptoms,
        String diagnosis,
        List<PrescriptionDto> prescriptions
) { }


