package com.shuleyetu.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecturer extends Person {
    
    private int lecturerId;
    private String staffNumber;
    private Integer departmentId;
    private String qualification;
    private String specialization;
    private Date hireDate;
    private boolean isActive;
    private String officeLocation;
    private String contactEmail;
    private String contactPhone;
    private Timestamp lastActive;
    
    // Joined fields
    private String departmentName;
    
    // Relationships
    private List<CourseOffering> courseOfferings;
    private Department department;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Lecturer() {
        super();
        this.role = "LECTURER";
        this.isActive = true;
        this.courseOfferings = new ArrayList<>();
    }
    
    /**
     * Constructor with required fields
     */
    public Lecturer(String name, String email, String staffNumber) {
        super(name, email);
        this.staffNumber = staffNumber;
        this.role = "LECTURER";
        this.isActive = true;
        this.courseOfferings = new ArrayList<>();
    }
    
    /**
     * Constructor with all basic fields
     */
    public Lecturer(String name, String email, String phone, String staffNumber,
                   Integer departmentId, String qualification, String specialization,
                   Date hireDate, boolean isActive, String officeLocation,
                   String contactEmail, String contactPhone) {
        super(name, email, phone, "LECTURER");
        this.staffNumber = staffNumber;
        this.departmentId = departmentId;
        this.qualification = qualification;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.isActive = isActive;
        this.officeLocation = officeLocation;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.courseOfferings = new ArrayList<>();
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Lecturer(int personId, int lecturerId, String name, String email, String phone,
                   String staffNumber, Integer departmentId, String qualification,
                   String specialization, Date hireDate, boolean isActive,
                   String officeLocation, String contactEmail, String contactPhone,
                   Timestamp lastActive, Timestamp createdAt, Timestamp updatedAt) {
        super(personId, name, email, phone, "LECTURER", createdAt, updatedAt);
        this.lecturerId = lecturerId;
        this.staffNumber = staffNumber;
        this.departmentId = departmentId;
        this.qualification = qualification;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.isActive = isActive;
        this.officeLocation = officeLocation;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.lastActive = lastActive;
        this.courseOfferings = new ArrayList<>();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getLecturerId() {
        return lecturerId;
    }
    
    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
        super.setPersonId(lecturerId);  // Keep in sync with personId
    }
    
    public String getStaffNumber() {
        return staffNumber;
    }
    
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
    
    public Integer getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public Date getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getOfficeLocation() {
        return officeLocation;
    }
    
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
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
    
    public Timestamp getLastActive() {
        return lastActive;
    }
    
    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }
    
    // Joined fields
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    // Relationships
    public List<CourseOffering> getCourseOfferings() {
        return courseOfferings;
    }
    
    public void setCourseOfferings(List<CourseOffering> courseOfferings) {
        this.courseOfferings = courseOfferings;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
        if (department != null) {
            this.departmentId = department.getDepartmentId();
            this.departmentName = department.getDepartmentName();
        }
    }
    
    // ============ INHERITED PERSON METHODS ============
    
    @Override
    public int getPersonId() {
        return super.getPersonId();
    }
    
    @Override
    public void setPersonId(int personId) {
        super.setPersonId(personId);
        if (this.lecturerId == 0) {
            this.lecturerId = personId;  // Sync if lecturerId not set
        }
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Add a course offering to this lecturer
     */
    public void addCourseOffering(CourseOffering offering) {
        if (this.courseOfferings == null) {
            this.courseOfferings = new ArrayList<>();
        }
        if (!this.courseOfferings.contains(offering)) {
            this.courseOfferings.add(offering);
        }
    }
    
    /**
     * Remove a course offering from this lecturer
     */
    public boolean removeCourseOffering(CourseOffering offering) {
        if (this.courseOfferings != null) {
            return this.courseOfferings.remove(offering);
        }
        return false;
    }
    
    /**
     * Get number of courses currently teaching
     */
    public int getCurrentCourseCount() {
        if (courseOfferings == null) return 0;
        return (int) courseOfferings.stream()
                .filter(co -> "ONGOING".equals(co.getStatus()))
                .count();
    }
    
    /**
     * Get total number of courses ever taught
     */
    public int getTotalCourseCount() {
        return courseOfferings != null ? courseOfferings.size() : 0;
    }
    
    /**
     * Get current/ongoing course offerings
     */
    public List<CourseOffering> getCurrentOfferings() {
        List<CourseOffering> current = new ArrayList<>();
        if (courseOfferings != null) {
            for (CourseOffering co : courseOfferings) {
                if ("ONGOING".equals(co.getStatus())) {
                    current.add(co);
                }
            }
        }
        return current;
    }
    
    /**
     * Get completed course offerings
     */
    public List<CourseOffering> getCompletedOfferings() {
        List<CourseOffering> completed = new ArrayList<>();
        if (courseOfferings != null) {
            for (CourseOffering co : courseOfferings) {
                if ("COMPLETED".equals(co.getStatus())) {
                    completed.add(co);
                }
            }
        }
        return completed;
    }
    
    /**
     * Get total students taught across all courses
     */
    public int getTotalStudentsTaught() {
        int total = 0;
        if (courseOfferings != null) {
            for (CourseOffering co : courseOfferings) {
                total += co.getEnrolledCount();
            }
        }
        return total;
    }
    
    /**
     * Get years of service
     */
    public long getYearsOfService() {
        if (hireDate == null) return 0;
        long diff = System.currentTimeMillis() - hireDate.getTime();
        return diff / (1000L * 60 * 60 * 24 * 365);
    }
    
    /**
     * Check if lecturer is available (active and not on leave)
     */
    public boolean isAvailable() {
        return isActive;
    }
    
    /**
     * Update last active timestamp
     */
    public void updateLastActive() {
        this.lastActive = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Get full name with title
     */
    public String getFullNameWithTitle() {
        String title = "";
        if (qualification != null) {
            if (qualification.contains("PhD") || qualification.contains("Doctor")) {
                title = "Dr. ";
            } else if (qualification.contains("Master") || qualification.contains("MSc")) {
                title = "Prof. ";
            }
        }
        return title + getName();
    }
    
    /**
     * Get lecturer info for display
     */
    public String getLecturerInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getFullNameWithTitle());
        info.append(" (").append(staffNumber).append(")");
        if (departmentName != null) {
            info.append(" - ").append(departmentName);
        }
        return info.toString();
    }
    
    /**
     * Get teaching load summary
     */
    public String getTeachingLoadSummary() {
        return String.format("Teaching %d current courses, %d total students",
            getCurrentCourseCount(),
            getTotalStudentsTaught()
        );
    }
    
    /**
     * Check if lecturer teaches a specific course
     */
    public boolean teachesCourse(int courseId) {
        if (courseOfferings != null) {
            for (CourseOffering co : courseOfferings) {
                if (co.getCourseId() == courseId) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get courses taught in a specific semester
     */
    public List<CourseOffering> getCoursesInSemester(int semesterId) {
        List<CourseOffering> semesterCourses = new ArrayList<>();
        if (courseOfferings != null) {
            for (CourseOffering co : courseOfferings) {
                if (co.getSemesterId() == semesterId) {
                    semesterCourses.add(co);
                }
            }
        }
        return semesterCourses;
    }
       
    @Override
    public int getTypeId() {
        return lecturerId;
    }
    
    @Override
    public String getIdentifier() {
        return staffNumber;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lecturer lecturer = (Lecturer) o;
        return lecturerId == lecturer.lecturerId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lecturerId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Lecturer{" +
               "lecturerId=" + lecturerId +
               ", name='" + getName() + '\'' +
               ", staffNumber='" + staffNumber + '\'' +
               ", departmentId=" + departmentId +
               ", isActive=" + isActive +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Lecturer{" +
               "lecturerId=" + lecturerId +
               ", personId=" + getPersonId() +
               ", name='" + getName() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", phone='" + getPhone() + '\'' +
               ", staffNumber='" + staffNumber + '\'' +
               ", departmentId=" + departmentId +
               ", departmentName='" + departmentName + '\'' +
               ", qualification='" + qualification + '\'' +
               ", specialization='" + specialization + '\'' +
               ", hireDate=" + hireDate +
               ", isActive=" + isActive +
               ", officeLocation='" + officeLocation + '\'' +
               ", contactEmail='" + contactEmail + '\'' +
               ", contactPhone='" + contactPhone + '\'' +
               ", lastActive=" + lastActive +
               ", courseCount=" + (courseOfferings != null ? courseOfferings.size() : 0) +
               ", yearsOfService=" + getYearsOfService() +
               ", createdAt=" + getCreatedAt() +
               ", updatedAt=" + getUpdatedAt() +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s (%s) - %s",
            getFullNameWithTitle(),
            staffNumber,
            departmentName != null ? departmentName : "No Department"
        );
    }
}