package service;

import interfaces.ErrorMessage;
import medical.Appointment;
import medical.Ticket;
import person.Patient;
import repositories.BDRepository;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MedicalHistoryService implements ErrorMessage {

    final private BDRepository bdRepository= new BDRepository();
    private MedicalHistoryService(){}

    private static class SINGLETON_HOLDER{
        private static final MedicalHistoryService INSTANCE = new MedicalHistoryService();
    }

    public static MedicalHistoryService getInstance(){
        return SINGLETON_HOLDER.INSTANCE;
    }

    // din interfata
    @Override
    public String errorMessage(String name) {
        return "No patient found for the given name " + name + "!";
    }

    // verifica daca pacientul dat cu nume exista in istoric
    public boolean patientInDatabase(String patientName) throws SQLException {
        for (Patient patient: bdRepository.retrievePatient()) {
            if(patient.getLastName().equalsIgnoreCase(patientName)) {
                return true;
            }
        }
        throw new RuntimeException(errorMessage(patientName));
    }

    public List<String> diseasesByPatient(List<Appointment> appointments, String name) throws SQLException {

        patientInDatabase(name);  // imi pasa doar sa dea eroare in cazul in care patientul nu exista in baza de date
        return appointments.stream()
                .flatMap(appointment -> appointment.getDisease().stream())
                .collect(Collectors.toList());

    }

    // le afiseaza in ordine dupa doctor si data
    public List<Appointment> appointmentsByPatient(List<Appointment> appointments, String patientName) throws SQLException {

        patientInDatabase(patientName);  // imi pasa doar sa dea eroare in cazul in care patientul nu exista in baza de date
        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getDoctor_name)
                        .thenComparing(Appointment::getDate))
                .collect(Collectors.toList());
    }

    public List<Ticket> ticketsByPatientSortedByDate(List<Appointment> appointments, String name) throws SQLException {

        patientInDatabase(name); // imi pasa doar sa dea eroare in cazul in care patientul nu exista in baza de date

        List<Ticket> tickets = appointments.stream()
                .map(Appointment::getTicket)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Collections.sort(tickets, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return tickets;
    }

    // Alphabetical list of patients
    public void printAllPatients(List<Patient> patients) {
        patients.stream()
                .sorted(Comparator.comparing(Patient::getLastName))
                .forEach(System.out::println);
    }

}
