package Patient.Project.Patient;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatientFilter {

    private int id;
    private String name;
    private int age;
    private String diseases;
    private String gender;
    private Integer pageNo ;
    private Integer pageSize ;
}

