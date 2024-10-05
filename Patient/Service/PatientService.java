package Patient.Project.Patient.Service;

import Patient.Project.Patient.DTO.PatientWithBilling;
import Patient.Project.Patient.Entity.Billing;
import Patient.Project.Patient.Entity.Patient;
import Patient.Project.Patient.PatientFilter;
import Patient.Project.Patient.Repository.BillingRepo;
import Patient.Project.Patient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private BillingRepo billingRepo;

    public Page<Patient> getFilteredPatients(PatientFilter filter, Pageable pageable) {
//        return patientRepository.findByNameContainingIgnoreCase(filter.getName());
//    }
//}

        return patientRepository.findPatientsWithPagination(
                filter.getId() == 0 ? null : filter.getId(),
                filter.getName(),
                filter.getAge() == 0 ? null : filter.getAge(),
                filter.getDiseases(),
                filter.getGender(),
                pageable
//
//                if( filter.getId() == 0){
//                    null;
//                  }else {
//
//                    filter.getId();
//        }
        );
    }

    public Patient getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patientRepository.findByIdAndNotDeleted(id).orElse(null);  // Returns patient if not soft-deleted
            }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }





    public List<Patient> getPatients(String name, String sortField, String sortOrder) {
        Sort sort = Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortOrder)));
        List<Patient> patients;
        System.out.println("name" + name);
        if (name != null && !name.isEmpty()) {
            patients = patientRepository.findAll(sort).stream()
                    .filter(patient -> patient.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            System.out.println("p1111" + patients);
        } else {
            patients = patientRepository.findAll(sort);
        }

        return patients;
    }

    public List<PatientWithBilling> getAllPatientsWithBilling() {
        return patientRepository.findAllPatientsWithBilling();
    }

    public PatientWithBilling updatePatientAndBilling(PatientWithBilling dto) {
        Optional<Patient> optionalPatient = patientRepository.findById((long) dto.getId());
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setName(dto.getName());
            patient.setAge(dto.getAge());
            patient.setDiseases(dto.getDiseases());
            patient.setGender(dto.getGender());

            Billing billing = patient.getBilling();
            if (billing == null) {
                billing = new Billing();
                billing.setPatient(patient);
            }
            billing.setAmount(dto.getBilling());
            billingRepo.save(billing);

            Patient updatedPatient = patientRepository.save(patient);

            return new PatientWithBilling(
                    updatedPatient.getId(),
                    updatedPatient.getName(),
                    updatedPatient.getAge(),
                    updatedPatient.getDiseases(),
                    updatedPatient.getGender(),
                    billing.getAmount()
            );
        } else {
            throw new RuntimeException("Patient not found with id " + dto.getId());
        }
    }
    public void softDeletePatient(Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            patient.setisDeleted(false);
            patientRepository.save(patient);
        }
    }

    public void savePatientWithBilling(PatientWithBilling dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setDiseases(dto.getDiseases());
        patient.setGender(dto.getGender());
       // patient.setIsDeleted(false); // Ensure the patient is not soft-deleted
        Patient savedPatient = patientRepository.save(patient);

        Billing billing = new Billing();
        billing.setPatient(savedPatient);
        billing.setAmount(dto.getBilling());
        billingRepo.save(billing);
    }

}












