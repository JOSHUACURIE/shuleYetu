package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {
    private int departmentId;
    private String departmentCode;
    private String departmentName;
    private String faculty;
    private String description;
    private String headOfDepartment;  // name or reference to lecturer
    private Integer headLecturerId;    // ID of the lecturer who is HOD
    private String contactEmail;
    private String contactPhone;
    private String officeLocation;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private List<Lecturer> lecturers;  // lecturers in this department
    private List<Programme> programmes;  // programmes offered by this department
    private List<Course> courses;  // courses owned by this department
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Department() {
        this.lecturers = new ArrayList<>();
        this.programmes = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.isActive = true;
    }
    
    /**
     * Constructor with required fields
     */
    public Department(String departmentCode, String departmentName, String faculty) {
        this();
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.faculty = faculty;
    }
    
    /**
     * Constructor with all basic fields (no relationships)
     */
    public Department(String departmentCode, String departmentName, String faculty,
                     String description, String headOfDepartment, Integer headLecturerId,
                     String contactEmail, String contactPhone, String officeLocation,
                     boolean isActive) {
        this(departmentCode, departmentName, faculty);
        this.description = description;
        this.headOfDepartment = headOfDepartment;
        this.headLecturerId = headLecturerId;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.officeLocation = officeLocation;
        this.isActive = isActive;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Department(int departmentId, String departmentCode, String departmentName, 
                     String faculty, String description, String headOfDepartment, 
                     Integer headLecturerId, String contactEmail, String contactPhone,
                     String officeLocation, boolean isActive, Timestamp createdAt, 
                     Timestamp updatedAt) {
        this(departmentCode, departmentName, faculty, description, headOfDepartment, 
             headLecturerId, contactEmail, contactPhone, officeLocation, isActive);
        this.departmentId = departmentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentCode() {
        return departmentCode;
    }
    
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getFaculty() {
        return faculty;
    }
    
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getHeadOfDepartment() {
        return headOfDepartment;
    }
    
    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }
    
    public Integer getHeadLecturerId() {
        return headLecturerId;
    }
    
    public void setHeadLecturerId(Integer headLecturerId) {
        this.headLecturerId = headLecturerId;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getOfficeLocation() {
        return officeLocation;
    }
    
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    
    public List<Lecturer> getLecturers() {
        return lecturers;
    }
    
    public void setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }
    
    public List<Programme> getProgrammes() {
        return programmes;
    }
    
    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Add a lecturer to this department
     */
    public void addLecturer(Lecturer lecturer) {
        if (this.lecturers == null) {
            this.lecturers = new ArrayList<>();
        }
        if (!this.lecturers.contains(lecturer)) {
            this.lecturers.add(lecturer);
            lecturer.setDepartmentId(this.departmentId);
        }
    }
    
    /**
     * Remove a lecturer from this department
     */
    public boolean removeLecturer(Lecturer lecturer) {
        if (this.lecturers != null) {
            return this.lecturers.remove(lecturer);
        }
        return false;
    }
    
    /**
     * Add a programme to this department
     */
    public void addProgramme(Programme programme) {
        if (this.programmes == null) {
            this.programmes = new ArrayList<>();
        }
        if (!this.programmes.contains(programme)) {
            this.programmes.add(programme);
        }
    }
    
    /**
     * Remove a programme from this department
     */
    public boolean removeProgramme(Programme programme) {
        if (this.programmes != null) {
            return this.programmes.remove(programme);
        }
        return false;
    }
    
    /**
     * Add a course to this department
     */
    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        }
        if (!this.courses.contains(course)) {
            this.courses.add(course);
        }
    }
    
    /**
     * Remove a course from this department
     */
    public boolean removeCourse(Course course) {
        if (this.courses != null) {
            return this.courses.remove(course);
        }
        return false;
    }
    
    /**
     * Get number of lecturers in department
     */
    public int getLecturerCount() {
        return lecturers != null ? lecturers.size() : 0;
    }
    
    /**
     * Get number of programmes in department
     */
    public int getProgrammeCount() {
        return programmes != null ? programmes.size() : 0;
    }
    
    /**
     * Get number of courses in department
     */
    public int getCourseCount() {
        return courses != null ? courses.size() : 0;
    }
    
    /**
     * Get total students across all programmes in this department
     */
    public int getTotalStudents() {
        int total = 0;
        if (programmes != null) {
            for (Programme programme : programmes) {
                total += programme.getStudentCount();
            }
        }
        return total;
    }
    
    /**
     * Check if a lecturer is the HOD
     */
    public boolean isHeadOfDepartment(Lecturer lecturer) {
        return headLecturerId != null && 
               lecturer != null && 
               headLecturerId.equals(lecturer.getLecturerId());
    }
    
    /**
     * Set the Head of Department
     */
    public void setHeadOfDepartment(Lecturer lecturer) {
        if (lecturer != null) {
            this.headOfDepartment = lecturer.getName();
            this.headLecturerId = lecturer.getLecturerId();
        }
    }
    
    /**
     * Remove the Head of Department
     */
    public void removeHeadOfDepartment() {
        this.headOfDepartment = null;
        this.headLecturerId = null;
    }
    
    /**
     * Get active lecturers only
     */
    public List<Lecturer> getActiveLecturers() {
        List<Lecturer> active = new ArrayList<>();
        if (lecturers != null) {
            for (Lecturer lecturer : lecturers) {
                if (lecturer.isActive()) {
                    active.add(lecturer);
                }
            }
        }
        return active;
    }
    
    /**
     * Get active programmes only
     */
    public List<Programme> getActiveProgrammes() {
        List<Programme> active = new ArrayList<>();
        if (programmes != null) {
            for (Programme programme : programmes) {
                if (programme.isActive()) {
                    active.add(programme);
                }
            }
        }
        return active;
    }
    
    /**
     * Get active courses only
     */
    public List<Course> getActiveCourses() {
        List<Course> active = new ArrayList<>();
        if (courses != null) {
            for (Course course : courses) {
                if (course.isActive()) {
                    active.add(course);
                }
            }
        }
        return active;
    }
    
    /**
     * Search lecturers by name
     */
    public List<Lecturer> searchLecturers(String keyword) {
        List<Lecturer> results = new ArrayList<>();
        if (lecturers != null && keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            for (Lecturer lecturer : lecturers) {
                if (lecturer.getName() != null && 
                    lecturer.getName().toLowerCase().contains(lowerKeyword)) {
                    results.add(lecturer);
                }
            }
        }
        return results;
    }
    
    /**
     * Search programmes by name
     */
    public List<Programme> searchProgrammes(String keyword) {
        List<Programme> results = new ArrayList<>();
        if (programmes != null && keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            for (Programme programme : programmes) {
                if (programme.getProgrammeName() != null && 
                    programme.getProgrammeName().toLowerCase().contains(lowerKeyword)) {
                    results.add(programme);
                }
            }
        }
        return results;
    }
    
    /**
     * Get department contact info as formatted string
     */
    public String getContactInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Department: ").append(departmentName).append("\n");
        info.append("Faculty: ").append(faculty).append("\n");
        info.append("HOD: ").append(headOfDepartment != null ? headOfDepartment : "Not Assigned").append("\n");
        info.append("Email: ").append(contactEmail != null ? contactEmail : "N/A").append("\n");
        info.append("Phone: ").append(contactPhone != null ? contactPhone : "N/A").append("\n");
        info.append("Location: ").append(officeLocation != null ? officeLocation : "N/A");
        return info.toString();
    }
    
    /**
     * Get a summary of department statistics
     */
    public String getStatisticsSummary() {
        return String.format(
            "Department: %s | Faculty: %s | Lecturers: %d | Programmes: %d | Courses: %d | Students: %d",
            departmentName, faculty, getLecturerCount(), getProgrammeCount(), 
            getCourseCount(), getTotalStudents()
        );
    }
    
    /**
     * Check if department has any active programmes
     */
    public boolean hasActiveProgrammes() {
        return !getActiveProgrammes().isEmpty();
    }
    
    /**
     * Check if department has any active lecturers
     */
    public boolean hasActiveLecturers() {
        return !getActiveLecturers().isEmpty();
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return departmentId == that.departmentId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(departmentId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Department{" +
               "departmentId=" + departmentId +
               ", departmentCode='" + departmentCode + '\'' +
               ", departmentName='" + departmentName + '\'' +
               ", faculty='" + faculty + '\'' +
               ", isActive=" + isActive +
               '}';
    }
    
 
    public String toDetailedString() {
        return "Department{" +
               "departmentId=" + departmentId +
               ", departmentCode='" + departmentCode + '\'' +
               ", departmentName='" + departmentName + '\'' +
               ", faculty='" + faculty + '\'' +
               ", description='" + description + '\'' +
               ", headOfDepartment='" + headOfDepartment + '\'' +
               ", headLecturerId=" + headLecturerId +
               ", contactEmail='" + contactEmail + '\'' +
               ", contactPhone='" + contactPhone + '\'' +
               ", officeLocation='" + officeLocation + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", lecturerCount=" + (lecturers != null ? lecturers.size() : 0) +
               ", programmeCount=" + (programmes != null ? programmes.size() : 0) +
               ", courseCount=" + (courses != null ? courses.size() : 0) +
               '}';
    }
    
    
    public String toDisplayString() {
        return departmentCode + " - " + departmentName + " (" + faculty + ")";
    }
}