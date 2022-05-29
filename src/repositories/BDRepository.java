package repositories;

import medical.Appointment;
import medical.Medicine;
import medical.Ticket;
import person.Doctor;
import person.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.sql.Types.NULL;

public class BDRepository {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/medicalmedical";
    private static final String DATABASE_USER = "root";
    private static final String  DATABASE_PASSWORD = "root";

    //person: id, firstName, lastName, gender, type, age, salary
    private static final String RETRIEVE_PERSONS = "select * from person " +
                                                    "where type = ?";
    private static final String CREATE_NEW_PERSON = "insert into person(id, firstName, lastName, gender, type, age, salary) " +
                                                    "values (NULL, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_PERSON = "delete from person " +
                                                " where upper(lastName) = upper(?)";
    private static final String GET_PERSON_ID = "select id from person " +
                                                " where upper(lastName) = upper(?)";
    private static final String UPDATE_SALRAY_FOR_DOCTOR = "update person set salary = ? " +
                                                           " where upper(lastName) = upper(?)";

    //ticket: id, date, specialization, details
    private static final String CREATE_NEW_TICKET = "insert into ticket(id, date, specialization, details) " +
                                                    " values (NULL, ?, ?, ?)";
    private static final String GET_TICKET_ID = "select id from ticket " +
                                                " where date = ? and upper(details) = upper(?) and upper(specialization) = upper(?)";

    // appointment : id, date, doctor_name, ticketId, patientId
    // creez appointment pentru un patient => daca vreau ii fac ticket => salvez idAppointment
    private static final String CREATE_NEW_APPOINTMENT = "insert into appointment(id, date, doctor_name, ticketId, patientId) " +
                                                        " values (NULL, ?, ?, ?, ?)";
    private static final String GET_APPOINTMENT_ID =
            "select id " +
            "from appointment " +
            "where date = ? and upper(doctor_name) = upper(?) and patientId = ? " +
            "order by id desc ";   // o sa o vreau doar pe cea curenta => deci ultima din baza de date
    private static final String UPDATE_SET_FK_TICKET_ID = "update appointment " +
                                                          " set ticketId = ? " +
                                                          " where id = ?";
    private static final String GET_APPOINTMENT_FOR_PERSON =
            "select app.id, app.date, app.doctor_name, tick.date, tick.specialization, tick.details " +
            "from appointment app join person per on (app.patientId = per.id) left join ticket tick on (app.ticketId = tick.id) " +
            "where upper(lastName) = upper(?)";
    private static final String DELETE_APPOINTMENT = "delete from appointment " +
                                                " where id = ?";

    // disease: id, disease, appointmentId
    private static final String CREATE_NEW_DISEASE = "insert into disease(id, disease, appointmentId) " +
                                                     " values (NULL, ?, ?)";
    private static final String GET_DISEASE_BY_APPOINTMENT_ID = "select disease " +
                                                                " from disease " +
                                                                " where appointmentId = ?";

    // medicine: id, name, using_mode, appointmentId
    private static final String CREATE_NEW_MEDICINE = "insert into medicine(id, name, using_mode, appointmentId) " +
                                                      " values (NULL, ?, ?, ?)";
    private static final String GET_MEDICINE_BY_APPOINTMENT_ID = "select name, using_mode " +
                                                                 " from medicine " +
                                                                 " where appointmentId = ?";


    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    // type patient = 1
    public boolean addPatient(Patient patient) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_PERSON);
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getLastName());
        preparedStatement.setString(3, patient.getGender());
        preparedStatement.setInt(4, 1);
        preparedStatement.setInt(5, patient.getAge());
        preparedStatement.setString(6,null);

        return preparedStatement.executeUpdate() > 0;
    }

    // type doctor = 0
    public boolean addDoctor(Doctor doctor) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_PERSON);
        preparedStatement.setString(1, doctor.getFirstName());
        preparedStatement.setString(2, doctor.getLastName());
        preparedStatement.setString(3, doctor.getGender());
        preparedStatement.setInt(4, 0);
        preparedStatement.setString(5, null);
        preparedStatement.setDouble(6, doctor.getSalary());

        return preparedStatement.executeUpdate() > 0;
    }

    public List<Patient> retrievePatient() throws  SQLException {
        List <Patient> result = new ArrayList<>();
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(RETRIEVE_PERSONS);
        preparedStatement.setInt(1, 1);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            Patient patient = new Patient(
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(6)
            );
            result.add(patient);
        }
        return result;
    }

    public List<Doctor> retrieveDoctor() throws  SQLException {
        List <Doctor> result = new ArrayList<>();
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(RETRIEVE_PERSONS);
        preparedStatement.setInt(1, 0);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            Doctor doctor = new Doctor(
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getDouble(7)
            );
            result.add(doctor);
        }
        return result;
    }

    public boolean deletePerson(String lastName) throws  SQLException {
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(DELETE_PERSON);

        preparedStatement.setString(1, lastName);
        return  preparedStatement.executeUpdate() > 0;
    }


    public Integer getPatientId(Patient patient) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_PERSON_ID);

        preparedStatement.setString(1, patient.getLastName());
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            return  rs.getInt(1);
        }
        return NULL;
    }

    public boolean updateSalary(String lastName, Double salary) throws  SQLException {
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(UPDATE_SALRAY_FOR_DOCTOR);

        preparedStatement.setDouble(1, salary);
        preparedStatement.setString(2, lastName);
        return  preparedStatement.executeUpdate() > 0;
    }


    public boolean addAppointment(Appointment appointment, Patient patient) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_APPOINTMENT);

        preparedStatement.setString(1, appointment.getDate().toString());
        preparedStatement.setString(2, appointment.getDoctor_name());
        preparedStatement.setString(3, null);
        preparedStatement.setInt(4, getPatientId(patient));

        return preparedStatement.executeUpdate() > 0;
    }

    public Integer getAppointmentId(Appointment appointment, Patient patient) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_APPOINTMENT_ID);

        preparedStatement.setString(1, appointment.getDate().toString());
        preparedStatement.setString(2, appointment.getDoctor_name());
        preparedStatement.setInt(3, getPatientId(patient));

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            return  rs.getInt(1);
        }
        return NULL;
    }

    public boolean deleteAppointment(Appointment appointment, Patient patient) throws  SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(DELETE_APPOINTMENT);

        preparedStatement.setInt(1, getAppointmentId(appointment, patient));
        return  preparedStatement.executeUpdate() > 0;
    }


