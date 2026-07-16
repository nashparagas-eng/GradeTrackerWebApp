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
        this.repo         = repo;
        this.gradeService = gradeService;
    }
