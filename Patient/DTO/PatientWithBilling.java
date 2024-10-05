package Patient.Project.Patient.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatientWithBilling {

        private int id;
        private String name;
        private int age;
        private String diseases;
        private String gender;

        private Integer billing;

        public PatientWithBilling(int id, String name, int age, String diseases, String gender, Integer billing) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.diseases = diseases;
            this.gender = gender;
            this.billing = billing;
        }

        // Getters and setters
    }


