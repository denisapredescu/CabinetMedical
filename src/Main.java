import data.OutputService;
import medical.*;
import person.Doctor;
import person.Patient;
import repositories.BDRepository;
import service.DoctorService;
import service.MedicalHistoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        MedicalHistoryService medicalHistoryService = MedicalHistoryService.getInstance();  // pentru functii legate de lista de istorice / pacienti
        DoctorService doctorService = DoctorService.getInstance();   // pentru functii legate de lista de doctori
        OutputService outputService = new OutputService();    // pentru scrierea in fisierul data.txt
        BDRepository bdRepository = new BDRepository();     // legatura cu baza de date

        Scanner sc = new Scanner(System.in);
        String input = "y";  // y = yes

        while(true){
            System.out.println("Choose a user mode: admin or doctor ('no' to exit)");

            String user = sc.nextLine();

            if(user.equalsIgnoreCase("no")){
                break;
            }
            if(user.equalsIgnoreCase("admin")){
                /// admin: poate sa vada totul si sa stearga/adauge doctori

                while(true) {

                    System.out.println("Choose one of the following options: ");
                    System.out.println("1. Alphabetical list of doctors");
                    System.out.println("2. Alphabetical list of patients");
                    System.out.println("3. Add doctor");
                    System.out.println("4. Delete doctor");
                    System.out.println("5. Change the salary for a given doctor");
                    System.out.println("6. Print list of doctors");
                    System.out.println("7. Print list of patients");
                    System.out.println("0. Exit application");

                    String opt = sc.nextLine();

                    if (opt.equalsIgnoreCase("0")) {
                        break;
                    }

                    switch (opt) {
                        case "1": {  // Alphabetical list of doctors
                            doctorService.printAllDoctors(bdRepository.retrieveDoctor());
                            System.out.println("\n");
                            break;
                        }

                        case "2": {  // Alphabetical list of patients
                            medicalHistoryService.printAllPatients(bdRepository.retrievePatient());
                            System.out.println("\n");
                            break;
                        }

                        case "3": {  // Add doctor
                            System.out.println("Enter 'y' to continue!");
                            input = sc.nextLine();

                            if(input.equalsIgnoreCase("y")){
                                System.out.println("Insert doctor Firstname");
                                String firstname = sc.nextLine();

                                System.out.println("Insert doctor Lastname");
                                String lastname = sc.nextLine();

                                System.out.println("Insert doctor gender");
                                String gender = sc.nextLine();

                                System.out.println("Insert doctor salary");
                                Double salary = Double.parseDouble(sc.nextLine());

                                Doctor doctor = new Doctor(firstname, lastname, gender, salary);
                                bdRepository.addDoctor(doctor);
                            }
                            break;
                        }

                        case "4":{  // Delete doctor given by name
                            System.out.println("Doctor name");
                            String doctor_lastname = sc.nextLine();

                            try{
                                doctorService.deleteDoctorFromDatabase(doctor_lastname, "doctor");
                            }catch (Exception exception) {
                                System.out.println(exception.getMessage());
                            }
                            break;
                        }

                        case "5":{  // Change the salary for a given doctor
                            System.out.println("Doctor name");
                            String doctor_Lastname = sc.nextLine();

                            System.out.println("New salary");
                            Double salary = Double.parseDouble(sc.nextLine());

                            try{
                                doctorService.changeSalaryToDoctor(doctor_Lastname, salary);
                            }catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }

                            break;
                        }

                        case "6":{
                            outputService.writeDoctors(bdRepository.retrieveDoctor());
                            break;
                        }

                        case "7":{
                            outputService.writePatients(bdRepository.retrievePatient());
                            break;
                        }

                        default:{
                            System.out.println("Invalid Option!!");
                        }
                    }
                }
            }

            if(user.equalsIgnoreCase("doctor")){
                /// doctor: se ocupa de patienti si poate sa vada doar ce are legatura cu ei
                Boolean ok = false;

                String doctorName = "";   // numele va mai fi folosit mai jos
                input = "y";
                while(!ok){

                    if(!input.equalsIgnoreCase("y")){
                        break;
                    }
                    System.out.println("Enter your lastname");
                    doctorName = sc.nextLine();

                    try{
                        ok = doctorService.doctorInDatabase(doctorName);

                    }catch (Exception exception){

                        System.out.println(exception.getMessage());
                        System.out.println("Do you want to try again? (y)");
                        input = sc.nextLine();
                    }
                }

                while(ok) {

                    System.out.println("Choose one of the following options: ");
                    System.out.println("1. Alphabetical list of patients");
                    System.out.println("2. Add new patient (+ appointment)");
                    System.out.println("3. Add appointment for existent patient");
                    System.out.println("4. Find past diseases for a given patient");
                    System.out.println("5. List all tickets for a given patient sorted by date");
                    System.out.println("6. List all appointments for a given patient (sorted by doctor, then by date)");
                    System.out.println("0. Exit application");

                    String opt = sc.nextLine();

                    if (opt.equalsIgnoreCase("0")) {
                        break;
                    }

                    switch (opt) {

                        case "1": {  // Alphabetical list of patients
                            medicalHistoryService.printAllPatients(bdRepository.retrievePatient());
                            break;
                        }

                        case "2":{  // New patient
                            System.out.println("Do you want to continue? (y)");
                            input = sc.nextLine();

                            if(!input.equalsIgnoreCase("y")){
                                break;
                            }

                            System.out.println("Insert patient Firstname");
                            String fname = sc.nextLine();

                            System.out.println("Insert patient Lastname");
                            String lname = sc.nextLine();

                            System.out.println("Insert patient gender");
                            String gender = sc.nextLine();

                            System.out.println("Insert patient age");
                            Integer age = Integer.parseInt(sc.nextLine());  // se comporta dubios sc.next impreuna cu sc.nextLine / sc.nextLine si sc.nextInt

                            Patient patient = new Patient(fname, lname, gender, age);

                            bdRepository.addPatient(patient);

                            System.out.println("Add appointment to " + fname + " " + lname + "? (for 'yes' type 'y')");
                            String addAppointment = sc.nextLine();
                            // parametri pentru un Appointment => ii definesc inainte pentru ca doctorul poate sa omita sa completeze toate variabilele
                            if(addAppointment.equalsIgnoreCase("y")) {
                                Appointment patientAppointment = new Appointment(doctorName);

                                bdRepository.addAppointment(patientAppointment, patient);

                                while (true) {

                                    System.out.println("Insert details about the appointment");
                                    System.out.println("a. Insert disease");
                                    System.out.println("b. Insert prescription");
                                    System.out.println("c. Insert ticket");
                                    System.out.println("d. finish details about the patient");

                                    String subOpt = sc.nextLine();

                                    if (subOpt.equalsIgnoreCase("d")) {

                                        // al doilea parametru este o lista de Appointment => este mai usor sa folosesc constructorul cu un parametru si apoi sa adaug si Appointmentdoctor
                                        System.out.println("Saving all the details about the patient " + patient.getFirstName() + " " + patient.getLastName());
                                        break;
                                    }

                                    switch (subOpt) {
                                        case "a": {
                                            while (true) {
                                                System.out.println("a. Add disease");
                                                System.out.println("b. Exit");

                                                String add = sc.nextLine();

                                                if (add.equalsIgnoreCase("a")) {
                                                    System.out.println("Insert disease");
                                                    String disease = sc.nextLine();
                                                    bdRepository.addDisease(patientAppointment, patient, disease);
                                                    break;
                                                } else if (add.equalsIgnoreCase("b")) {
                                                    break;
                                                } else {
                                                    System.out.println("Choose 'a' or 'b'");
                                                }
                                            }
                                            break;
                                        }
                                        case "b": {
                                            while (true) {
                                                System.out.println("a. Add medicine to prescription");
                                                System.out.println("b. Exit");

                                                String add = sc.nextLine();

                                                if (add.equalsIgnoreCase("a")) {
                                                    System.out.println("Insert medicine name");
                                                    String name = sc.nextLine();

                                                    System.out.println("Insert how to use it");
                                                    String use = sc.nextLine();
                                                    Medicine medicine = new Medicine(name, use);
                                                    bdRepository.addMedicine(patientAppointment, patient, medicine);
                                                    break;
                                                } else if (add.equalsIgnoreCase("b")) {
                                                    break;
                                                } else {
                                                    System.out.println("Choose 'a' or 'b'");
                                                }
                                            }
                                            break;
                                        }
                                        case "c": {

                                            System.out.println("Do you want to continue? (y)");
                                            input = sc.nextLine();

                                            if (input.equalsIgnoreCase("y")) {

                                                System.out.println("Insert specialization");
                                                String sp = sc.nextLine();

                                                System.out.println("Insert details about the pacient");
                                                String details = sc.nextLine();

                                                Ticket ticket = new Ticket(sp, details);
                                                bdRepository.addTicket(ticket);
                                                bdRepository.setAppointmentFKTicket(patientAppointment, patient, ticket);
                                            }
                                            break;
                                        }
                                        default: {
                                            System.out.println("Invalid Option!!");
                                        }
                                    }
                                }
                            }
                            break;
                        }

                        case "3":{   // Existent patient

                            input = "y";
                            // caz in care pacientul este deja in baza de date => trebuie sa adaug in istoricul sau vizita curenta
                            Boolean okPass = false;
                            String patientName = null;
                            while(!okPass) {

                                if(!input.equalsIgnoreCase("y")){
                                    break;
                                }

                                try{
                                    System.out.println("Insert patient Lastname");
                                    patientName = sc.nextLine();
                                    okPass = medicalHistoryService.patientInDatabase(patientName);

                                    //am inserat un nume neexistent in istoric => cere altul nou
                                }catch (Exception exception){
                                    System.out.println(exception.getMessage());

                                    System.out.println("Do you want to try again? (y)");
                                    input = sc.nextLine();
                                }
                            }

                            // doctorul poate sa omita sa completeze toate variabilele
                            Appointment patientAppointment = new Appointment(doctorName);
                            Patient patient = new Patient(patientName);

                            Boolean okInsertion = false;

                            if(okPass)
                                bdRepository.addAppointment(patientAppointment, patient);

                            while(true && okPass){

                                System.out.println("Insert details about the appointment");
                                System.out.println("a. Insert disease");
                                System.out.println("b. Insert prescription");
                                System.out.println("c. Insert ticket");
                                System.out.println("d. finish details about the patient");

                                String subOpt = sc.nextLine();

                                if (subOpt.equalsIgnoreCase("d")){
                                    if (okInsertion == true) {
                                        System.out.println("Saving all the details about the patient " + patientName);
                                    }
                                    else{
                                        bdRepository.deleteAppointment(patientAppointment, patient);
                                    }
                                    break;
                                }

                                switch (subOpt){
                                    case "a":{

                                        while(true) {
                                            System.out.println("a. Add disease");
                                            System.out.println("b. Exit");
                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){
                                                System.out.println("Insert disease");
                                                String disease = sc.nextLine();
                                                bdRepository.addDisease(patientAppointment, patient, disease);
                                                okInsertion = true;
                                                break;
                                            }
                                            else if(add.equalsIgnoreCase("b")){ //iesire din switch
                                                break;
                                            }
                                            else{
                                                System.out.println("Choose 'a' or 'b'");
                                            }
                                        }
                                        break;
                                    }

                                    case "b":{

                                        while(true) {

                                            System.out.println("a. Add medicine to prescription");
                                            System.out.println("b. Exit");

                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){

                                                System.out.println("Insert medicine name");
                                                String name = sc.nextLine();

                                                System.out.println("Insert how to use it");
                                                String use = sc.nextLine();

                                                Medicine medicine = new Medicine(name, use);
                                                bdRepository.addMedicine(patientAppointment, patient, medicine);
                                                okInsertion = true;
                                                break;
                                            }
                                            else
                                            if(add.equalsIgnoreCase("b")){ //iesire
                                                break;
                                            }
                                            else {
                                                System.out.println("Choose 'a' or 'b'");
                                            }
                                        }
                                        break;
                                    }

                                    case "c":{

                                        System.out.println("Do you want to continue? (y)");
                                        input = sc.nextLine();

                                        if(input.equalsIgnoreCase("y")){

                                            System.out.println("Insert specialization");
                                            String sp = sc.nextLine();

                                            System.out.println("Insert details about the pacient");
                                            String details = sc.nextLine();

                                            Ticket ticket = new Ticket(sp, details);
                                            bdRepository.addTicket(ticket);
                                            bdRepository.setAppointmentFKTicket(patientAppointment, patient, ticket);
                                            okInsertion = true;
                                        }

                                        break;
                                    }
                                    default:{
                                        System.out.println("Invalid Option!!");
                                    }
                                }
                            }
                            break;
                        }

                        case "4": {  // Find past diseases for a given patient
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();

                            try {
                                List<String> listDiseases = medicalHistoryService.diseasesByPatient(bdRepository.retrieveAppointmentForPatient(name), name);

                                if (!listDiseases.isEmpty()){
                                    outputService.writeDisease(listDiseases, name);
                                }
                                else {
                                    System.out.println("No diseases!");
                                }
                            }
                            catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }

                            break;
                        }

                        case "5": {   // List all tickets for a given patient sorted by date
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();

                            try {
                                List<Ticket> tickets = medicalHistoryService.ticketsByPatientSortedByDate(bdRepository.retrieveAppointmentForPatient(name), name);

                                if (!tickets.isEmpty()){
                                    outputService.writeTickets(tickets, name);
                                } else{
                                    System.out.println("No tickets!");
                                }
                            }
                            catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }

                            break;
                        }

                        case "6": {  // List all appointments for a given patient (sorted by doctor, then by date)
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();
                            try{
                                List<Appointment> appointments = medicalHistoryService.appointmentsByPatient(bdRepository.retrieveAppointmentForPatient(name), name);

                                if (!appointments.isEmpty()) {
                                    outputService.writeAppointments(appointments, name);
                                } else{
                                    System.out.println("No appointments!");
                                }

                            }catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }

                            break;
                        }

                        default:{
                            System.out.println("Invalid Option!!");
                        }
                    }
                }
            }
        }
    }
}

