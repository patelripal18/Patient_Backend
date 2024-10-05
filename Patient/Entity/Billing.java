package Patient.Project.Patient.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
    public class Billing {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "billing_id")
        private int id;

        @Column(name = "billing_amount")
        private Integer amount;

        @OneToOne
        @JoinColumn(name = "patient_id")
        private Patient patient;

        // Getters and setters
    }


