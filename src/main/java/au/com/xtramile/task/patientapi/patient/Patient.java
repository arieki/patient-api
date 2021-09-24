package au.com.xtramile.task.patientapi.patient;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table
@Data
public class Patient {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(nullable = false, updatable = false)
  private UUID id;
  @Column(name = "user_id")
  private String userId;
  @Column(name = "first_name", nullable = false)
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "email_address")
  private String emailAddress;
  @Column(name = "phone_number")
  private String phoneNumber;
  @Column
  private String address;
  @Column(name = "created_date")
  @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  private LocalDateTime createdDate;
  @Column(name = "updated_date")
  @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
  private LocalDateTime updateDate;
}
