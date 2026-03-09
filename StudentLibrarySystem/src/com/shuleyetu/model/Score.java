package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Score {
    
    private int scoreId;
    private int enrollmentId;
    private double cat1;
    private double cat2;
    private double cat3;
    private double exam;
    private Double totalCat;      // calculated: cat1 + cat2 + cat3
    private Double totalScore;    // calculated: totalCat + exam
    private String grade;         // calculated based on totalScore
    private String remarks;
    private Integer gradedBy;
    private Timestamp gradedDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Enrollment enrollment;
    private Lecturer grader;
    
    // Constants for grade boundaries
    public static final double GRADE_A_MIN = 70.0;
    public static final double GRADE_B_MIN = 60.0;
    public static final double GRADE_C_MIN = 50.0;
    public static final double GRADE_D_MIN = 40.0;
    
    public static final double CAT_MAX = 30.0;
    public static final double EXAM_MAX = 70.0;
    public static final double TOTAL_MAX = 100.0;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Score() {
        this.cat1 = 0.0;
        this.cat2 = 0.0;
        this.cat3 = 0.0;
        this.exam = 0.0;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
        calculateTotalAndGrade();
    }
    
    /**
     * Constructor with enrollment ID
     */
    public Score(int enrollmentId) {
        this();
        this.enrollmentId = enrollmentId;
    }
    
    /**
     * Constructor with enrollment object
     */
    public Score(Enrollment enrollment) {
        this();
        this.enrollment = enrollment;
        this.enrollmentId = enrollment != null ? enrollment.getEnrollmentId() : 0;
    }
    
    /**
     * Constructor with all scores
     */
    public Score(int enrollmentId, double cat1, double cat2, double cat3, double exam) {
        this(enrollmentId);
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.exam = exam;
        validateScores();
        calculateTotalAndGrade();
    }
    
    /**
     * Constructor with scores and grader
     */
    public Score(int enrollmentId, double cat1, double cat2, double cat3, double exam, 
                Integer gradedBy) {
        this(enrollmentId, cat1, cat2, cat3, exam);
        this.gradedBy = gradedBy;
        this.gradedDate = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Score(int scoreId, int enrollmentId, double cat1, double cat2, double cat3, 
                double exam, Double totalCat, Double totalScore, String grade,
                String remarks, Integer gradedBy, Timestamp gradedDate,
                Timestamp createdAt, Timestamp updatedAt) {
        this.scoreId = scoreId;
        this.enrollmentId = enrollmentId;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.exam = exam;
        this.totalCat = totalCat;
        this.totalScore = totalScore;
        this.grade = grade;
        this.remarks = remarks;
        this.gradedBy = gradedBy;
        this.gradedDate = gradedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getScoreId() {
        return scoreId;
    }
    
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }
    
    public int getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
        updateTimestamp();
    }
    
    public double getCat1() {
        return cat1;
    }
    
    public void setCat1(double cat1) {
        this.cat1 = cat1;
        validateCatScore(cat1, "CAT 1");
        calculateTotalAndGrade();
        updateTimestamp();
    }
    
    public double getCat2() {
        return cat2;
    }
    
    public void setCat2(double cat2) {
        this.cat2 = cat2;
        validateCatScore(cat2, "CAT 2");
        calculateTotalAndGrade();
        updateTimestamp();
    }
    
    public double getCat3() {
        return cat3;
    }
    
    public void setCat3(double cat3) {
        this.cat3 = cat3;
        validateCatScore(cat3, "CAT 3");
        calculateTotalAndGrade();
        updateTimestamp();
    }
    
    public double getExam() {
        return exam;
    }
    
    public void setExam(double exam) {
        this.exam = exam;
        validateExamScore(exam);
        calculateTotalAndGrade();
        updateTimestamp();
    }
    
    public Double getTotalCat() {
        return totalCat;
    }
    
    // No setter for totalCat - it's calculated
    
    public Double getTotalScore() {
        return totalScore;
    }
    
    // No setter for totalScore - it's calculated
    
    public String getGrade() {
        return grade;
    }
    
    // No setter for grade - it's calculated
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
        updateTimestamp();
    }
    
    public Integer getGradedBy() {
        return gradedBy;
    }
    
    public void setGradedBy(Integer gradedBy) {
        this.gradedBy = gradedBy;
        this.gradedDate = new Timestamp(System.currentTimeMillis());
        updateTimestamp();
    }
    
    public Timestamp getGradedDate() {
        return gradedDate;
    }
    
    public void setGradedDate(Timestamp gradedDate) {
        this.gradedDate = gradedDate;
        updateTimestamp();
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Enrollment getEnrollment() {
        return enrollment;
    }
    
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        if (enrollment != null) {
            this.enrollmentId = enrollment.getEnrollmentId();
        }
        updateTimestamp();
    }
    
    public Lecturer getGrader() {
        return grader;
    }
    
    public void setGrader(Lecturer grader) {
        this.grader = grader;
        if (grader != null) {
            this.gradedBy = grader.getLecturerId();
        }
        updateTimestamp();
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Update the updatedAt timestamp
     */
    private void updateTimestamp() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Calculate total CAT and total score, then determine grade
     */
    public void calculateTotalAndGrade() {
        // Calculate total CAT
        this.totalCat = cat1 + cat2 + cat3;
        
        // Calculate total score
        this.totalScore = totalCat + exam;
        
        // Determine grade
        this.grade = calculateGrade(this.totalScore);
    }
    
    /**
     * Calculate grade based on total score
     */
    public static String calculateGrade(double score) {
        if (score >= GRADE_A_MIN) return "A";
        if (score >= GRADE_B_MIN) return "B";
        if (score >= GRADE_C_MIN) return "C";
        if (score >= GRADE_D_MIN) return "D";
        return "F";
    }
    
    /**
     * Get grade point value (for GPA calculation)
     */
    public double getGradePoint() {
        if (grade == null) return 0.0;
        
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    /**
     * Validate all scores
     */
    private void validateScores() {
        validateCatScore(cat1, "CAT 1");
        validateCatScore(cat2, "CAT 2");
        validateCatScore(cat3, "CAT 3");
        validateExamScore(exam);
    }
    
    /**
     * Validate a single CAT score
     */
    private void validateCatScore(double score, String catName) {
        if (score < 0 || score > CAT_MAX) {
            throw new IllegalArgumentException(
                catName + " score must be between 0 and " + CAT_MAX);
        }
    }
    
    /**
     * Validate exam score
     */
    private void validateExamScore(double score) {
        if (score < 0 || score > EXAM_MAX) {
            throw new IllegalArgumentException(
                "Exam score must be between 0 and " + EXAM_MAX);
        }
    }
    
    /**
     * Check if all scores have been entered
     */
    public boolean isComplete() {
        return cat1 > 0 || cat2 > 0 || cat3 > 0 || exam > 0;
    }
    
    /**
     * Check if score is passing (D or above)
     */
    public boolean isPassing() {
        return grade != null && !"F".equals(grade);
    }
    
    /**
     * Get score status (Pass/Fail)
     */
    public String getPassFailStatus() {
        return isPassing() ? "PASS" : "FAIL";
    }
    
    /**
     * Get formatted score display
     */
    public String getFormattedScores() {
        return String.format("CATs: %.1f, %.1f, %.1f | Exam: %.1f | Total: %.1f | Grade: %s",
            cat1, cat2, cat3, exam, totalScore != null ? totalScore : 0, grade);
    }
    
    /**
     * Get concise score display
     */
    public String getConciseDisplay() {
        return String.format("Total: %.1f (%s)", 
            totalScore != null ? totalScore : 0, grade);
    }
    
    /**
     * Get student name from enrollment
     */
    public String getStudentName() {
        if (enrollment != null && enrollment.getStudent() != null) {
            return enrollment.getStudent().getName();
        }
        return "";
    }
    
    /**
     * Get student registration number
     */
    public String getStudentRegNumber() {
        if (enrollment != null && enrollment.getStudent() != null) {
            return enrollment.getStudent().getRegistrationNumber();
        }
        return "";
    }
    
    /**
     * Get course code from enrollment
     */
    public String getCourseCode() {
        if (enrollment != null && enrollment.getCourseOffering() != null && 
            enrollment.getCourseOffering().getCourse() != null) {
            return enrollment.getCourseOffering().getCourse().getCourseCode();
        }
        return "";
    }
    
    /**
     * Get course title
     */
    public String getCourseTitle() {
        if (enrollment != null && enrollment.getCourseOffering() != null && 
            enrollment.getCourseOffering().getCourse() != null) {
            return enrollment.getCourseOffering().getCourse().getCourseTitle();
        }
        return "";
    }
    
    /**
     * Get grader name
     */
    public String getGraderName() {
        return grader != null ? grader.getName() : "";
    }
    
    /**
     * Check if score has been graded
     */
    public boolean isGraded() {
        return gradedBy != null && gradedDate != null;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return scoreId == score.scoreId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(scoreId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Score{" +
               "scoreId=" + scoreId +
               ", enrollmentId=" + enrollmentId +
               ", cat1=" + cat1 +
               ", cat2=" + cat2 +
               ", cat3=" + cat3 +
               ", exam=" + exam +
               ", totalScore=" + totalScore +
               ", grade='" + grade + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Score{" +
               "scoreId=" + scoreId +
               ", enrollmentId=" + enrollmentId +
               ", cat1=" + cat1 +
               ", cat2=" + cat2 +
               ", cat3=" + cat3 +
               ", exam=" + exam +
               ", totalCat=" + totalCat +
               ", totalScore=" + totalScore +
               ", grade='" + grade + '\'' +
               ", remarks='" + remarks + '\'' +
               ", gradedBy=" + gradedBy +
               ", gradedDate=" + gradedDate +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentName='" + getStudentName() + '\'' +
               ", courseCode='" + getCourseCode() + '\'' +
               ", graderName='" + getGraderName() + '\'' +
               '}';
    }
    
    /**
     * Report card entry string
     */
    public String toReportCardEntry() {
        return String.format("%-10s %-30s %5.1f %5.1f %5.1f %5.1f %6.1f %4s",
            getCourseCode(),
            getCourseTitle().length() > 28 ? getCourseTitle().substring(0, 25) + "..." : getCourseTitle(),
            cat1, cat2, cat3, exam,
            totalScore != null ? totalScore : 0,
            grade != null ? grade : "-"
        );
    }
}