package com.arka.Arka_ERP.controller;

import com.arka.Arka_ERP.dto.CreatePatientRequest;
import com.arka.Arka_ERP.dto.PatientDetailDto;
import com.arka.Arka_ERP.dto.PatientSummaryDto;
import com.arka.Arka_ERP.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/search")
    public List<PatientSummaryDto> searchPatients(@RequestParam(defaultValue = "") String query) {
        return patientService.searchPatients(query);
    }

    @GetMapping("/{id}")
    public PatientDetailDto getPatient(@PathVariable Long id) {
        return patientService.getPatientDetail(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDetailDto createPatient(@Valid @RequestBody CreatePatientRequest request) {
        return patientService.createPatient(request);
    }

    @PutMapping("/{id}")
    public PatientDetailDto updatePatient(@PathVariable Long id, @Valid @RequestBody CreatePatientRequest request) {
        return patientService.updatePatient(id, request);
    }

    @GetMapping("/{id}/suggestions")
    public List<String> getMedicationSuggestions(@PathVariable Long id) {
        return patientService.suggestMedications(id);
    }
}


