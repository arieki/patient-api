package au.com.xtramile.task.patientapi.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PatientRepositoryTest {
  
  @Autowired
  private PatientRepository underTest;

  @Test
  void itShouldSaveNewPatient() {
    Patient patient = new Patient();
    patient.setFirstName("ANY-FIRST");
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    
    Patient savedRecord = underTest.save(patient);
    
    assertNotNull(savedRecord.getId());
    assertEquals("ANY-FIRST", savedRecord.getFirstName());
    assertNull(savedRecord.getUpdateDate());
  }

  @Test
  void afterSaveARecord_itShouldBePresent() {
    Patient patient = new Patient();
    patient.setFirstName("ANY-FIRST");
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    
    Patient savedRecord = underTest.save(patient);
    
    UUID savedId = savedRecord.getId();

    Optional<Patient> patientRecord = underTest.findById(savedId);
    assertTrue(patientRecord.isPresent());
  }
}
