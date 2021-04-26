package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IntegrationTopDownTest {

    private StudentValidator studentValidator = mock(StudentValidator.class);
    private TemaValidator temaValidator = mock(TemaValidator.class);
    private NotaValidator notaValidator = mock(NotaValidator.class);

    private StudentXMLRepo studentRepo = mock(StudentXMLRepo.class);
    private TemaXMLRepo temaRepo = mock(TemaXMLRepo.class);
    private NotaXMLRepo notaRepo = mock(NotaXMLRepo.class);

    private Service service = new Service(studentRepo, studentValidator, temaRepo, temaValidator, notaRepo, notaValidator);

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        StudentXMLRepo studentRepository = new StudentXMLRepo("src/test/resources/files/Students.xml");
        TemaXMLRepo assignmentRepository = new TemaXMLRepo("src/test/resources/files/Assignments.xml");
        NotaXMLRepo gradeRepository = new NotaXMLRepo("src/test/resources/files/Grades.xml");
        List<Nota> grades = new ArrayList<>();
        gradeRepository.findAll().forEach(grades::add);
        grades.forEach(g -> gradeRepository.delete(g.getID()));
        List<Tema> assignments = new ArrayList<>();
        assignmentRepository.findAll().forEach(assignments::add);
        assignments.forEach(a -> assignmentRepository.delete(a.getID()));
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        students.forEach(s -> studentRepository.delete(s.getID()));
    }

    @Test
    public void validator_repo_stubs() {
        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        when(studentRepo.save(any())).thenReturn(null);

        service.addStudent(s);

        verify(studentValidator).validate(any());
        verify(studentRepo).save(any());
    }

    @Test
    public void repo_stub() {
        Student s = new Student("id2", "name1", 936, "student@gmail.com");
        when(studentRepo.save(any())).thenReturn(null);
        StudentValidator studentValidator_real = new StudentValidator();

        service = new Service(studentRepo, studentValidator_real, temaRepo, temaValidator, notaRepo, notaValidator);
        service.addStudent(s);

        verify(studentRepo).save(any());
    }

    @Test
    public void no_stubs() {
        Student s = new Student("id3", "name1", 936, "student@gmail.com");
        StudentXMLRepo studentRepo_real = new StudentXMLRepo("src/test/resources/files/Students.xml");
        StudentValidator studentValidator_real = new StudentValidator();

        service = new Service(studentRepo_real, studentValidator_real, temaRepo, temaValidator, notaRepo, notaValidator);
        Student added = service.addStudent(s);

        Assert.assertNull(added);
    }
}
