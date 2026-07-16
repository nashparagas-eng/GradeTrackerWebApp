package ph.edu.dlsu.lbycpob.gradetrackerwebapp.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.lbycpob.gradetracker.dto.StudentFormDTO;
import ph.edu.dlsu.lbycpob.gradetracker.model.Student;
import ph.edu.dlsu.lbycpob.gradetracker.util.GradeCalculator;
import ph.edu.dlsu.lbycpob.gradetracker.util.IDVerifier;

// ============================================================
// GradeService.java
// PURPOSE:
//   Houses the business logic that was scattered across
//   StudentInputHandler (the computation calls) and
//   GradeTrackerApp.inputStudentData() on the desktop.
//
//   On the desktop the flow was:
//     1. Prompt for scores  (StudentInputHandler)
//     2. Call GradeCalculator  (StudentInputHandler.inputOneStudent)
//     3. Construct Student     (StudentInputHandler.inputOneStudent)
//     4. Repo.addStudent(s)    (GradeTrackerApp.inputStudentData)
//
//   In the web version:
//     1. Form data arrives as a validated StudentFormDTO
//     2. GradeService.buildStudent(dto) replicates steps 2-3
//     3. GradeController calls repo.addStudent(student)
//
//   The service is intentionally thin here; it is the right
//   place to add future logic (persistence, grade appeals, etc.)
//   without touching the controller or repository.
// ============================================================

@Service
public class GradeService {
}