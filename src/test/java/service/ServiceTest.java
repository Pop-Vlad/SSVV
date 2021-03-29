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

public class ServiceTest {

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

    @Test
    public void tc_1_AddStudentIdValid() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id1", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_2_AddStudentIdInvalid() {
        service.addStudent(new Student("", "name1", 936, "student@gmail.com"));
    }

    @Test
    public void tc_3_AddStudentNameValid() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id3", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_4_AddStudentNameInvalid() {
        service.addStudent(new Student("id4", "", 936, "student@gmail.com"));
    }

    @Test
    public void tc_5_AddStudentEmailValid() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id5", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_6_AddStudentEmailInvalid() {
        service.addStudent(new Student("id6", "name1", 936, ""));
    }

    @Test
    public void tc_7_AddStudentGroupValid() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id7", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_8_AddStudentGroupInvalid() {
        service.addStudent(new Student("id8", "name1", -1, ""));
    }

    @Test
    public void tc_9_AddStudentNotInList() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_10_AddStudentInList() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("1", "name1", 936, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size(), after.size());
    }

    @Test
    public void tc_11_AddStudent_BVA1() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", 0, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_12_AddStudent_BVA2() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", -1, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_13_AddStudent_BVA3() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", 1, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_14_AddStudent_BVA4() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", Integer.MAX_VALUE, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test
    public void tc_15_AddStudent_BVA5() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", Integer.MAX_VALUE - 1, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }

    @Test(expected = ValidationException.class)
    public void tc_16_AddStudent_BVA6() {
        List<Student> before = new ArrayList<>();
        service.getAllStudenti().forEach(before::add);

        Student s = new Student("id9", "name1", Integer.MAX_VALUE + 1, "student@gmail.com");
        service.addStudent(s);

        List<Student> after = new ArrayList<>();
        service.getAllStudenti().forEach(after::add);
        Assert.assertEquals(before.size() + 1, after.size());
    }
}