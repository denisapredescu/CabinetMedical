package medical;

import java.time.LocalDate;

public class Ticket {

    private final LocalDate date;
    private String specialization;
    private String details;

    public Ticket(){
        this.date = LocalDate.now();
        this.specialization = "";
        this.details = "";
    }

    public Ticket(String specialization, String details) {
        this.date = LocalDate.now();
        this.specialization = specialization;
        this.details = details;
    }

    public Ticket(LocalDate date, String specialization, String details) {
        this.date = date;
        this.specialization = specialization;
        this.details = details;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Ticket {" +
                "date=" + date +
                ", specialization='" + specialization + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
