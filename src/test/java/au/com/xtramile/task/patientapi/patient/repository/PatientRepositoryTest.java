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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import au.com.xtramile.task.patientapi.patient.model.Patient;

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

  @Test
  void whenFindAllOnCurrentPage_itShouldReturnOnlyLessThanEqualsToSize() {
    underTest.save(patient);
    patient = new Patient();
    patient.setFirstName("FIRSTNAME-2");
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    underTest.save(patient);

    patient = new Patient();
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    patient.setFirstName("FIRSTNAME-3");
    underTest.save(patient);

    patient = new Patient();
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    patient.setFirstName("FIRSTNAME-4");
    underTest.save(patient);

    Page<Patient> currPage = underTest.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "firstName")));
  
    assertEquals(3, currPage.getContent().size()); 
    assertEquals("ANY-FIRST", currPage.getContent().get(0).getFirstName());
  }

  @Test
  void whenFindAllOnNextPage_itShouldReturnOnlyLessThanEqualsToSize() {
    underTest.save(patient);
    patient = new Patient();
    patient.setFirstName("FIRSTNAME-2");
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    underTest.save(patient);

    patient = new Patient();
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    patient.setFirstName("FIRSTNAME-3");
    underTest.save(patient);

    patient = new Patient();
    patient.setLastName("ANY-LAST");
    patient.setPhoneNumber("ANY-PHONE");
    patient.setEmailAddress("ANY-EMAIL");
    patient.setCreatedDate(LocalDateTime.now());
    patient.setFirstName("FIRSTNAME-4");
    underTest.save(patient);

    Page<Patient> currPage = underTest.findAll(PageRequest.of(1, 3, Sort.by(Sort.Direction.ASC, "firstName")));
  
    assertEquals(1, currPage.getContent().size()); 
    assertEquals("FIRSTNAME-4", currPage.getContent().get(0).getFirstName());
  }

}
