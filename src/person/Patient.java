package person;

public class Patient extends Person  {

    public final Integer age;

    public Patient(String firstName, String lastName, String gender, Integer age) {
        super(firstName, lastName, gender);
        this.age = age;
    }

    public Patient(String lastName) {
        super("", lastName, "");
        this.age = 0;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Patient {" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", age=" + age +
                '}';
    }
}
