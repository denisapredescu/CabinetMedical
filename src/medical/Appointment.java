package medical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Appointment{

    private final LocalDate date;
    private final String doctor_name;
    private List<String> disease;
    private List<Medicine> prescription;
    private Ticket ticket;

    public Appointment(String doctor_name){
        this.date = LocalDate.now();
        this.doctor_name = doctor_name;
        this.disease = new ArrayList<String>();
        this.prescription = new ArrayList<Medicine>();
        this.ticket = null;
    }

    public Appointment(String doctor_name, List<String> disease, List<Medicine> prescription, Ticket ticket) {
        this.date = LocalDate.now();
        this.doctor_name = doctor_name;
        this.disease = disease;
        this.prescription = prescription;
        this.ticket = ticket;
    }
    public Appointment(LocalDate date, String doctor_name, List<String> disease, List<Medicine> prescription, Ticket ticket) {
        this.date = date;
        this.doctor_name = doctor_name;
        this.disease = disease;
        this.prescription = prescription;
        this.ticket = ticket;
    }

    public void addDisease(String disease){
        this.disease.add(disease);
    }

    public void addPrescription(Medicine medicine){
        this.prescription.add(medicine);
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public List<String> getDisease() {
        return disease;
    }

    public void setDisease(List<String> disease) {
        this.disease = disease;
    }

    public List<Medicine> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Medicine> prescription) {
        this.prescription = prescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Appointment {" +
                "date=" + date +
                ", doctor_name='" + doctor_name + '\'' +
                ", disease=" + disease +
                ", prescription=" + prescription +
                ", ticket=" + ticket +
                '}';
    }
}
