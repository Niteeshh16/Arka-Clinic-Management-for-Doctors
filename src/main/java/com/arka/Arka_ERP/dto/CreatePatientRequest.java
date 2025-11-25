package com.arka.Arka_ERP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePatientRequest(
        @NotBlank @Size(max = 50) String firstName,
        @Size(max = 50) String middleName,
        @Size(max = 50) String lastName,
        Integer age,
        @Size(max = 10) String gender,
        @Size(max = 15) String mobile,
        @Size(max = 255) String address
) { }


