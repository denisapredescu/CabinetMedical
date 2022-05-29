package person;

public class Doctor extends Person {

    public Double salary;

    public Doctor(String firstName, String lastName, String gender, Double salary) {
        super(firstName, lastName, gender);
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Doctor {" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", salary=" + salary +
                '}';
    }
}

