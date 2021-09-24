package au.com.xtramile.task.patientapi.patient.service;

import java.util.Optional;
import java.util.UUID;

import au.com.xtramile.task.patientapi.patient.model.Patient;

public interface PatientService {
  
  Patient createNewPatient(final Patient patient);

  Patient updatePatient(final UUID id, final Patient patient);
}
