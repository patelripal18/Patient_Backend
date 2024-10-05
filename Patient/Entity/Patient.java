
package Patient.Project.Patient.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;
    private int age;
    private String diseases;
    private String gender;

    @OneToOne(mappedBy = "patient")
    @JsonIgnore
    private Billing billing;

    @Column(name = "isdeleted", nullable = false)
    private boolean isDeleted = false;


    public void setisDeleted(boolean b) {
    }

    public void setIsDeleted(boolean b) {

    }
}







