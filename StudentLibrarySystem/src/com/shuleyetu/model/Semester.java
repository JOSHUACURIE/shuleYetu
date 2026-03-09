package com.shuleyetu.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Semester {
    
    private int semesterId;
    private int academicYearId;
    private String semesterNumber;  // "1", "2", "3"
    private Date startDate;
    private Date endDate;
    private Date registrationStartDate;
    private Date registrationEndDate;
    private Date addDropDeadline;
    private Date examStartDate;
    private Date examEndDate;
    private boolean isCurrent;
    private String status;  // "UPCOMING", "ONGOING", "COMPLETED"
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private AcademicYear academicYear;
    private List<CourseOffering> courseOfferings;
    private List<Enrollment> enrollments;
    private List<ResultSlip> resultSlips;
    
    // Constants
    public static final String SEMESTER_1 = "1";
    public static final String SEMESTER_2 = "2";
    public static final String SEMESTER_3 = "3";
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Semester() {
        this.courseOfferings = new ArrayList<>();
        this.enrollments = new ArrayList<>();
        this.resultSlips = new ArrayList<>();
        this.isCurrent = false;
        this.status = "UPCOMING";
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public Semester(int academicYearId, String semesterNumber, Date startDate, Date endDate) {
        this();
        this.academicYearId = academicYearId;
        this.semesterNumber = semesterNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        updateStatusBasedOnDates();
    }
    
    /**
     * Constructor with academic year object
     */
    public Semester(AcademicYear academicYear, String semesterNumber, Date startDate, Date endDate) {
        this();
        this.academicYear = academicYear;
        this.academicYearId = academicYear != null ? academicYear.getAcademicYearId() : 0;
        this.semesterNumber = semesterNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        updateStatusBasedOnDates();
    }
    
    /**
     * Constructor with all basic fields
     */
    public Semester(int academicYearId, String semesterNumber, Date startDate, Date endDate,
                   Date registrationStartDate, Date registrationEndDate, Date addDropDeadline,
                   Date examStartDate, Date examEndDate, boolean isCurrent, String description) {
        this(academicYearId, semesterNumber, startDate, endDate);
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.addDropDeadline = addDropDeadline;
        this.examStartDate = examStartDate;
        this.examEndDate = examEndDate;
        this.isCurrent = isCurrent;
        this.description = description;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Semester(int semesterId, int academicYearId, String semesterNumber,
                   Date startDate, Date endDate, Date registrationStartDate,
                   Date registrationEndDate, Date addDropDeadline, Date examStartDate,
                   Date examEndDate, boolean isCurrent, String status, String description,
                   Timestamp createdAt, Timestamp updatedAt) {
        this.semesterId = semesterId;
        this.academicYearId = academicYearId;
        this.semesterNumber = semesterNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.addDropDeadline = addDropDeadline;
        this.examStartDate = examStartDate;
        this.examEndDate = examEndDate;
        this.isCurrent = isCurrent;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.courseOfferings = new ArrayList<>();
        this.enrollments = new ArrayList<>();
        this.resultSlips = new ArrayList<>();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getSemesterId() {
        return semesterId;
    }
    
    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }
    
    public int getAcademicYearId() {
        return academicYearId;
    }
    
    public void setAcademicYearId(int academicYearId) {
        this.academicYearId = academicYearId;
        updateTimestamp();
    }
    
    public String getSemesterNumber() {
        return semesterNumber;
    }
    
    public void setSemesterNumber(String semesterNumber) {
        this.semesterNumber = semesterNumber;
        updateTimestamp();
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        updateStatusBasedOnDates();
        updateTimestamp();
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        updateStatusBasedOnDates();
        updateTimestamp();
    }
    
    public Date getRegistrationStartDate() {
        return registrationStartDate;
    }
    
    public void setRegistrationStartDate(Date registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
        updateTimestamp();
    }
    
    public Date getRegistrationEndDate() {
        return registrationEndDate;
    }
    
    public void setRegistrationEndDate(Date registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
        updateTimestamp();
    }
    
    public Date getAddDropDeadline() {
        return addDropDeadline;
    }
    
    public void setAddDropDeadline(Date addDropDeadline) {
        this.addDropDeadline = addDropDeadline;
        updateTimestamp();
    }
    
    public Date getExamStartDate() {
        return examStartDate;
    }
    
    public void setExamStartDate(Date examStartDate) {
        this.examStartDate = examStartDate;
        updateTimestamp();
    }
    
    public Date getExamEndDate() {
        return examEndDate;
    }
    
    public void setExamEndDate(Date examEndDate) {
        this.examEndDate = examEndDate;
        updateTimestamp();
    }
    
    public boolean isCurrent() {
        return isCurrent;
    }
    
    public void setCurrent(boolean current) {
        isCurrent = current;
        if (current) {
            this.status = "ONGOING";
        }
        updateTimestamp();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        updateTimestamp();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public AcademicYear getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
        if (academicYear != null) {
            this.academicYearId = academicYear.getAcademicYearId();
        }
        updateTimestamp();
    }
    
    public List<CourseOffering> getCourseOfferings() {
        return courseOfferings;
    }
    
    public void setCourseOfferings(List<CourseOffering> courseOfferings) {
        this.courseOfferings = courseOfferings;
        updateTimestamp();
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
        updateTimestamp();
    }
    
    public List<ResultSlip> getResultSlips() {
        return resultSlips;
    }
    
    public void setResultSlips(List<ResultSlip> resultSlips) {
        this.resultSlips = resultSlips;
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
     * Update status based on current date
     */
    private void updateStatusBasedOnDates() {
        if (startDate == null || endDate == null) return;
        
        Date today = new Date(System.currentTimeMillis());
        
        if (today.before(startDate)) {
            this.status = "UPCOMING";
        } else if (today.after(endDate)) {
            this.status = "COMPLETED";
        } else {
            this.status = "ONGOING";
        }
    }
    
    /**
     * Add a course offering to this semester
     */
    public void addCourseOffering(CourseOffering offering) {
        if (this.courseOfferings == null) {
            this.courseOfferings = new ArrayList<>();
        }
        if (!this.courseOfferings.contains(offering)) {
            this.courseOfferings.add(offering);
            offering.setSemesterId(this.semesterId);
            updateTimestamp();
        }
    }
    
    /**
     * Remove a course offering from this semester
     */
    public boolean removeCourseOffering(CourseOffering offering) {
        if (this.courseOfferings != null) {
            updateTimestamp();
            return this.courseOfferings.remove(offering);
        }
        return false;
    }
    
    /**
     * Add an enrollment to this semester
     */
    public void addEnrollment(Enrollment enrollment) {
        if (this.enrollments == null) {
            this.enrollments = new ArrayList<>();
        }
        if (!this.enrollments.contains(enrollment)) {
            this.enrollments.add(enrollment);
            updateTimestamp();
        }
    }
    
    /**
     * Remove an enrollment from this semester
     */
    public boolean removeEnrollment(Enrollment enrollment) {
        if (this.enrollments != null) {
            updateTimestamp();
            return this.enrollments.remove(enrollment);
        }
        return false;
    }
    
    /**
     * Get number of course offerings
     */
    public int getCourseOfferingCount() {
        return courseOfferings != null ? courseOfferings.size() : 0;
    }
    
    /**
     * Get number of enrollments
     */
    public int getEnrollmentCount() {
        return enrollments != null ? enrollments.size() : 0;
    }
    
    /**
     * Check if semester is ongoing
     */
    public boolean isOngoing() {
        return "ONGOING".equals(status);
    }
    
    /**
     * Check if semester is upcoming
     */
    public boolean isUpcoming() {
        return "UPCOMING".equals(status);
    }
    
    /**
     * Check if semester is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Get duration in days
     */
    public long getDurationInDays() {
        if (startDate == null || endDate == null) return 0;
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Get days remaining until end
     */
    public long getDaysRemaining() {
        if (endDate == null) return 0;
        Date today = new Date(System.currentTimeMillis());
        if (today.after(endDate)) return 0;
        long diff = endDate.getTime() - today.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Get days since start
     */
    public long getDaysSinceStart() {
        if (startDate == null) return 0;
        Date today = new Date(System.currentTimeMillis());
        if (today.before(startDate)) return 0;
        long diff = today.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Get progress percentage
     */
    public double getProgressPercentage() {
        long total = getDurationInDays();
        if (total == 0) return 0;
        long elapsed = getDaysSinceStart();
        return (double) elapsed / total * 100;
    }
    
    /**
     * Check if registration is open
     */
    public boolean isRegistrationOpen() {
        if (registrationStartDate == null || registrationEndDate == null) return false;
        Date today = new Date(System.currentTimeMillis());
        return !today.before(registrationStartDate) && !today.after(registrationEndDate);
    }
    
    /**
     * Check if within add/drop period
     */
    public boolean isWithinAddDropPeriod() {
        if (addDropDeadline == null) return false;
        Date today = new Date(System.currentTimeMillis());
        return !today.after(addDropDeadline);
    }
    
    /**
     * Check if exams are ongoing
     */
    public boolean isExamPeriod() {
        if (examStartDate == null || examEndDate == null) return false;
        Date today = new Date(System.currentTimeMillis());
        return !today.before(examStartDate) && !today.after(examEndDate);
    }
    
    /**
     * Get semester name (e.g., "Semester 1")
     */
    public String getSemesterName() {
        return "Semester " + semesterNumber;
    }
    
    /**
     * Get full display name with academic year
     */
    public String getDisplayName() {
        String yearCode = academicYear != null ? academicYear.getYearCode() : String.valueOf(academicYearId);
        return "Semester " + semesterNumber + ", " + yearCode;
    }
    
    /**
     * Get short display name
     */
    public String getShortDisplayName() {
        return "S" + semesterNumber + " " + (academicYear != null ? academicYear.getYearCode() : "");
    }
    
    /**
     * Get formatted date range
     */
    public String getDateRangeString() {
        if (startDate == null || endDate == null) return "";
        return startDate.toString() + " to " + endDate.toString();
    }
    
    /**
     * Get human-readable date range
     */
    public String getFormattedDateRange() {
        if (startDate == null || endDate == null) return "";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startMonth = cal.get(Calendar.MONTH) + 1;
        int startDay = cal.get(Calendar.DAY_OF_MONTH);
        int startYear = cal.get(Calendar.YEAR);
        
        cal.setTime(endDate);
        int endMonth = cal.get(Calendar.MONTH) + 1;
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        int endYear = cal.get(Calendar.YEAR);
        
        return String.format("%02d/%02d/%d - %02d/%02d/%d",
            startDay, startMonth, startYear,
            endDay, endMonth, endYear);
    }
    
    /**
     * Get status with emoji
     */
    public String getStatusDisplay() {
        switch (status) {
            case "ONGOING":
                return "🟢 Ongoing";
            case "UPCOMING":
                return "⏳ Upcoming";
            case "COMPLETED":
                return "✅ Completed";
            default:
                return status;
        }
    }
    
    /**
     * Get semester info summary
     */
    public String getSummary() {
        return String.format("%s: %s - %s (%d days) %s",
            getDisplayName(),
            startDate != null ? startDate.toString() : "N/A",
            endDate != null ? endDate.toString() : "N/A",
            getDurationInDays(),
            getStatusDisplay()
        );
    }
    
    /**
     * Activate this semester (set as current)
     */
    public void activate() {
        this.isCurrent = true;
        this.status = "ONGOING";
        updateTimestamp();
    }
    
    /**
     * Deactivate this semester
     */
    public void deactivate() {
        this.isCurrent = false;
        if (isOngoing()) {
            this.status = "COMPLETED";
        }
        updateTimestamp();
    }
    
    /**
     * Check if semester dates are valid
     */
    public boolean isValid() {
        if (startDate == null || endDate == null) return false;
        if (endDate.before(startDate)) return false;
        
        if (registrationStartDate != null && registrationEndDate != null) {
            if (registrationEndDate.before(registrationStartDate)) return false;
        }
        
        if (examStartDate != null && examEndDate != null) {
            if (examEndDate.before(examStartDate)) return false;
        }
        
        return true;
    }
    
    /**
     * Get the next semester number
     */
    public String getNextSemesterNumber() {
        if (semesterNumber == null) return "1";
        
        switch (semesterNumber) {
            case "1": return "2";
            case "2": return "3";
            case "3": return "1";  // Next academic year
            default: return "1";
        }
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return semesterId == semester.semesterId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(semesterId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Semester{" +
               "semesterId=" + semesterId +
               ", academicYearId=" + academicYearId +
               ", semesterNumber='" + semesterNumber + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", isCurrent=" + isCurrent +
               ", status='" + status + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Semester{" +
               "semesterId=" + semesterId +
               ", academicYearId=" + academicYearId +
               ", semesterNumber='" + semesterNumber + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", registrationStartDate=" + registrationStartDate +
               ", registrationEndDate=" + registrationEndDate +
               ", addDropDeadline=" + addDropDeadline +
               ", examStartDate=" + examStartDate +
               ", examEndDate=" + examEndDate +
               ", isCurrent=" + isCurrent +
               ", status='" + status + '\'' +
               ", description='" + description + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", courseOfferingCount=" + getCourseOfferingCount() +
               ", enrollmentCount=" + getEnrollmentCount() +
               ", durationDays=" + getDurationInDays() +
               ", progress=" + String.format("%.1f", getProgressPercentage()) + "%" +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s [%s]",
            getDisplayName(),
            getStatusDisplay()
        );
    }
}