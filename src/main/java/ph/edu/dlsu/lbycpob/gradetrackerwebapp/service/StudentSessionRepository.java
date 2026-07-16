package ph.edu.dlsu.lbycpob.gradetrackerwebapp.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ph.edu.dlsu.lbycpob.gradetracker.model.Student;
import ph.edu.dlsu.lbycpob.gradetracker.util.GradeConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ============================================================
// StudentSessionRepository.java
// NOTE:
//   The original StudentRepository used a fixed-size Student[]
//   array and an int counter, all as instance fields.
//
//   In the web version the repository must survive across multiple
//   HTTP requests while remaining private to one browser session.
//   @SessionScope achieves exactly that: Spring creates one instance
//   per HTTP session and destroys it when the session expires.
//
//   The Student[] array is replaced with an ArrayList for easier
//   Java-collection API use; the MAX_STUDENTS capacity guard is
//   preserved identically.
// ============================================================

@Component
@SessionScope
public class StudentSessionRepository {
    private final List<Student> students = new ArrayList<>();

    /** Adds a student to the session list if capacity allows. */
    public boolean addStudent(Student s) {
        if (students.size() >= GradeConstants.MAX_STUDENTS) return false;
        students.add(s);
        return true;
    }

    /** Returns an unmodifiable view of the student list. */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /** Returns the student at the given zero-based index, or null. */
    public Student getStudent(int index) {
        if (index < 0 || index >= students.size()) return null;
        return students.get(index);
    }
}
