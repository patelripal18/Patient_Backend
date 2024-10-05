package Patient.Project.Patient.Repository;

import Patient.Project.Patient.DTO.PatientWithBilling;
import Patient.Project.Patient.Entity.Patient;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
       List<Patient> findAll(Sort sort);

    //    List<Patient> findByNameContainingIgnoreCase(String name);

//    @Query("SELECT p.id, p.name, p.age, b.amount " +
//            "FROM Patient p " +
//            "RIGHT JOIN Billing b ON p.id = b.patient_id")
    @Query("SELECT new Patient.Project.Patient.DTO.PatientWithBilling(p.id, p.name, p.age, p.diseases, p.gender, b.amount) " +
            "FROM Patient p RIGHT JOIN p.billing b")
//@Query("SELECT p.id, p.name, p.age, b.amount " +
//        "FROM Patient p RIGHT JOIN p.billing b")
    List<PatientWithBilling> findAllPatientsWithBilling();

    @Query("SELECT p FROM Patient p WHERE " +
            "(:id IS NULL OR p.id = :id) AND " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:age IS NULL OR p.age = :age) AND " +
            "(:diseases IS NULL OR LOWER(p.diseases) LIKE LOWER(CONCAT('%', :diseases, '%'))) AND " +
            "(:gender IS NULL OR LOWER(p.gender) = LOWER(:gender)) AND " +
            "p.isDeleted = false")
    Page<Patient> findPatientsWithPagination(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("age") Integer age,
            @Param("diseases") String diseases,
            @Param("gender") String gender,
            Pageable pageable);

     @Query("SELECT p FROM Patient p WHERE p.isDeleted = false")
    List<Patient> findAllActive();


    @Query("SELECT p FROM Patient p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Patient> findByIdAndNotDeleted(@Param("id") Long id);

}
