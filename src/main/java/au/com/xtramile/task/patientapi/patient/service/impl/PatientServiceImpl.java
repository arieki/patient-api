package au.com.xtramile.task.patientapi.patient.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import au.com.xtramile.task.patientapi.patient.model.Patient;
import au.com.xtramile.task.patientapi.patient.repository.PatientRepository;
import au.com.xtramile.task.patientapi.patient.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
  
  private PatientRepository repository;

  @Override
  public Patient createNewPatient(Patient patient) {
    log.info("createNewPatient {}", patient.toString());
    Boolean isExist = 
      repository.findByEmailAddressOrPhoneNumber(patient.getEmailAddress(), patient.getPhoneNumber());
    if (isExist) {
      throw new RuntimeException(
        String.format("Patient with email %s or phone number %s already registered", 
        patient.getEmailAddress(), patient.getPhoneNumber()));
    }

    return repository.save(patient);
  }

  @Override
  public Patient updatePatient(UUID id, Patient patient) {
    log.info("updatePatient id: {}", id);
    Optional<Patient> existing = repository.findById(id);
    if (existing.isPresent()) {
      Patient updated = existing.get();
      updated.setUserId(patient.getUserId());
      updated.setFirstName(patient.getFirstName());
      updated.setLastName(patient.getLastName());
      updated.setEmailAddress(patient.getEmailAddress());
      updated.setPhoneNumber(patient.getPhoneNumber());
      updated.setAddress(patient.getAddress());
      updated.setUpdateDate(patient.getUpdateDate());
      repository.save(updated);
    }
    return patient;
  }
}
