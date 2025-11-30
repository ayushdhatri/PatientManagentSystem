package com.pm.patientservice.controller;


import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients") // https : localhost:4000/patients
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO){
         PatientResponseDTO newPatient =  patientService.createPatient(patientRequestDTO);
         return ResponseEntity.ok().body(newPatient);
    }
    //localhost:4000/patients/123123123-123123123-123123123
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,@RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(updatedPatient);
    }

}
