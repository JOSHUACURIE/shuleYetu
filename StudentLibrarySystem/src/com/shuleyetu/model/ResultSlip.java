package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultSlip {
    
    private int resultSlipId;
    private int studentId;
    private int semesterId;
    private double gpa;
    private int totalCredits;
    private String status;  // "DRAFT", "PUBLISHED", "ARCHIVED"
    private Timestamp generatedDate;
    private Timestamp publishedDate;
    private Integer publishedBy;
    private String remarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Student student;
    private Semester semester;
    private Admin publisher;
    private List<ResultDetail> details;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public ResultSlip() {
        this.status = "DRAFT";
        this.generatedDate = new Timestamp(System.currentTimeMillis());
        this.details = new ArrayList<>();
        this.createdAt = this.generatedDate;
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public ResultSlip(int studentId, int semesterId) {
        this();
        this.studentId = studentId;
        this.semesterId = semesterId;
    }
    
    /**
     * Constructor with student and semester objects
     */
    public ResultSlip(Student student, Semester semester) {
        this();
        this.student = student;
        this.semester = semester;
        this.studentId = student != null ? student.getStudentId() : 0;
        this.semesterId = semester != null ? semester.getSemesterId() : 0;
    }
    
    /**
     * Constructor with calculated results
     */
    public ResultSlip(int studentId, int semesterId, double gpa, int totalCredits) {
        this(studentId, semesterId);
        this.gpa = gpa;
        this.totalCredits = totalCredits;
    }
    
    /**
     * Constructor with all basic fields
     */
    public ResultSlip(int studentId, int semesterId, double gpa, int totalCredits, 
                     String status, String remarks) {
        this(studentId, semesterId, gpa, totalCredits);
        this.status = status;
        this.remarks = remarks;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public ResultSlip(int resultSlipId, int studentId, int semesterId, double gpa, 
                     int totalCredits, String status, Timestamp generatedDate,
                     Timestamp publishedDate, Integer publishedBy, String remarks,
                     Timestamp createdAt, Timestamp updatedAt) {
        this.resultSlipId = resultSlipId;
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.status = status;
        this.generatedDate = generatedDate;
        this.publishedDate = publishedDate;
        this.publishedBy = publishedBy;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.details = new ArrayList<>();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getResultSlipId() {
        return resultSlipId;
    }
    
    public void setResultSlipId(int resultSlipId) {
        this.resultSlipId = resultSlipId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
        updateTimestamp();
    }
    
    public int getSemesterId() {
        return semesterId;
    }
    
    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
        updateTimestamp();
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        this.gpa = gpa;
        updateTimestamp();
    }
    
    public int getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
        updateTimestamp();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        updateTimestamp();
    }
    
    public Timestamp getGeneratedDate() {
        return generatedDate;
    }
    
    public void setGeneratedDate(Timestamp generatedDate) {
        this.generatedDate = generatedDate;
        updateTimestamp();
    }
    
    public Timestamp getPublishedDate() {
        return publishedDate;
    }
    
    public void setPublishedDate(Timestamp publishedDate) {
        this.publishedDate = publishedDate;
        updateTimestamp();
    }
    
    public Integer getPublishedBy() {
        return publishedBy;
    }
    
    public void setPublishedBy(Integer publishedBy) {
        this.publishedBy = publishedBy;
        updateTimestamp();
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
        if (student != null) {
            this.studentId = student.getStudentId();
        }
        updateTimestamp();
    }
    
    public Semester getSemester() {
        return semester;
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
        if (semester != null) {
            this.semesterId = semester.getSemesterId();
        }
        updateTimestamp();
    }
    
    public Admin getPublisher() {
        return publisher;
    }
    
    public void setPublisher(Admin publisher) {
        this.publisher = publisher;
        if (publisher != null) {
            this.publishedBy = publisher.getAdminId();
        }
        updateTimestamp();
    }
    
    public List<ResultDetail> getDetails() {
        return details;
    }
    
    public void setDetails(List<ResultDetail> details) {
        this.details = details;
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
     * Add a result detail to this slip
     */
    public void addDetail(ResultDetail detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        if (!this.details.contains(detail)) {
            this.details.add(detail);
            detail.setResultSlipId(this.resultSlipId);
            recalculateGpa();  // Recalculate GPA when adding a course
            updateTimestamp();
        }
    }
    
    /**
     * Remove a result detail from this slip
     */
    public boolean removeDetail(ResultDetail detail) {
        if (this.details != null) {
            boolean removed = this.details.remove(detail);
            if (removed) {
                recalculateGpa();
                updateTimestamp();
            }
            return removed;
        }
        return false;
    }
    
    /**
     * Recalculate GPA based on all result details
     */
    public void recalculateGpa() {
        if (details == null || details.isEmpty()) {
            this.gpa = 0.0;
            this.totalCredits = 0;
            return;
        }
        
        double totalGradePoints = 0.0;
        int totalCredits = 0;
        
        for (ResultDetail detail : details) {
            totalGradePoints += detail.getGradePoint() * detail.getCreditUnits();
            totalCredits += detail.getCreditUnits();
        }
        
        if (totalCredits > 0) {
            this.gpa = totalGradePoints / totalCredits;
            // Round to 2 decimal places
            this.gpa = Math.round(this.gpa * 100.0) / 100.0;
        } else {
            this.gpa = 0.0;
        }
        
        this.totalCredits = totalCredits;
    }
    
    /**
     * Publish the result slip
     */
    public void publish(int adminId) {
        this.status = "PUBLISHED";
        this.publishedDate = new Timestamp(System.currentTimeMillis());
        this.publishedBy = adminId;
        updateTimestamp();
    }
    
    /**
     * Unpublish the result slip (revert to draft)
     */
    public void unpublish() {
        this.status = "DRAFT";
        this.publishedDate = null;
        this.publishedBy = null;
        updateTimestamp();
    }
    
    /**
     * Archive the result slip
     */
    public void archive() {
        this.status = "ARCHIVED";
        updateTimestamp();
    }
    
    /**
     * Check if result slip is published
     */
    public boolean isPublished() {
        return "PUBLISHED".equals(status);
    }
    
    /**
     * Check if result slip is draft
     */
    public boolean isDraft() {
        return "DRAFT".equals(status);
    }
    
    /**
     * Check if result slip is archived
     */
    public boolean isArchived() {
        return "ARCHIVED".equals(status);
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
     * Get student programme
     */
    public String getStudentProgramme() {
        return student != null && student.getProgramme() != null ? 
               student.getProgramme().getProgrammeName() : "";
    }
    
    /**
     * Get semester name
     */
    public String getSemesterName() {
        return semester != null ? semester.getDisplayName() : "";
    }
    
    /**
     * Get academic year
     */
    public String getAcademicYear() {
        return semester != null && semester.getAcademicYear() != null ? 
               semester.getAcademicYear().getYearCode() : "";
    }
    
    /**
     * Get publisher name
     */
    public String getPublisherName() {
        return publisher != null ? publisher.getName() : "";
    }
    
    /**
     * Get formatted GPA with letter grade
     */
    public String getFormattedGpa() {
        return String.format("%.2f - %s", gpa, getGpaGrade());
    }
    
    /**
     * Get GPA letter grade
     */
    public String getGpaGrade() {
        if (gpa >= 4.0) return "A (Excellent)";
        if (gpa >= 3.5) return "B+ (Very Good)";
        if (gpa >= 3.0) return "B (Good)";
        if (gpa >= 2.5) return "C+ (Fair)";
        if (gpa >= 2.0) return "C (Satisfactory)";
        if (gpa >= 1.5) return "D+ (Pass)";
        if (gpa >= 1.0) return "D (Marginal Pass)";
        return "F (Fail)";
    }
    
    /**
     * Get status display
     */
    public String getStatusDisplay() {
        switch (status) {
            case "DRAFT":
                return "📝 Draft";
            case "PUBLISHED":
                return "✅ Published";
            case "ARCHIVED":
                return "📦 Archived";
            default:
                return status;
        }
    }
    
    /**
     * Check if result slip is complete (has all details)
     */
    public boolean isComplete() {
        return details != null && !details.isEmpty();
    }
    
    /**
     * Get number of courses in this result slip
     */
    public int getCourseCount() {
        return details != null ? details.size() : 0;
    }
    
    /**
     * Get result summary
     */
    public String getSummary() {
        return String.format("%s - %s: GPA %.2f (%d courses, %d credits)",
            getStudentName(),
            getSemesterName(),
            gpa,
            getCourseCount(),
            totalCredits
        );
    }
    
    /**
     * Get result detail for a specific course
     */
    public ResultDetail getDetailForCourse(int courseId) {
        if (details != null) {
            for (ResultDetail detail : details) {
                if (detail.getCourseId() == courseId) {
                    return detail;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if result slip has a specific course
     */
    public boolean hasCourse(int courseId) {
        return getDetailForCourse(courseId) != null;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultSlip that = (ResultSlip) o;
        return resultSlipId == that.resultSlipId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(resultSlipId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "ResultSlip{" +
               "resultSlipId=" + resultSlipId +
               ", studentId=" + studentId +
               ", semesterId=" + semesterId +
               ", gpa=" + gpa +
               ", totalCredits=" + totalCredits +
               ", status='" + status + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "ResultSlip{" +
               "resultSlipId=" + resultSlipId +
               ", studentId=" + studentId +
               ", semesterId=" + semesterId +
               ", gpa=" + gpa +
               ", totalCredits=" + totalCredits +
               ", status='" + status + '\'' +
               ", generatedDate=" + generatedDate +
               ", publishedDate=" + publishedDate +
               ", publishedBy=" + publishedBy +
               ", remarks='" + remarks + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentName='" + getStudentName() + '\'' +
               ", semesterName='" + getSemesterName() + '\'' +
               ", courseCount=" + getCourseCount() +
               '}';
    }
    
    /**
     * Transcript header string
     */
    public String toTranscriptHeader() {
        StringBuilder header = new StringBuilder();
        header.append("=".repeat(80)).append("\n");
        header.append("                    ACADEMIC TRANSCRIPT\n");
        header.append("=".repeat(80)).append("\n\n");
        header.append("Student: ").append(getStudentName()).append("\n");
        header.append("Reg No:  ").append(getStudentRegNumber()).append("\n");
        header.append("Programme: ").append(getStudentProgramme()).append("\n");
        header.append("Semester: ").append(getSemesterName()).append("\n");
        header.append("Academic Year: ").append(getAcademicYear()).append("\n");
        header.append("-".repeat(80)).append("\n");
        header.append(String.format("%-10s %-30s %-8s %-8s %-8s %-8s\n", 
            "Code", "Course Title", "Credits", "Score", "Grade", "GP"));
        header.append("-".repeat(80));
        return header.toString();
    }
    
    /**
     * Transcript footer string
     */
    public String toTranscriptFooter() {
        StringBuilder footer = new StringBuilder();
        footer.append("-".repeat(80)).append("\n");
        footer.append(String.format("Total Credits: %d\n", totalCredits));
        footer.append(String.format("GPA: %.2f - %s\n", gpa, getGpaGrade()));
        footer.append("Status: ").append(getStatusDisplay()).append("\n");
        if (publishedDate != null) {
            footer.append("Published: ").append(publishedDate).append("\n");
            footer.append("Published By: ").append(getPublisherName()).append("\n");
        }
        footer.append("=".repeat(80));
        return footer.toString();
    }
}