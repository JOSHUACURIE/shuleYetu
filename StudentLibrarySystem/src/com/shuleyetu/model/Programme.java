package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Programme {
    
    private int programmeId;
    private String programmeCode;
    private String programmeName;
    private int durationYears;
    private String description;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private List<Student> students;
    private List<Course> courses;  // via programme_course
    private List<ProgrammeCourse> programmeCourses;  // join entity with year/semester info
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Programme() {
        this.isActive = true;
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.programmeCourses = new ArrayList<>();
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public Programme(String programmeCode, String programmeName, int durationYears) {
        this();
        this.programmeCode = programmeCode;
        this.programmeName = programmeName;
        this.durationYears = durationYears;
    }
    
    /**
     * Constructor with all basic fields
     */
    public Programme(String programmeCode, String programmeName, int durationYears, 
                    String description, boolean isActive) {
        this(programmeCode, programmeName, durationYears);
        this.description = description;
        this.isActive = isActive;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Programme(int programmeId, String programmeCode, String programmeName, 
                    int durationYears, String description, boolean isActive,
                    Timestamp createdAt, Timestamp updatedAt) {
        this(programmeCode, programmeName, durationYears, description, isActive);
        this.programmeId = programmeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getProgrammeId() {
        return programmeId;
    }
    
    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }
    
    public String getProgrammeCode() {
        return programmeCode;
    }
    
    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }
    
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public int getDurationYears() {
        return durationYears;
    }
    
    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    
    public List<ProgrammeCourse> getProgrammeCourses() {
        return programmeCourses;
    }
    
    public void setProgrammeCourses(List<ProgrammeCourse> programmeCourses) {
        this.programmeCourses = programmeCourses;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Update the updatedAt timestamp
     */
    private void updateTimestamp() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Add a student to this programme
     */
    public void addStudent(Student student) {
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        if (!this.students.contains(student)) {
            this.students.add(student);
            updateTimestamp();
        }
    }
    
    /**
     * Remove a student from this programme
     */
    public boolean removeStudent(Student student) {
        if (this.students != null) {
            updateTimestamp();
            return this.students.remove(student);
        }
        return false;
    }
    
    /**
     * Add a course to this programme (with year and semester info)
     */
    public void addCourse(Course course, int yearLevel, String semester, boolean isCore) {
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        }
        if (!this.courses.contains(course)) {
            this.courses.add(course);
            
            // Create ProgrammeCourse relationship
            ProgrammeCourse pc = new ProgrammeCourse();
            pc.setProgramme(this);
            pc.setCourse(course);
            pc.setYearLevel(yearLevel);
            pc.setSemester(semester);
            pc.setCore(isCore);
            
            if (this.programmeCourses == null) {
                this.programmeCourses = new ArrayList<>();
            }
            this.programmeCourses.add(pc);
            updateTimestamp();
        }
    }
    
    /**
     * Remove a course from this programme
     */
    public boolean removeCourse(Course course) {
        if (this.courses != null) {
            // Remove from courses list
            boolean removed = this.courses.remove(course);
            
            // Remove from programmeCourses
            if (this.programmeCourses != null) {
                this.programmeCourses.removeIf(pc -> pc.getCourseId() == course.getCourseId());
            }
            
            if (removed) {
                updateTimestamp();
            }
            return removed;
        }
        return false;
    }
    
    /**
     * Get courses for a specific year and semester
     */
    public List<Course> getCoursesForYearAndSemester(int yearLevel, String semester) {
        List<Course> result = new ArrayList<>();
        if (programmeCourses != null) {
            for (ProgrammeCourse pc : programmeCourses) {
                if (pc.getYearLevel() == yearLevel && pc.getSemester().equals(semester)) {
                    result.add(pc.getCourse());
                }
            }
        }
        return result;
    }
    
    /**
     * Get core courses for a specific year
     */
    public List<Course> getCoreCourses(int yearLevel) {
        List<Course> result = new ArrayList<>();
        if (programmeCourses != null) {
            for (ProgrammeCourse pc : programmeCourses) {
                if (pc.getYearLevel() == yearLevel && pc.isCore()) {
                    result.add(pc.getCourse());
                }
            }
        }
        return result;
    }
    
    /**
     * Get elective courses for a specific year
     */
    public List<Course> getElectiveCourses(int yearLevel) {
        List<Course> result = new ArrayList<>();
        if (programmeCourses != null) {
            for (ProgrammeCourse pc : programmeCourses) {
                if (pc.getYearLevel() == yearLevel && !pc.isCore()) {
                    result.add(pc.getCourse());
                }
            }
        }
        return result;
    }
    
    /**
     * Get total number of students in this programme
     */
    public int getStudentCount() {
        return students != null ? students.size() : 0;
    }
    
    /**
     * Get total number of courses in this programme
     */
    public int getCourseCount() {
        return courses != null ? courses.size() : 0;
    }
    
    /**
     * Get active students only
     */
    public List<Student> getActiveStudents() {
        List<Student> active = new ArrayList<>();
        if (students != null) {
            for (Student student : students) {
                if ("ACTIVE".equals(student.getStatus())) {
                    active.add(student);
                }
            }
        }
        return active;
    }
    
    /**
     * Check if programme has any active students
     */
    public boolean hasActiveStudents() {
        return !getActiveStudents().isEmpty();
    }
    
    /**
     * Get programme duration in semesters (2 semesters per year)
     */
    public int getDurationInSemesters() {
        return durationYears * 2;
    }
    
    /**
     * Get total credits required for this programme
     */
    public int getTotalCredits() {
        int total = 0;
        if (courses != null) {
            for (Course course : courses) {
                total += course.getCreditUnits();
            }
        }
        return total;
    }
    
    /**
     * Get programme display name with code
     */
    public String getDisplayName() {
        return programmeCode + " - " + programmeName;
    }
    
    /**
     * Get programme summary
     */
    public String getSummary() {
        return String.format("%s (%d years) - %d courses, %d students",
            programmeName, durationYears, getCourseCount(), getStudentCount());
    }
    
    /**
     * Check if programme is valid (has required fields)
     */
    public boolean isValid() {
        return programmeCode != null && !programmeCode.isEmpty() &&
               programmeName != null && !programmeName.isEmpty() &&
               durationYears > 0;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Programme programme = (Programme) o;
        return programmeId == programme.programmeId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(programmeId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Programme{" +
               "programmeId=" + programmeId +
               ", programmeCode='" + programmeCode + '\'' +
               ", programmeName='" + programmeName + '\'' +
               ", durationYears=" + durationYears +
               ", isActive=" + isActive +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Programme{" +
               "programmeId=" + programmeId +
               ", programmeCode='" + programmeCode + '\'' +
               ", programmeName='" + programmeName + '\'' +
               ", durationYears=" + durationYears +
               ", description='" + description + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentCount=" + getStudentCount() +
               ", courseCount=" + getCourseCount() +
               '}';
    }
}