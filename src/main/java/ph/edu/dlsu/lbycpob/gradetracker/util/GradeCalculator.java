package ph.edu.dlsu.lbycpob.gradetracker.util;

// ============================================================
// GradeCalculator.java
// NOTE: No changes required from the desktop app.
//   All methods are static pure functions with no IO dependency.
//   Called identically from GradeService:
//     GradeCalculator.computeRawGrade(...)
//     GradeCalculator.assignNumericGrade(avg)
//     GradeCalculator.assignLetterRank(avg)
//     GradeCalculator.getRemarks(grade)
// ============================================================

public final class GradeCalculator {

    private GradeCalculator() { }  // prevent instantiation

    /** Arithmetic mean of a scores array. Returns 0.0 for null/empty. */
    public static double computeAverage(double[] scores) {
        if (scores == null || scores.length == 0) return 0.0;
        double sum = 0.0;
        for (double score : scores) sum += score;
        return sum / scores.length;
    }



}
