package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int offeringId;
    private Timestamp enrollmentDate;
    private String status;  // "ENROLLED", "DROPPED", "COMPLETED", "PENDING"
    private String grade;   // Final grade if completed
    private Double totalScore;  // Final score if completed
    private String remarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Student student;
    private CourseOffering courseOffering;
    private Score score;  // One-to-one relationship with Score
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Enrollment() {
        this.status = "ENROLLED";
        this.enrollmentDate = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Constructor with required fields
     */
    public Enrollment(int studentId, int offeringId) {
        this();
        this.studentId = studentId;
        this.offeringId = offeringId;
    }
    
    /**
     * Constructor with student and course offering objects
     */
    public Enrollment(Student student, CourseOffering courseOffering) {
        this();
        this.student = student;
        this.courseOffering = courseOffering;
        this.studentId = student != null ? student.getStudentId() : 0;
        this.offeringId = courseOffering != null ? courseOffering.getOfferingId() : 0;
    }
    
    /**
     * Constructor with all basic fields
     */
    public Enrollment(int studentId, int offeringId, String status, String remarks) {
        this(studentId, offeringId);
        this.status = status;
        this.remarks = remarks;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Enrollment(int enrollmentId, int studentId, int offeringId, 
                     Timestamp enrollmentDate, String status, String grade,
                     Double totalScore, String remarks, Timestamp createdAt, 
                     Timestamp updatedAt) {
        this();
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.offeringId = offeringId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.grade = grade;
        this.totalScore = totalScore;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getOfferingId() {
        return offeringId;
    }
    
    public void setOfferingId(int offeringId) {
        this.offeringId = offeringId;
    }
    
    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public Double getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            this.studentId = student.getStudentId();
        }
    }
    
    public CourseOffering getCourseOffering() {
        return courseOffering;
    }
    
    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
        if (courseOffering != null) {
            this.offeringId = courseOffering.getOfferingId();
        }
    }
    
    public Score getScore() {
        return score;
    }
    
    public void setScore(Score score) {
        this.score = score;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Check if enrollment is active
     */
    public boolean isActive() {
        return "ENROLLED".equals(status);
    }
    
    /**
     * Check if enrollment is dropped
     */
    public boolean isDropped() {
        return "DROPPED".equals(status);
    }
    
    /**
     * Check if enrollment is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Check if enrollment is pending
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    /**
     * Mark as dropped
     */
    public void drop() {
        this.status = "DROPPED";
    }
    
    /**
     * Mark as completed
     */
    public void complete() {
        this.status = "COMPLETED";
    }
    
    /**
     * Mark as pending
     */
    public void setPending() {
        this.status = "PENDING";
    }
    
    /**
     * Check if score has been entered
     */
    public boolean hasScore() {
        return score != null;
    }
    
    /**
     * Check if grade has been awarded
     */
    public boolean hasGrade() {
        return grade != null && !grade.isEmpty();
    }
    
    /**
     * Get the course associated with this enrollment
     */
    public Course getCourse() {
        if (courseOffering != null) {
            return courseOffering.getCourse();
        }
        return null;
    }
    
    /**
     * Get the course code
     */
    public String getCourseCode() {
        Course course = getCourse();
        return course != null ? course.getCourseCode() : "";
    }
    
    /**
     * Get the course title
     */
    public String getCourseTitle() {
        Course course = getCourse();
        return course != null ? course.getCourseTitle() : "";
    }
    
    /**
     * Get the lecturer teaching this course
     */
    public Lecturer getLecturer() {
        if (courseOffering != null) {
            return courseOffering.getLecturer();
        }
        return null;
    }
    
    /**
     * Get the lecturer name
     */
    public String getLecturerName() {
        Lecturer lecturer = getLecturer();
        return lecturer != null ? lecturer.getName() : "";
    }
    
    /**
     * Get the semester
     */
    public Semester getSemester() {
        if (courseOffering != null) {
            return courseOffering.getSemester();
        }
        return null;
    }
    
    /**
     * Get the academic year
     */
    public String getAcademicYear() {
        if (courseOffering != null) {
            return courseOffering.getAcademicYear();
        }
        return "";
    }
    
    /**
     * Get the semester name
     */
    public String getSemesterName() {
        if (courseOffering != null) {
            return courseOffering.getSemesterName();
        }
        return "";
    }
    
    /**
     * Get the credit units for this course
     */
    public int getCreditUnits() {
        Course course = getCourse();
        return course != null ? course.getCreditUnits() : 0;
    }
    
    /**
     * Get student name
     */
    public String getStudentName() {
        return student != null ? student.getName() : "";
    }
    
    /**
     * Get student registration number
     */
    public String getStudentRegNumber() {
        return student != null ? student.getRegistrationNumber() : "";
    }
    
    /**
     * Check if enrollment is within add/drop period
     */
    public boolean isWithinAddDropPeriod() {
        if (courseOffering == null || courseOffering.getSemester() == null) {
            return false;
        }
        
        Semester semester = courseOffering.getSemester();
        Date today = new Date();
        
        // Check if today is before the add/drop deadline
        return semester.getAddDropDeadline() != null && 
               !today.after(semester.getAddDropDeadline());
    }
    
    /**
     * Get status display with color indicator
     */
    public String getStatusDisplay() {
        switch (status) {
            case "ENROLLED":
                return "🟢 Enrolled";
            case "DROPPED":
                return "🔴 Dropped";
            case "COMPLETED":
                return "✅ Completed";
            case "PENDING":
                return "⏳ Pending";
            default:
                return status;
        }
    }
    
    /**
     * Get grade display
     */
    public String getGradeDisplay() {
        if (grade != null && !grade.isEmpty()) {
            return grade;
        }
        if (score != null && score.getGrade() != null) {
            return score.getGrade();
        }
        return "-";
    }
    
    /**
     * Get score display
     */
    public String getScoreDisplay() {
        if (totalScore != null) {
            return String.format("%.1f", totalScore);
        }
        if (score != null) {
            return String.format("%.1f", score.getTotalScore());
        }
        return "-";
    }
    
    /**
     * Format enrollment info for display
     */
    public String toDisplayString() {
        return String.format("%s - %s (%s)",
            getCourseCode(),
            getCourseTitle(),
            getStatusDisplay()
        );
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return enrollmentId == that.enrollmentId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Enrollment{" +
               "enrollmentId=" + enrollmentId +
               ", studentId=" + studentId +
               ", offeringId=" + offeringId +
               ", status='" + status + '\'' +
               ", grade='" + grade + '\'' +
               ", score=" + totalScore +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Enrollment{" +
               "enrollmentId=" + enrollmentId +
               ", studentId=" + studentId +
               ", offeringId=" + offeringId +
               ", enrollmentDate=" + enrollmentDate +
               ", status='" + status + '\'' +
               ", grade='" + grade + '\'' +
               ", totalScore=" + totalScore +
               ", remarks='" + remarks + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentName='" + getStudentName() + '\'' +
               ", studentReg='" + getStudentRegNumber() + '\'' +
               ", courseCode='" + getCourseCode() + '\'' +
               ", courseTitle='" + getCourseTitle() + '\'' +
               ", semester='" + getSemesterName() + '\'' +
               ", lecturer='" + getLecturerName() + '\'' +
               ", credits=" + getCreditUnits() +
               '}';
    }
    
    /**
     * Summary string for UI lists
     */
    public String toSummaryString() {
        return String.format("%s | %s - %s | %s | %s",
            getStudentRegNumber(),
            getCourseCode(),
            getCourseTitle(),
            getStatusDisplay(),
            getGradeDisplay()
        );
    }
    
    /**
     * Transcript entry string
     */
    public String toTranscriptEntry() {
        return String.format("%-10s %-30s %3d %6s %4s",
            getCourseCode(),
            getCourseTitle().length() > 28 ? getCourseTitle().substring(0, 25) + "..." : getCourseTitle(),
            getCreditUnits(),
            getScoreDisplay(),
            getGradeDisplay()
        );
    }
}