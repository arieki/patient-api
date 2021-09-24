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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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


  @Test
  void whenFetchPatientsByPage_itShouldReturnLessThanEqualsSize() {
    List<Patient> content = new LinkedList<>();
    Patient data = new Patient();
    data.setId(UUID.randomUUID());
    data.setFirstName("A");
    content.add(data);
    data = new Patient();
    data.setId(UUID.randomUUID());
    data.setFirstName("B");
    content.add(data);

    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "firstName"));
    Page<Patient> patients = new PageImpl<>(content, pageable, 3);

    when(repository.findAll(any(Pageable.class))).thenReturn(patients);

    Page<Patient> currPage = underTest.fetchAllByPage(0, 2);
    assertEquals(2, currPage.getContent().size());
    assertEquals("A", currPage.getContent().get(0).getFirstName());
    assertEquals(2, currPage.getTotalPages());
  }

  @Test
  void itShoudlDeleteFromDb() {
    Patient patient = new Patient();
    patient.setFirstName("ANY-FIRSTNAME");
    patient.setLastName("ANY-LASTNAME");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setCreatedDate(LocalDateTime.now());

    underTest.deletePatient(patient);

    ArgumentCaptor<Patient> captorPatient = ArgumentCaptor.forClass(Patient.class);
    verify(repository).delete(captorPatient.capture());
    assertEquals("ANY-FIRSTNAME", captorPatient.getValue().getFirstName());
  }

  @Test
  void whenDeletePatientThatIsNotExist_itShouldThrowRuntimeException() {
    assertThrows(RuntimeException.class, () -> underTest.deletePatient(null), "no parameter passed");
  }
}
