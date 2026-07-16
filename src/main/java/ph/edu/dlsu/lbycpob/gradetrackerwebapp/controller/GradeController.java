package ph.edu.dlsu.lbycpob.gradetrackerwebapp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ph.edu.dlsu.lbycpob.gradetracker.dto.ClassStatsResult;
import ph.edu.dlsu.lbycpob.gradetracker.dto.IDVerifyFormDTO;
import ph.edu.dlsu.lbycpob.gradetracker.dto.StudentFormDTO;
import ph.edu.dlsu.lbycpob.gradetracker.model.Student;
import ph.edu.dlsu.lbycpob.gradetracker.service.GradeService;
import ph.edu.dlsu.lbycpob.gradetracker.service.StudentSessionRepository;
import ph.edu.dlsu.lbycpob.gradetracker.util.GradeCalculator;
import ph.edu.dlsu.lbycpob.gradetracker.util.GradeConstants;
import ph.edu.dlsu.lbycpob.gradetracker.util.IDVerifier;

// ============================================================
// GradeController.java
// NOTEs:
//   The original GradeTrackerApp.displayMenu() drove a do-while
//   loop where user keystrokes mapped to method calls:
//     "1" -> inputStudentData()
//     "2" -> ReportPrinter.printReport(repo)
//     "3" -> ReportPrinter.printClassStats(repo)
//     "4" -> idVerifier.verifyID()
//     "5" -> exit
//
//   In the web version each of those actions is a URL:
//     GET  /                 -> home page (menu)
//     GET  /students/enter   -> student entry form (was option 1)
//     POST /students/add     -> process form, add student, redirect
//     POST /students/clear   -> clear session data
//     GET  /report           -> grade report table (was option 2)
//     GET  /stats            -> class statistics (was option 3)
//     GET  /verify           -> ID verification form (was option 4)
//     POST /verify           -> process and display result
//
//   ReportPrinter.printReport() and printClassStats() are replaced
//   by Thymeleaf templates (report.html, stats.html) that receive
//   model data built here.
// ============================================================

@Controller
public class GradeController {

    private final StudentSessionRepository repo;
    private final GradeService gradeService;

    // Constructor injection -- preferred over @Autowired on fields
    public GradeController(StudentSessionRepository repo, GradeService gradeService) {
        this.repo = repo;
        this.gradeService = gradeService;
    }

    // =====================================================================
    // GET /    -- Home / Menu  (was GradeTrackerApp.displayMenu())
    // =====================================================================
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("studentCount", repo.getCount());
        model.addAttribute("maxStudents", GradeConstants.MAX_STUDENTS);
        model.addAttribute("repoFull", repo.isFull());
        return "index";
    }

    // =====================================================================
    // GET /students/enter   -- Show the student data entry form
    //                          (was menu option 1 -> inputStudentData())
    // =====================================================================
    @GetMapping("/students/enter")
    public String showEntryForm(Model model) {
        // Provide a fresh DTO for the form (Thymeleaf binds to it)
        if (!model.containsAttribute("studentForm")) {
            model.addAttribute("studentForm", new StudentFormDTO());
        }
        model.addAttribute("students", repo.getAllStudents());
        model.addAttribute("studentCount", repo.getCount());
        model.addAttribute("maxStudents", GradeConstants.MAX_STUDENTS);
        model.addAttribute("repoFull", repo.isFull());
        model.addAttribute("numModules", GradeConstants.NUM_MODULES);
        model.addAttribute("minScore", GradeConstants.MIN_SCORE);
        model.addAttribute("maxScore", GradeConstants.MAX_SCORE);
        return "enter-students";
    }

    // =====================================================================
    // POST /students/add   -- Submit one student's data
    //                         (was inputOneStudent() + repo.addStudent())
    // =====================================================================
    @PostMapping("/students/add")
    public String addStudent(@Valid @ModelAttribute("studentForm") StudentFormDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttrs,
                             Model model) {
        // Layer 1: Bean Validation (field-level annotations on StudentFormDTO)
        // Layer 2: ID semantic check via IDVerifier
        if (!bindingResult.hasErrors()) {
            String idResult = IDVerifier.validateID(dto.getIdNumber());
            if (idResult.startsWith("Invalid")) {
                bindingResult.rejectValue("idNumber", "id.invalid", idResult);
            }
        }

        if (bindingResult.hasErrors()) {
            // Re-populate model and re-render the form with error messages
            model.addAttribute("students", repo.getAllStudents());
            model.addAttribute("studentCount", repo.getCount());
            model.addAttribute("maxStudents", GradeConstants.MAX_STUDENTS);
            model.addAttribute("repoFull", repo.isFull());
            model.addAttribute("numModules", GradeConstants.NUM_MODULES);
            model.addAttribute("minScore", GradeConstants.MIN_SCORE);
            model.addAttribute("maxScore", GradeConstants.MAX_SCORE);
            return "enter-students";
        }

        if (repo.isFull()) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Maximum of " + GradeConstants.MAX_STUDENTS
                            + " students reached. Clear data to start over.");
            return "redirect:/students/enter";
        }

        Student student = gradeService.buildStudent(dto);
        repo.addStudent(student);

        redirectAttrs.addFlashAttribute("successMessage",
                "Student \"" + student.getName() + "\" added successfully.");
        return "redirect:/students/enter";
    }

    // =====================================================================
    // POST /students/clear   -- Reset session data (like restarting the app)
    // =====================================================================
    @PostMapping("/students/clear")
    public String clearStudents(RedirectAttributes redirectAttrs) {
        repo.clear();
        redirectAttrs.addFlashAttribute("successMessage",
                "All student data cleared.");
        return "redirect:/";
    }

    // =====================================================================
    // GET /report   -- Grade report table
    //                  (was ReportPrinter.printReport(repo))
    // =====================================================================
    @GetMapping("/report")
    public String viewReport(Model model) {
        List<Student> students = repo.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("hasData", !students.isEmpty());

        // Provide GradeCalculator reference for the template to call getRemarks
        // (Thymeleaf cannot call static methods directly, so we precompute remarks)
        // The remarks are computed in the template via a helper approach below.
        return "report";
    }


}
