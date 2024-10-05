package Patient.Project.Patient.Controller;


import Patient.Project.Patient.DTO.PatientWithBilling;
import Patient.Project.Patient.Entity.Patient;
import Patient.Project.Patient.PatientFilter;
import Patient.Project.Patient.Service.PatientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/filter")
    public ResponseEntity<Page<Patient>> filterPatients(@RequestBody PatientFilter patientFilter) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert patientFilter object to JSON string
            String patientFilterJson = objectMapper.writeValueAsString(patientFilter);
            System.out.println("patientFilter JSON: " + patientFilterJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately in production code
        }

        Pageable pageable = PageRequest.of(patientFilter.getPageNo(), patientFilter.getPageSize());
        //Pageable pageable = PageRequest.of(0, 2);
        Page<Patient> filteredPatients = patientService.getFilteredPatients(patientFilter, pageable);

        return new ResponseEntity<>(filteredPatients, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Patient> getPatientById(@RequestParam Long id) {
        Patient patient = patientService.getPatientById(id);

        if (patient != null) {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<Patient>> getAllPatients() {
//        List<Patient> patients = patientService.getAllPatients();
//        if (patients.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(patients, HttpStatus.OK);
//        }
//    }

    @GetMapping("/all-patients-with-billing")
    public ResponseEntity<List<PatientWithBilling>> getAllPatientsWithOrWithoutBilling() {
        try {
            List<Patient> allPatients = patientService.getAllPatients();  // Get all patients
            List<PatientWithBilling> result = new ArrayList<>();

            for (Patient patient : allPatients) {
                // Ensure the patient object and its fields are not null
                if (patient != null) {
                    // Safely handle billing information
                    Integer billingAmount = (patient.getBilling() != null) ? patient.getBilling().getAmount() : null;

                    // Build the PatientWithBilling object
                    PatientWithBilling patientWithBilling = new PatientWithBilling(
                            patient.getId(),
                            patient.getName(),
                            patient.getAge(),
                            patient.getDiseases(),
                            patient.getGender(),
                            billingAmount // Could be null if no billing exists
                    );

                    result.add(patientWithBilling);
                }
            }

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            // Log the error for troubleshooting
            e.printStackTrace();  // Log to console or file (in production, use a logger)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientWithBilling> getPatientWithBilling(@PathVariable Long id) {
        Patient optionalPatient = patientService.getPatientById(id);

        if (optionalPatient != null) {
            Patient patient = optionalPatient;
            Integer billingAmount = (patient.getBilling() != null) ? patient.getBilling().getAmount() : null;

            PatientWithBilling patientWithBilling = new PatientWithBilling(
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getDiseases(),
                    patient.getGender(),
                    billingAmount
            );

            return new ResponseEntity<>(patientWithBilling, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Patient>> listPatient(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "Asc") String sortOrder) {

        List<Patient> patients = patientService.getPatients(name, sortField, sortOrder);
        System.out.println("PPPPPPP");
        System.out.println(patients);

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

//    @GetMapping("/with-billing")
//    public ResponseEntity<List<PatientWithBilling>> getAllPatientsWithBilling() {
//        List<PatientWithBilling> patientsWithBilling = patientService.getAllPatientsWithBilling();
//        return new ResponseEntity<>(patientsWithBilling, HttpStatus.OK);
//    }

    @PutMapping("/update")
    public ResponseEntity<String> updatePatientAndBilling(@RequestBody PatientWithBilling dto) {
        try {
            patientService.updatePatientAndBilling(dto);  // Perform update operation
            return new ResponseEntity<>("Update was successful", HttpStatus.OK);  // Return success message only
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.NOT_FOUND);  // Return error message if something fails
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeletePatient(@PathVariable Long id) {
        patientService.softDeletePatient(id);
        return ResponseEntity.ok().body("deleted successfully");
    }


    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping
    public ResponseEntity<String> addPatientWithBilling(@RequestBody PatientWithBilling dto) {
        try {
            patientService.savePatientWithBilling(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Patient and billing created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the patient and billing");
        }
    }
}







