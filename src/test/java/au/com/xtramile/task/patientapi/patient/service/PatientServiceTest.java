package au.com.xtramile.task.patientapi.patient.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import au.com.xtramile.task.patientapi.patient.model.Patient;
import au.com.xtramile.task.patientapi.patient.repository.PatientRepository;
import au.com.xtramile.task.patientapi.patient.service.impl.PatientServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
  
  private PatientService underTest;

  @Mock
  private PatientRepository repository;

  @BeforeEach
  void setup() {
    underTest = new PatientServiceImpl(repository);
  }

  @Test
  void whenCreateNewPatient_itShouldReturnSavedRecord() {
    Patient patient = new Patient();
    patient.setFirstName("ANY-FIRSTNAME");
    patient.setLastName("ANY-LASTNAME");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setCreatedDate(LocalDateTime.now());

    underTest.createNewPatient(patient);

    ArgumentCaptor<Patient> captorPatient = ArgumentCaptor.forClass(Patient.class);
    verify(repository).findByEmailAddressOrPhoneNumber(anyString(), anyString());
    verify(repository).save(captorPatient.capture());
    assertEquals("ANY-FIRSTNAME", captorPatient.getValue().getFirstName());
    assertEquals("ANY-LASTNAME", captorPatient.getValue().getLastName());
    assertEquals("ANY-EMAIL", captorPatient.getValue().getEmailAddress());
    assertNotNull(patient.getCreatedDate());
  }

  @Test
  void whenCreateDuplicatePatient_itShouldThrowsRuntimeException() {
    Patient patient = new Patient();
    patient.setFirstName("ANY-FIRSTNAME");
    patient.setLastName("ANY-LASTNAME");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setCreatedDate(LocalDateTime.now());

    when(repository.findByEmailAddressOrPhoneNumber(anyString(), anyString())).thenReturn(true);

    RuntimeException saveException = assertThrows(RuntimeException.class, 
      () -> underTest.createNewPatient(patient));
    verify(repository).findByEmailAddressOrPhoneNumber(anyString(), anyString());
    verify(repository, times(0)).save(any());
    assertTrue(saveException.getMessage().contains("already registered"));
  }

  @Test
  void whenUpdatePatient_itShouldUpdateTheValue() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdDate = LocalDateTime.now();

    Patient patient = new Patient();
    patient.setId(id);
    patient.setUserId("ANY-USERID");
    patient.setFirstName("ANY-FIRSTNAME");
    patient.setLastName("ANY-LASTNAME");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setCreatedDate(createdDate);
    when(repository.findById(any())).thenReturn(Optional.of(patient));

    Patient modifiedPatient = new Patient();
    modifiedPatient.setId(id);
    modifiedPatient.setUserId("ANY-USERID");
    modifiedPatient.setFirstName("ANY-FIRSTNAME2");
    modifiedPatient.setLastName("ANY-LASTNAME");
    modifiedPatient.setEmailAddress("ANY-EMAIL");
    modifiedPatient.setPhoneNumber("ANY-PHONE");
    modifiedPatient.setCreatedDate(createdDate);
    modifiedPatient.setUpdateDate(LocalDateTime.now());

    Patient updatedPatient = underTest.updatePatient(id, modifiedPatient);
    
    ArgumentCaptor<Patient> captorPatient = ArgumentCaptor.forClass(Patient.class);
    
    verify(repository).findById(any());
    verify(repository).save(captorPatient.capture());
    assertEquals("ANY-FIRSTNAME2", captorPatient.getValue().getFirstName());
    assertNotNull(captorPatient.getValue().getUpdateDate());
    assertEquals(updatedPatient, captorPatient.getValue());
  }

}
