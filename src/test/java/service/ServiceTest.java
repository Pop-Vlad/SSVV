package service;

import domain.Nota;
import domain.Student;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceTest {

    StudentXMLRepo studentXMLRepository;
    TemaXMLRepo temaXMLRepository;
    NotaXMLRepo notaXMLRepository;
    private Service service;

    @Before
    public void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "src/test/resources/files/Students.xml";
        String filenameTema = "src/test/resources/files/Assignments.xml";
        String filenameNota = "src/test/resources/files/Grades.xml";
        studentXMLRepository = new StudentXMLRepo(filenameStudent);
        temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @After
    public void tearDown() {
        List<Nota> grades = new ArrayList<>();
        notaXMLRepository.findAll().forEach(grades::add);
        grades.forEach(g -> notaXMLRepository.delete(g.getID()));
        List<Nota> assignments = new ArrayList<>();
        notaXMLRepository.findAll().forEach(assignments::add);
        assignments.forEach(a -> notaXMLRepository.delete(a.getID()));
        List<Nota> students = new ArrayList<>();
        notaXMLRepository.findAll().forEach(students::add);
        students.forEach(s -> notaXMLRepository.delete(s.getID()));
        notaXMLRepository = null;
        temaXMLRepository = null;
        studentXMLRepository = null;
        service = null;
    }

    @Test
    public void tc_1_AddStudentIdValid() {

        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        service.addStudent(s);
        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        service.deleteStudent("id1");
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void addStudent() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        service.addStudent(s);
        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        service.deleteStudent("id1");
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void addStudentFail() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        Student s = new Student("id1", "name1", -1, "student@gmail.com");
        //Assert.assertThrows(ValidationException.class, () -> {service.addStudent(s);});
    }

}