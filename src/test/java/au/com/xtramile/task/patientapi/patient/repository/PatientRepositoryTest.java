package au.com.xtramile.task.patientapi.patient.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import au.com.xtramile.task.patientapi.patient.model.Patient;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PatientRepositoryTest {
  
  @Autowired
  private PatientRepository underTest;

  private Patient patient;

  @BeforeEach
  void setup() {
    patient = new Patient();
    patient.setFirstName("ANY-FIRST");
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    
  }

  @Test
  void itShouldSaveNewPatient() {
    Patient savedRecord = underTest.save(patient);
    
    assertNotNull(savedRecord.getId());
    assertEquals("ANY-FIRST", savedRecord.getFirstName());
    assertNull(savedRecord.getUpdateDate());
  }

  @Test
  void afterSaveARecord_itShouldBePresent() {    
    Patient savedRecord = underTest.save(patient);
    
    UUID savedId = savedRecord.getId();

    Optional<Patient> patientRecord = underTest.findById(savedId);
    assertTrue(patientRecord.isPresent());
  }

  @Test
  void whenFindByEmailAndPhone_itShouldBeExist() {
    underTest.save(patient);
    Boolean isExist = underTest.findByEmailAddressOrPhoneNumber("ANY-EMAIL", "ANY-PHONE");
    assertTrue(isExist);
  }

  @Test
  void whenFindByEmailAndPhone_itShouldBeExistIfOneOfParameterFulfilled() {
    underTest.save(patient);
    Boolean isExist =  underTest.findByEmailAddressOrPhoneNumber("ANY-EMAIL2", "ANY-PHONE");
    assertTrue(isExist);
  }

  @Test
  void whenFindByEmailAndPhone_itShouldBeNotFound() {
    underTest.save(patient);
    Boolean isExist = underTest.findByEmailAddressOrPhoneNumber("ANY-EMAIL2", "ANY-PHONE2");
    assertFalse(isExist);
  }
}
