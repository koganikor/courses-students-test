package com.example.cst.service;

import com.example.cst.model.Rut;
import com.example.cst.model.Student;
import com.example.cst.model.entity.StudentEntity;
import com.example.cst.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Gets all students in database.
     *
     * @return a list of all students in database.
     */
    public List<Student> getStudents() {
        List<StudentEntity> entities = studentRepository.findAll();

        return entities.stream().map(this::getStudentFromEntity).collect(Collectors.toList());
    }

    /**
     * Gets a student identified by a given rut.
     *
     * @param rut the identifier to search for.
     * @return the student.
     * @throws ResponseStatusException (BAD_REQUEST) if an invalid rut is provided.
     * @throws ResponseStatusException (NOT_FOUND) if no student is found.
     */
    public Student getStudentByRut(String rut) {
        Rut sRut = validateRut(rut);

        Optional<StudentEntity> oStudent = studentRepository.findById(sRut.getMantissa());

        if (!oStudent.isPresent()) {
            log.warn("Student with rut {} not found", rut);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student found with specified rut");
        }

        return getStudentFromEntity(oStudent.get());
    }

    /**
     * Creates a new student.
     *
     * @param newStudent student data to be stored.
     * @return the created student.
     */
    public Student createStudent(Student newStudent) {
        if (newStudent == null) {
            log.warn("No student provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No student provided");
        }

        Rut sRut = validateRut(newStudent.getRut());

        Optional<StudentEntity> oStudent = studentRepository.findById(sRut.getMantissa());

        if (oStudent.isPresent()) {
            log.warn("Student already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already exists");
        }

        StudentEntity newEntity = new StudentEntity();
        newEntity.setRut(sRut.getMantissa());
        newEntity.setName(newStudent.getName());
        newEntity.setLastName(newStudent.getLastName());
        newEntity.setAge(newStudent.getAge());
        newEntity.setCourse(newStudent.getCourse());

        studentRepository.save(newEntity);

        return getStudentFromEntity(newEntity);
    }

    /**
     * Updates a student.
     *
     * @param rut student identifier.
     * @param updatedStudent the new data.
     * @return the updated student.
     */
    public Student updateStudent(String rut, Student updatedStudent) {
        Rut sRut = validateRut(rut);

        Optional<StudentEntity> oStudent = studentRepository.findById(sRut.getMantissa());

        if (!oStudent.isPresent()) {
            log.warn("Student does not exist {}", rut);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist");
        }

        StudentEntity studentEntity = oStudent.get();
        studentEntity.setName(updatedStudent.getName());
        studentEntity.setLastName(updatedStudent.getLastName());
        studentEntity.setAge(updatedStudent.getAge());
        studentEntity.setCourse(updatedStudent.getCourse());

        studentRepository.save(studentEntity);

        return getStudentFromEntity(studentEntity);
    }

    /**
     * Deletes a student.
     *
     * @param rut student identifier.
     */
    public void deleteStudent(String rut) {
        Rut sRut = validateRut(rut);

        Optional<StudentEntity> oStudent = studentRepository.findById(sRut.getMantissa());

        if (!oStudent.isPresent()) {
            log.warn("Student does not exist {}", rut);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist");
        }

        studentRepository.delete(oStudent.get());
    }

    private Rut validateRut(String rut) {
        Rut sRut = Rut.castRut(rut);

        if (sRut == null) {
            log.warn("Invalid rut provided {}", rut);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rut provided");
        }

        return sRut;
    }

    private Student getStudentFromEntity(StudentEntity entity) {
        Rut rut = new Rut(entity.getRut());

        Student ret = new Student();
        ret.setRut(rut.toString());
        ret.setName(entity.getName());
        ret.setLastName(entity.getLastName());
        ret.setAge(entity.getAge());
        ret.setCourse(entity.getCourse());

        return ret;
    }
}
