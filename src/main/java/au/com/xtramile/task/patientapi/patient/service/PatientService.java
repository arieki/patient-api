package au.com.xtramile.task.patientapi.patient.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import au.com.xtramile.task.patientapi.patient.model.Patient;

public interface PatientService {
  
  Patient createNewPatient(final Patient patient);

  Patient updatePatient(final UUID id, final Patient patient);

  Page<Patient> fetchAllByPage(final int page, final int size);

  void deletePatient(final UUID id);
}