//    app.id, app.date, app.doctor_name, tick.date, tick.specialization, tick.details
    public List<Appointment> retrieveAppointmentForPatient(String name) throws  SQLException {
        List <Appointment> result = new ArrayList<>();
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_APPOINTMENT_FOR_PERSON);
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){

            LocalDate ticketDate;
            Ticket ticket = null;  // este null in cazul in care nu am ticket in appointment
            if(rs.getString(4) != null) {
                ticketDate = LocalDate.parse(rs.getString(4), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ticket = new Ticket(
                        ticketDate,
                        rs.getString(5),
                        rs.getString(6)
                );
            }

            List<String> disease =  getDiseaseByAppointmentId(rs.getInt(1));   // iau toate disease-urile
            List<Medicine> medicines = getMedicineByAppointmentId(rs.getInt(1));  // iau toate medicines
            LocalDate appointmentDate = LocalDate.parse(rs.getString(2), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Appointment appointment = new Appointment(
                    appointmentDate,
                    rs.getString(3),
                    disease,
                    medicines,
                    ticket
            );
            result.add(appointment);
        }
        return result;
    }

    public boolean addTicket(Ticket ticket) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_TICKET);
        preparedStatement.setString(1, ticket.getDate().toString());
        preparedStatement.setString(2, ticket.getSpecialization());
        preparedStatement.setString(3, ticket.getDetails());

        return preparedStatement.executeUpdate() > 0;
    }

    public Integer getTicketId(Ticket ticket) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_TICKET_ID);

        preparedStatement.setString(1, ticket.getDate().toString());
        preparedStatement.setString(2, ticket.getDetails());
        preparedStatement.setString(3, ticket.getSpecialization());

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) {
            return  rs.getInt(1);
        }
        return NULL;
    }

    public boolean setAppointmentFKTicket(Appointment appointment, Patient patient, Ticket ticket) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(UPDATE_SET_FK_TICKET_ID);

        preparedStatement.setInt(1, getTicketId(ticket));
        preparedStatement.setInt(2, getAppointmentId(appointment, patient));

        return  preparedStatement.executeUpdate() > 0;
    }

    public boolean addDisease(Appointment appointment, Patient patient, String disease) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_DISEASE);
        preparedStatement.setString(1, disease);
        preparedStatement.setInt(2, getAppointmentId(appointment, patient));

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addMedicine(Appointment appointment, Patient patient, Medicine medicine) throws SQLException{
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(CREATE_NEW_MEDICINE);
        preparedStatement.setString(1, medicine.getName());
        preparedStatement.setString(2, medicine.getUse());
        preparedStatement.setInt(3, getAppointmentId(appointment, patient));

        return preparedStatement.executeUpdate() > 0;
    }

    public List<Medicine> getMedicineByAppointmentId(Integer appointmentId)throws  SQLException {
        List <Medicine> result = new ArrayList<>();
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_MEDICINE_BY_APPOINTMENT_ID);
        preparedStatement.setInt(1, appointmentId);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            Medicine medicine = new Medicine(
                    rs.getString(1),
                    rs.getString(2)
            );
            result.add(medicine);
        }
        return result;
    }

    public List<String> getDiseaseByAppointmentId(Integer appointmentId)throws  SQLException {
        List <String> result = new ArrayList<>();
        PreparedStatement preparedStatement = getDBConnection().prepareStatement(GET_DISEASE_BY_APPOINTMENT_ID);
        preparedStatement.setInt(1, appointmentId);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            String str = rs.getString(1);
            result.add(str);
        }
        return new ArrayList<>(new HashSet<>(result));   // ca sa nu am duplicate
    }

}
