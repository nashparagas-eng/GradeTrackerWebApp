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
}
