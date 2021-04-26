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
import validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IntegrationTest {

    StudentXMLRepo studentRepository;
    TemaXMLRepo assignmentRepository;
    NotaXMLRepo gradeRepository;
    private Service service;

    @Before
    public void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "src/test/resources/files/Students.xml";
        String filenameTema = "src/test/resources/files/Assignments.xml";
        String filenameNota = "src/test/resources/files/Grades.xml";
        studentRepository = new StudentXMLRepo(filenameStudent);
        assignmentRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentRepository, assignmentRepository);
        gradeRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentRepository, studentValidator, assignmentRepository, temaValidator, gradeRepository, notaValidator);
        studentRepository.save(new Student("1", "student1", 936, "student1@gmail.com"));
        studentRepository.save(new Student("2", "student2", 937, "student2@gmail.com"));
        studentRepository.save(new Student("3", "student3", 924, "student3@gmail.com"));
        assignmentRepository.save(new Tema("1", "descr1", 6, 4));
        assignmentRepository.save(new Tema("2", "descr2", 8, 6));
        gradeRepository.save(new Nota("1", "1", "2", 9.5, LocalDate.of(2020, 12, 20)));
        gradeRepository.save(new Nota("2", "2", "1", 8.5, LocalDate.of(2020, 12, 20)));
    }

    @After
    public void tearDown() {
        List<Nota> grades = new ArrayList<>();
        gradeRepository.findAll().forEach(grades::add);
        grades.forEach(g -> gradeRepository.delete(g.getID()));
        List<Tema> assignments = new ArrayList<>();
        assignmentRepository.findAll().forEach(assignments::add);
        assignments.forEach(a -> assignmentRepository.delete(a.getID()));
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        students.forEach(s -> studentRepository.delete(s.getID()));
        gradeRepository = null;
        assignmentRepository = null;
        studentRepository = null;
        service = null;
    }

    @Test(expected = ValidationException.class)
    public void tc_Integration_AddGradeValid() {
        List<Nota> before = new ArrayList<>();
        service.getAllNote().forEach(before::add);

        Nota n = new Nota("3", "2", "1", 9.3, LocalDate.of(2020, 12, 20));
        service.addNota(n, "");

        List<Nota> after = new ArrayList<>();
        service.getAllNote().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_Integration_AddAssignmentValid() {
        List<Tema> before = new ArrayList<>();
        service.getAllTeme().forEach(before::add);

        Tema a = new Tema("id1", "desc1", 11, 7);
        Tema added = service.addTema(a);

        List<Tema> after = new ArrayList<>();
        service.getAllTeme().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
        Assert.assertNull(added);
    }

    @Test
    public void tc_Integration_BigBang() {
        tc_Integration_AddStudentValid();
        tc_Integration_AddAssignmentValid();

        List<Nota> before = new ArrayList<>();
        service.getAllNote().forEach(before::add);

        Nota n = new Nota("id4", "id1", "id1", 8.3, LocalDate.of(2020, 12, 20));
        service.addNota(n, "");

        List<Nota> after = new ArrayList<>();
        service.getAllNote().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_Integration_AddStudentValid() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        Student added = service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
        Assert.assertNull(added);
    }

    @Test
    public void tc_Integration_AddAssignment() {
        List<Student> beforeStudent = new ArrayList<>();
        service.getAllStudenti().forEach(beforeStudent::add);
        List<Tema> beforeAssignment = new ArrayList<>();
        service.getAllTeme().forEach(beforeAssignment::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        Student addedS = service.addStudent(s);
        Assert.assertNull(addedS);

        Tema a = new Tema("id1", "desc1", 11, 7);
        Tema addedA = service.addTema(a);
        Assert.assertNull(addedA);

        List<Student> afterStudent = new ArrayList<>();
        service.getAllStudenti().forEach(afterStudent::add);
        List<Tema> afterAssignment = new ArrayList<>();
        service.getAllTeme().forEach(afterAssignment::add);

        Assert.assertEquals(beforeStudent.size() + 1, afterStudent.size());
        Assert.assertEquals(beforeAssignment.size() + 1, afterAssignment.size());
    }

    @Test
    public void tc_Integration_AddGrade() {
        List<Student> beforeStudent = new ArrayList<>();
        service.getAllStudenti().forEach(beforeStudent::add);
        List<Tema> beforeAssignment = new ArrayList<>();
        service.getAllTeme().forEach(beforeAssignment::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        Student addedS = service.addStudent(s);
        Assert.assertNull(addedS);

        Tema a = new Tema("id1", "desc1", 11, 7);
        Tema addedA = service.addTema(a);
        Assert.assertNull(addedA);

        Nota g = new Nota("id4", "id1", "id1", 8.3, LocalDate.of(2020, 12, 20));
        Nota addedG = service.addNota(g, "feedback");
        Assert.assertNull(addedG);

        List<Student> afterStudent = new ArrayList<>();
        service.getAllStudenti().forEach(afterStudent::add);
        List<Tema> afterAssignment = new ArrayList<>();
        service.getAllTeme().forEach(afterAssignment::add);

        Assert.assertEquals(beforeStudent.size() + 1, afterStudent.size());
        Assert.assertEquals(beforeAssignment.size() + 1, afterAssignment.size());
    }
}
