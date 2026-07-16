package ph.edu.dlsu.lbycpob.gradetrackerwebapp.dto;

import jakarta.validation.constraints.*;

// ============================================================
// StudentFormDTO.java  (no desktop equivalent)
//   Mapping to original desktop prompts:
//     name              <- inputName()
//     idNumber          <- inputIdNumber()     (+ IDVerifier check in service)
//     module1..module5  <- inputLabPerformance() loop
//     classParticipation<- inputComponentScore("Class Participation")
//     teacherEvaluation <- inputComponentScore("Teacher's Evaluation")
//     practicalExam     <- inputComponentScore("Practical Exam")
//     project           <- inputComponentScore("Project")
// ============================================================
public class StudentFormDTO {
    // ---- Identity fields ----
    @NotBlank(message = "Student name is required.")
    private String name;

    @NotBlank(message = "ID number is required.")
    @Size(min = 8, max = 8, message = "ID number must be exactly 8 digits.")
    @Pattern(regexp = "\\d{8}", message = "ID number must contain exactly 8 digits.")
    private String idNumber;
