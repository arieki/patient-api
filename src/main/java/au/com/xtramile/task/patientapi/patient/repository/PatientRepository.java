package au.com.xtramile.task.patientapi.patient.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import au.com.xtramile.task.patientapi.patient.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
 
  @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END"
    + " FROM PATIENT WHERE EMAIL_ADDRESS = :emailAddress OR PHONE_NUMBER = :phoneNumber", nativeQuery = true)
  Boolean findByEmailAddressOrPhoneNumber(String emailAddress, String phoneNumber);

  Page<Patient> findAll(Pageable pageable);
  
}
