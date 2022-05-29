package service;

import interfaces.ErrorMessage;
import person.Doctor;
import repositories.BDRepository;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class DoctorService implements ErrorMessage {

    final private BDRepository bdRepository= new BDRepository();
    private DoctorService(){}

    private static class SINGLETON_HOLDER{
        private static final DoctorService INSTANCE = new DoctorService();
    }

    public static DoctorService getInstance(){
        return DoctorService.SINGLETON_HOLDER.INSTANCE;
    }

    // din interfata
    @Override
    public String errorMessage(String name) {
        return "No doctor found for the given name " + name + "!";
    }

    public void printAllDoctors(List<Doctor> doctors){
        doctors.stream()
                .sorted(Comparator.comparing(Doctor::getLastName))
                .forEach(System.out::println);
    }

    public void changeSalaryToDoctor(String doctorName, Double salary) throws SQLException{
        if(!bdRepository.updateSalary(doctorName, salary)){
            throw new RuntimeException(errorMessage(doctorName));
        }
    }

    public void deleteDoctorFromDatabase(String doctorName, String isDoctor) throws SQLException {
        if(isDoctor.equalsIgnoreCase("doctor"))
            if(!bdRepository.deletePerson(doctorName)){
                throw new RuntimeException(errorMessage(doctorName));
            }
    }

    public Boolean doctorInDatabase(String doctorName) throws SQLException {
        for (Doctor doctor : bdRepository.retrieveDoctor()){
            if(doctor.getLastName().equalsIgnoreCase(doctorName)){
                return true;
            }
        }
        throw new RuntimeException(errorMessage(doctorName));
    }
}
