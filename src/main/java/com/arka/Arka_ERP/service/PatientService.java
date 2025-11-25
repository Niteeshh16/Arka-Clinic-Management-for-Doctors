package com.arka.Arka_ERP.service;

import com.arka.Arka_ERP.dto.CreatePatientRequest;
import com.arka.Arka_ERP.dto.CreatePrescriptionRequest;
import com.arka.Arka_ERP.dto.CreateVisitRequest;
import com.arka.Arka_ERP.dto.PatientDetailDto;
import com.arka.Arka_ERP.dto.PatientSummaryDto;
import com.arka.Arka_ERP.dto.PrescriptionDto;
import com.arka.Arka_ERP.dto.VisitDto;
import com.arka.Arka_ERP.model.Patient;
import com.arka.Arka_ERP.model.Prescription;
import com.arka.Arka_ERP.model.Visit;
import com.arka.Arka_ERP.repository.PatientRepository;
import com.arka.Arka_ERP.repository.VisitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public List<PatientSummaryDto> searchPatients(String query) {
        String sanitized = StringUtils.hasText(query) ? query.trim() : "";
        if (sanitized.isBlank()) {
            return patientRepository.findAll().stream()
                    .map(this::mapToSummary)
                    .limit(25)
                    .toList();
        }
        return patientRepository.searchByName(sanitized).stream()
                .map(this::mapToSummary)
                .limit(25)
                .toList();
    }

    public PatientDetailDto getPatientDetail(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + patientId));
        return mapToDetail(patient);
    }

    public PatientDetailDto createPatient(CreatePatientRequest request) {
        Patient patient = Patient.builder()
                .firstName(capitalize(request.firstName()))
                .middleName(capitalize(request.middleName()))
                .lastName(capitalize(request.lastName()))
                .age(request.age())
                .gender(request.gender())
                .mobile(request.mobile())
                .address(request.address())
                .build();
        Patient saved = patientRepository.save(patient);
        return mapToDetail(saved);
    }

    public PatientDetailDto updatePatient(Long patientId, CreatePatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + patientId));
        
        patient.setFirstName(capitalize(request.firstName()));
        patient.setMiddleName(capitalize(request.middleName()));
        patient.setLastName(capitalize(request.lastName()));
        patient.setAge(request.age());
        patient.setGender(request.gender());
        patient.setMobile(request.mobile());
        patient.setAddress(request.address());
        
        Patient saved = patientRepository.save(patient);
        return mapToDetail(saved);
    }

    public VisitDto createVisit(CreateVisitRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + request.patientId()));

        Visit visit = Visit.builder()
                .patient(patient)
                .visitDate(request.visitDate())
                .symptoms(request.symptoms())
                .diagnosis(request.diagnosis())
                .build();

        if (request.prescriptions() != null) {
            List<Prescription> prescriptions = request.prescriptions().stream()
                    .map(p -> mapToPrescription(p, visit))
                    .toList();
            visit.setPrescriptions(prescriptions);
        }

        Visit saved = visitRepository.save(visit);
        return mapToVisitDto(saved);
    }

    public List<String> suggestMedications(Long patientId) {
        List<Visit> visits = visitRepository.findByPatientIdOrderByVisitDateDesc(patientId);
        return visits.stream()
                .flatMap(v -> v.getPrescriptions().stream())
                .collect(Collectors.groupingBy(Prescription::getMedicineName, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(entry -> entry.getKey() + " (" + entry.getValue() + "x)")
                .toList();
    }

    private PatientSummaryDto mapToSummary(Patient patient) {
        LocalDateTime lastVisit = patient.getVisits().stream()
                .map(Visit::getVisitDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        String fullName = String.format("%s %s %s",
                safe(patient.getFirstName()),
                safe(patient.getMiddleName()),
                safe(patient.getLastName())).replaceAll("\\s+", " ").trim();

        return new PatientSummaryDto(
                patient.getId(),
                fullName,
                patient.getAge(),
                patient.getGender(),
                patient.getMobile(),
                patient.getAddress(),
                lastVisit
        );
    }

    private PatientDetailDto mapToDetail(Patient patient) {
        List<VisitDto> visits = patient.getVisits().stream()
                .sorted(Comparator.comparing(Visit::getVisitDate).reversed())
                .map(this::mapToVisitDto)
                .toList();
        return new PatientDetailDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getMiddleName(),
                patient.getLastName(),
                patient.getAge(),
                patient.getGender(),
                patient.getMobile(),
                patient.getAddress(),
                patient.getCreatedAt(),
                visits
        );
    }

    private VisitDto mapToVisitDto(Visit visit) {
        List<PrescriptionDto> prescriptions = visit.getPrescriptions().stream()
                .map(p -> new PrescriptionDto(
                        p.getId(),
                        p.getMedicineName(),
                        p.getDosage(),
                        p.getDurationDays(),
                        p.getNotes()
                ))
                .toList();
        return new VisitDto(
                visit.getId(),
                visit.getVisitDate(),
                visit.getSymptoms(),
                visit.getDiagnosis(),
                prescriptions
        );
    }

    private Prescription mapToPrescription(CreatePrescriptionRequest request, Visit visit) {
        return Prescription.builder()
                .visit(visit)
                .medicineName(capitalize(request.medicineName()))
                .dosage(request.dosage())
                .durationDays(request.durationDays())
                .notes(request.notes())
                .build();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String capitalize(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        String trimmed = value.trim();
        return trimmed.substring(0, 1).toUpperCase(Locale.ENGLISH) + trimmed.substring(1);
    }
}


