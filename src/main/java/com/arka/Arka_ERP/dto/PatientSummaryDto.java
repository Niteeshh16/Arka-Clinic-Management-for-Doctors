package com.arka.Arka_ERP.dto;

import java.time.LocalDateTime;

public record PatientSummaryDto(
        Long id,
        String displayName,
        Integer age,
        String gender,
        String mobile,
        String address,
        LocalDateTime lastVisitDate
) { }


