package com.arka.Arka_ERP.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PatientDetailDto(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        Integer age,
        String gender,
        String mobile,
        String address,
        LocalDateTime createdAt,
        List<VisitDto> visits
) { }


