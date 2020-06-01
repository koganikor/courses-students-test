package com.example.cst.service;

import com.example.cst.model.Student;
import com.example.cst.model.entity.StudentEntity;
import com.example.cst.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentServiceTests {

    private StudentRepository studentRepository;

    @Before
    public void setup() {
        studentRepository = mock(StudentRepository.class);

        List<StudentEntity> entities = new ArrayList<>();

        StudentEntity studentA = new StudentEntity();
        studentA.setRut(1);
        studentA.setName("NameA");
        studentA.setLastName("LastNameA");
        studentA.setAge(10);
        studentA.setCourse("CourseA");

        entities.add(studentA);

        StudentEntity studentB = new StudentEntity();
        studentB.setRut(2);
        studentB.setName("NameB");
        studentB.setLastName("LastNameB");
        studentB.setAge(20);
        studentB.setCourse("CourseB");

        entities.add(studentB);

        when(studentRepository.findAll()).thenReturn(entities);
        when(studentRepository.findById(eq(1))).thenReturn(Optional.of(studentA));
        when(studentRepository.findById(eq(404))).thenReturn(Optional.empty());
    }

    @Test
    public void getStudents() {
        StudentService svc = new StudentService(studentRepository);

        List<Student> res = svc.getStudents();

        assertEquals(2, res.size());
    }

    @Test
    public void getStudentByRut() {
        StudentService svc = new StudentService(studentRepository);

        Student res = svc.getStudentByRut("1-9");

        assertEquals("1-9", res.getRut());
        assertEquals("NameA", res.getName());
        assertEquals("LastNameA", res.getLastName());
        assertEquals(10, res.getAge().intValue());
        assertEquals("CourseA", res.getCourse());
    }

    @Test(expected = ResponseStatusException.class)
    public void getStudentByRutNotFound() {
        StudentService svc = new StudentService(studentRepository);

        Student res = svc.getStudentByRut("404-9");
    }

}
