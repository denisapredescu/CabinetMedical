package data;

import medical.Appointment;
import medical.Medicine;
import medical.Ticket;
import person.Doctor;
import person.Patient;

import javax.print.Doc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;

public class OutputService {
    private final File file = new File("C:\\Users\\denis\\Desktop\\An2-Sem2\\Proiect_PAO\\cabinet_medical\\src\\data\\data.txt");
    Path path = Paths.get(file.getPath());


    public  void writeAppointments(List<Appointment> appointments, String name){

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) { // true -> append;  false -> sterge si adauga
            fileOutputStream.write((name + "'s appointments\n").getBytes(StandardCharsets.UTF_8));
            Integer ind = 1;
            for(Appointment appointment : appointments) {
                fileOutputStream.write(("Appointment " + ind + "\n").getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write(("   - date: " + appointment.getDate() + "\n").getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write(("   - diseases: ").getBytes(StandardCharsets.UTF_8));
                if(appointment.getDisease().isEmpty()){
                    fileOutputStream.write(("-\n").getBytes(StandardCharsets.UTF_8));
                }else{
                    fileOutputStream.write(("\n").getBytes(StandardCharsets.UTF_8));
                    for(String disease : appointment.getDisease()){
                        fileOutputStream.write(("       " + disease + "\n").getBytes(StandardCharsets.UTF_8));
                    }
                }

                fileOutputStream.write(("   - prescripcions: ").getBytes(StandardCharsets.UTF_8));
                if(appointment.getDisease().isEmpty()){
                    fileOutputStream.write(("-\n").getBytes(StandardCharsets.UTF_8));
                }else{
                    fileOutputStream.write(("\n").getBytes(StandardCharsets.UTF_8));
                    for(Medicine medicine : appointment.getPrescription()){
                        fileOutputStream.write(("       " + medicine.getName() + ", " + medicine.getUse() + "\n").getBytes(StandardCharsets.UTF_8));
                    }
                }
                fileOutputStream.write(("   - ticket: ").getBytes(StandardCharsets.UTF_8));
                if(appointment.getTicket() == null){
                    fileOutputStream.write(("-\n").getBytes(StandardCharsets.UTF_8));
                }else{
                    fileOutputStream.write(("date = " + appointment.getTicket().getDate() + "; specialization = " + appointment.getTicket().getSpecialization() + "; details = " + appointment.getTicket().getDetails() + "\n").getBytes(StandardCharsets.UTF_8));
                }
                ind += 1;
                fileOutputStream.write(("\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeTickets(List<Ticket> tickets, String name) throws IOException {
        Files.writeString(path, name + "'s tickets\n");
        Integer ind = 1;
        for(Ticket ticket : tickets) {
            try {
                Files.writeString(path, ("Ticket "+ind + ".\n"), APPEND);
                Files.writeString(path, "   date = " + ticket.getDate(), APPEND);
                Files.writeString(path, "   specialization = " + ticket.getSpecialization(), APPEND);
                Files.writeString(path, "   details = " + ticket.getDetails() + "\n\n", APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ind += 1;
        }
    }

    public void writeDisease(List<String> diseases, String name) throws IOException {
        Files.writeString(path, name + "'s diseases\n");
        Integer ind = 1;
        for( String disease: diseases) {
            try {
                Files.writeString(path, ind + ". "+disease +"\n", APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ind += 1;
        }
    }

    public void writePatients(List<Patient> patients) throws IOException {
        Files.writeString(path,   "     Patients\n\n");
        Integer ind = 1;
        for( Patient patient: patients) {
            try {
                Files.writeString(path, ind + ". "+ patient.getFirstName() + " " + patient.getLastName() + " " + patient.getGender() + " " + patient.getAge() +"\n", APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ind += 1;
        }
    }

    public void writeDoctors(List<Doctor> doctors) throws IOException {
        Files.writeString(path,   "     Doctors\n\n");
        Integer ind = 1;
        for( Doctor doctor: doctors) {
            try {
                Files.writeString(path, ind + ". "+ doctor.getFirstName() + " " + doctor.getLastName() + " " + doctor.getGender() + " " + doctor.getSalary() +"\n", APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ind += 1;
        }
    }
}
