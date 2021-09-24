package au.com.xtramile.task.patientapi.patient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.com.xtramile.task.patientapi.patient.model.Patient;
import au.com.xtramile.task.patientapi.patient.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PatientController {
  
  private PatientService service;

  @PostMapping(value="/save")
  public ResponseEntity<Patient> createNewPatient(@RequestBody Patient patient) {
    log.info("createNewPatient");
    Patient saved = service.createNewPatient(patient);
    return ResponseEntity.ok().body(saved);
  }

  @PutMapping(value = "/update/{id}")
  public ResponseEntity<Patient> updatePatent(@RequestBody Patient patient, @PathVariable UUID id) {
    log.info("updatePatent id: {}", id);
    return ResponseEntity.ok().body(service.updatePatient(id, patient));
  }

  @GetMapping(value="/patients")
  public ResponseEntity<Page<Patient>> fetchPatients(
    @RequestParam(defaultValue = "0") int page, @RequestParam int size) {
    log.info("fetchPatients [page: {}, size: {}]", page, size);
    return ResponseEntity.ok().body(service.fetchAllByPage(page, size));
  }
  
  @DeleteMapping(value = "/delete")
  public ResponseEntity<Patient> deletePatient(@RequestParam UUID id) {
    log.info("deletePatient {}", id);
    service.deletePatient(id);
    return ResponseEntity.noContent().build();
  }

}
