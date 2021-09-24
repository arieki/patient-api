package au.com.xtramile.task.patientapi.patient.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import au.com.xtramile.task.patientapi.patient.model.Patient;
import au.com.xtramile.task.patientapi.patient.service.PatientService;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {
  
  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientService patientService;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  void whenCreateNewPatient_itShouldResponseOk() throws Exception {
    UUID id = UUID.randomUUID();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.parse("24/09/2021 16:24:00", formatter);
    Patient saved = new Patient();
    saved.setId(id);
    saved.setUserId("ANY-IDNUMBER");
    saved.setFirstName("ANY-FIRSTNAME");
    saved.setLastName("ANY-LASTNAME");
    saved.setCreatedDate(now);
    when(patientService.createNewPatient(any())).thenReturn(saved);

    this.mockMvc.perform(
      MockMvcRequestBuilders.post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
        .content(
          "{\"id\":\"de6af5c5-42b7-45f3-9077-bc2bfd8d39ef\", "
          + "\"userId\":\"ANY-IDNUMBER\",\"firstName\":\"ANY-FIRSTNAME\", "
          + "\"lastName\":\"ANY-LASTNAME\",\"emailAddress\":null,\"phoneNumber\":null, "
          + "\"address\":null,\"createdDate\":\"24/09/2021 16:24:00\",\"updatedDate\":null}")
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("ANY-IDNUMBER"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("ANY-FIRSTNAME"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("ANY-LASTNAME"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").value("24/09/2021 16:24:00"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("ANY-IDNUMBER"));
    verify(patientService).createNewPatient(any());
  }

  @Test
  void whenCreateNewPatientThenThrowsError_itShouldResponseBadRequest() throws Exception {
    RuntimeException exception = new RuntimeException("email and phone number should not be null");
    when(patientService.createNewPatient(any())).thenThrow(exception);

    this.mockMvc.perform(
      MockMvcRequestBuilders.post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
        .content(
          "{\"id\":\"de6af5c5-42b7-45f3-9077-bc2bfd8d39ef\", "
          + "\"userId\":\"ANY-IDNUMBER\",\"firstName\":\"ANY-FIRSTNAME\", "
          + "\"lastName\":\"ANY-LASTNAME\",\"emailAddress\":null,\"phoneNumber\":null, "
          + "\"address\":null,\"createdDate\":\"24/09/2021 16:24:00\",\"updatedDate\":null}")
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string("email and phone number should not be null"));

    verify(patientService).createNewPatient(any());
  }

  @Test
  void whenFetchPatients_itShouldReturnBasedOnSizeConstraint() throws Exception {
    List<Patient> content = new LinkedList<>();
    Patient data = new Patient();
    data.setId(UUID.randomUUID());
    data.setFirstName("A");
    content.add(data);
    data = new Patient();
    data.setId(UUID.randomUUID());
    data.setFirstName("B");
    content.add(data);
    data = new Patient();
    data.setId(UUID.randomUUID());
    data.setFirstName("C");
    content.add(data);

    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "firstName"));
    Page<Patient> patients = new PageImpl<>(content, pageable, 3);

    when(patientService.fetchAllByPage(anyInt(), anyInt())).thenReturn(patients);

    this.mockMvc.perform(
      MockMvcRequestBuilders.get("/api/v1/patients").param("size", "3")
    )
    .andExpect(MockMvcResultMatchers.status().isOk())
    .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(2))
    .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3))
    .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].firstName").value("A"));

    verify(patientService).fetchAllByPage(anyInt(), anyInt());
  }

  @Test
  void itShouldDeleteRecordFromDb() throws Exception {
    this.mockMvc.perform(
      MockMvcRequestBuilders.delete("/api/v1/delete").contentType(MediaType.APPLICATION_JSON)
      .content("{\"id\":\"de6af5c5-42b7-45f3-9077-bc2bfd8d39ef\", "
      + "\"userId\":\"ANY-IDNUMBER\",\"firstName\":\"ANY-FIRSTNAME\", "
      + "\"lastName\":\"ANY-LASTNAME\",\"emailAddress\":null,\"phoneNumber\":null, "
      + "\"address\":null,\"createdDate\":\"24/09/2021 16:24:00\",\"updatedDate\":null}")
    )
    .andExpect(MockMvcResultMatchers.status().isNoContent());
    
    verify(patientService).deletePatient(any());
  }

  @Test
  void itShouldUpdatValue() throws Exception {
    UUID id = UUID.randomUUID();
    Patient value = new Patient();
    value.setId(id);
    value.setFirstName("ANY-FIRSTNAME");
    
    when(patientService.updatePatient(any(), any())).thenReturn(value);
    
    this.mockMvc.perform(
      MockMvcRequestBuilders.put("/api/v1/update/{id}", id).contentType(MediaType.APPLICATION_JSON)
      .content("{\"id\":\""+id.toString()+"\", "
      + "\"userId\":\"ANY-IDNUMBER\",\"firstName\":\"ANY-FIRSTNAME\", "
      + "\"lastName\":\"ANY-LASTNAME\",\"emailAddress\":null,\"phoneNumber\":null, "
      + "\"address\":null,\"createdDate\":\"24/09/2021 16:24:00\",\"updatedDate\":null}")

    )
    .andExpect(MockMvcResultMatchers.status().isOk())
    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()));

    verify(patientService).updatePatient(any(), any());
  }
}
