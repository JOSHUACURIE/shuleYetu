package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseOffering {
    private int offeringId;
    private int courseId;
    private int lecturerId;
    private int semesterId;
    private int maxStudents;
    private int enrolledCount;
    private String status;  // "PLANNED", "ONGOING", "COMPLETED", "CANCELLED"
    private Timestamp createdAt;
    
    // Relationships
    private Course course;
    private Lecturer lecturer;
    private Semester semester;
    private List<Enrollment> enrollments;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public CourseOffering() {
        this.enrollments = new ArrayList<>();
        this.enrolledCount = 0;
        this.status = "PLANNED";
        this.maxStudents = 50;  // default value
    }
    
    /**
     * Constructor with required fields
     */
    public CourseOffering(int courseId, int lecturerId, int semesterId) {
        this();
        this.courseId = courseId;
        this.lecturerId = lecturerId;
        this.semesterId = semesterId;
    }
    
    /**
     * Constructor with all basic fields
     */
    public CourseOffering(int courseId, int lecturerId, int semesterId, 
                         int maxStudents, String status) {
        this(courseId, lecturerId, semesterId);
        this.maxStudents = maxStudents;
        this.status = status;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public CourseOffering(int offeringId, int courseId, int lecturerId, int semesterId,
                         int maxStudents, int enrolledCount, String status, Timestamp createdAt) {
        this();
        this.offeringId = offeringId;
        this.courseId = courseId;
        this.lecturerId = lecturerId;
        this.semesterId = semesterId;
        this.maxStudents = maxStudents;
        this.enrolledCount = enrolledCount;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getOfferingId() {
        return offeringId;
    }
    
    public void setOfferingId(int offeringId) {
        this.offeringId = offeringId;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public int getLecturerId() {
        return lecturerId;
    }
    
    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }
    
    public int getSemesterId() {
        return semesterId;
    }
    
    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }
    
    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    public int getEnrolledCount() {
        return enrolledCount;
    }
    
    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Lecturer getLecturer() {
        return lecturer;
    }
    
    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
    
    public Semester getSemester() {
        return semester;
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Check if offering has available slots
     */
    public boolean hasAvailableSlots() {
        return enrolledCount < maxStudents;
    }
    
    /**
     * Get available slots count
     */
    public int getAvailableSlots() {
        return maxStudents - enrolledCount;
    }
    
    /**
     * Check if offering is full
     */
    public boolean isFull() {
        return enrolledCount >= maxStudents;
    }
    
    /**
     * Check if offering is ongoing
     */
    public boolean isOngoing() {
        return "ONGOING".equals(status);
    }
    
    /**
     * Check if offering is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Check if offering is planned
     */
    public boolean isPlanned() {
        return "PLANNED".equals(status);
    }
    
    /**
     * Check if offering is cancelled
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Increment enrolled count (when student enrolls)
     */
    public void incrementEnrolledCount() {
        if (!isFull()) {
            this.enrolledCount++;
        }
    }
    
    /**
     * Decrement enrolled count (when student drops)
     */
    public void decrementEnrolledCount() {
        if (this.enrolledCount > 0) {
            this.enrolledCount--;
        }
    }
    
    /**
     * Add an enrollment to this offering
     */
    public void addEnrollment(Enrollment enrollment) {
        if (this.enrollments == null) {
            this.enrollments = new ArrayList<>();
        }
        if (!this.enrollments.contains(enrollment)) {
            this.enrollments.add(enrollment);
            incrementEnrolledCount();
        }
    }
    
    /**
     * Remove an enrollment from this offering
     */
    public boolean removeEnrollment(Enrollment enrollment) {
        if (this.enrollments != null && this.enrollments.remove(enrollment)) {
            decrementEnrolledCount();
            return true;
        }
        return false;
    }
    
    /**
     * Get enrollment percentage
     */
    public double getEnrollmentPercentage() {
        if (maxStudents == 0) return 0;
        return ((double) enrolledCount / maxStudents) * 100;
    }
    
    /**
     * Get the academic year from semester
     */
    public String getAcademicYear() {
        if (semester != null && semester.getAcademicYear() != null) {
            return semester.getAcademicYear().getYearCode();
        }
        return "";
    }
    
    /**
     * Get semester name (e.g., "Semester 1, 2025/2026")
     */
    public String getSemesterName() {
        if (semester != null) {
            String semNum = semester.getSemesterNumber();
            String year = getAcademicYear();
            return "Semester " + semNum + ", " + year;
        }
        return "";
    }
    
    /**
     * Get course code and title
     */
    public String getCourseDisplay() {
        if (course != null) {
            return course.getCourseCode() + " - " + course.getCourseTitle();
        }
        return "";
    }
    
    /**
     * Get lecturer name
     */
    public String getLecturerName() {
        if (lecturer != null) {
            return lecturer.getName();
        }
        return "";
    }
    
    /**
     * Get list of enrolled students
     */
    public List<Student> getEnrolledStudents() {
        List<Student> students = new ArrayList<>();
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent() != null && 
                    "ENROLLED".equals(enrollment.getStatus())) {
                    students.add(enrollment.getStudent());
                }
            }
        }
        return students;
    }
    
    /**
     * Get count of enrolled students (active only)
     */
    public int getActiveEnrollmentCount() {
        if (enrollments == null) return 0;
        return (int) enrollments.stream()
                .filter(e -> "ENROLLED".equals(e.getStatus()))
                .count();
    }
    
    /**
     * Start the offering (change status to ONGOING)
     */
    public void start() {
        this.status = "ONGOING";
    }
    
    /**
     * Complete the offering
     */
    public void complete() {
        this.status = "COMPLETED";
    }
    
    /**
     * Cancel the offering
     */
    public void cancel() {
        this.status = "CANCELLED";
    }
    
    /**
     * Check if offering can be started
     */
    public boolean canStart() {
        return "PLANNED".equals(status) && hasAvailableSlots();
    }
    
    /**
     * Check if offering can be completed
     */
    public boolean canComplete() {
        return "ONGOING".equals(status);
    }
    
    /**
     * Get the year level of the course
     */
    public String getCourseLevel() {
        if (course != null) {
            return course.getLevel();
        }
        return "";
    }
    
    /**
     * Get total credits for this offering
     */
    public int getTotalCredits() {
        if (course != null) {
            return course.getCreditUnits() * getActiveEnrollmentCount();
        }
        return 0;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseOffering that = (CourseOffering) o;
        return offeringId == that.offeringId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(offeringId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "CourseOffering{" +
               "offeringId=" + offeringId +
               ", courseId=" + courseId +
               ", lecturerId=" + lecturerId +
               ", semesterId=" + semesterId +
               ", enrolled=" + enrolledCount + "/" + maxStudents +
               ", status='" + status + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "CourseOffering{" +
               "offeringId=" + offeringId +
               ", courseId=" + courseId +
               ", lecturerId=" + lecturerId +
               ", semesterId=" + semesterId +
               ", maxStudents=" + maxStudents +
               ", enrolledCount=" + enrolledCount +
               ", status='" + status + '\'' +
               ", createdAt=" + createdAt +
               ", course=" + (course != null ? course.getCourseCode() : "null") +
               ", lecturer=" + (lecturer != null ? lecturer.getName() : "null") +
               ", semester=" + (semester != null ? semester.getSemesterNumber() : "null") +
               ", enrollmentCount=" + (enrollments != null ? enrollments.size() : 0) +
               '}';
    }
    
    /**
     * Summary string for display
     */
    public String toSummaryString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCourseDisplay());
        sb.append(" - ").append(getSemesterName());
        sb.append(" - Lecturer: ").append(getLecturerName());
        sb.append(" - Enrolled: ").append(enrolledCount).append("/").append(maxStudents);
        sb.append(" - ").append(status);
        return sb.toString();
    }
}