package Patient.Project.Patient.Repository;

import Patient.Project.Patient.Entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepo extends JpaRepository<Billing ,Long>{
    Billing findByPatientId(Long patientId);

}
