package com.shuleyetu.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AcademicYear {
    private int academicYearId;
    private String yearCode;  // e.g., "2025/2026"
    private Date startDate;
    private Date endDate;
    private boolean isCurrent;
    private String description;
    private String status;  // "UPCOMING", "ONGOING", "COMPLETED"
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private List<Semester> semesters;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public AcademicYear() {
        this.semesters = new ArrayList<>();
        this.isCurrent = false;
        this.status = "UPCOMING";
    }
    
    /**
     * Constructor with required fields
     */
    public AcademicYear(String yearCode, Date startDate, Date endDate) {
        this();
        this.yearCode = yearCode;
        this.startDate = startDate;
        this.endDate = endDate;
        updateStatusBasedOnDates();
    }
    
    /**
     * Constructor with all basic fields
     */
    public AcademicYear(String yearCode, Date startDate, Date endDate, 
                       boolean isCurrent, String description) {
        this(yearCode, startDate, endDate);
        this.isCurrent = isCurrent;
        this.description = description;
        updateStatusBasedOnDates();
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public AcademicYear(int academicYearId, String yearCode, Date startDate, Date endDate,
                       boolean isCurrent, String description, String status,
                       Timestamp createdAt, Timestamp updatedAt) {
        this(yearCode, startDate, endDate, isCurrent, description);
        this.academicYearId = academicYearId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getAcademicYearId() {
        return academicYearId;
    }
    
    public void setAcademicYearId(int academicYearId) {
        this.academicYearId = academicYearId;
    }
    
    public String getYearCode() {
        return yearCode;
    }
    
    public void setYearCode(String yearCode) {
        this.yearCode = yearCode;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        updateStatusBasedOnDates();
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        updateStatusBasedOnDates();
    }
    
    public boolean isCurrent() {
        return isCurrent;
    }
    
    public void setCurrent(boolean current) {
        isCurrent = current;
        if (current) {
            this.status = "ONGOING";
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Semester> getSemesters() {
        return semesters;
    }
    
    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
    
    // ============ BUSINESS METHODS ============
    
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
     * Add a semester to this academic year
     */
    public void addSemester(Semester semester) {
        if (this.semesters == null) {
            this.semesters = new ArrayList<>();
        }
        if (!this.semesters.contains(semester)) {
            this.semesters.add(semester);
            semester.setAcademicYearId(this.academicYearId);
        }
    }
    
    /**
     * Remove a semester from this academic year
     */
    public boolean removeSemester(Semester semester) {
        if (this.semesters != null) {
            return this.semesters.remove(semester);
        }
        return false;
    }
    
    /**
     * Get semester by number
     */
    public Semester getSemesterByNumber(String semesterNumber) {
        if (semesters != null) {
            for (Semester semester : semesters) {
                if (semester.getSemesterNumber().equals(semesterNumber)) {
                    return semester;
                }
            }
        }
        return null;
    }
    
    /**
     * Get current/active semester
     */
    public Semester getCurrentSemester() {
        if (semesters != null) {
            for (Semester semester : semesters) {
                if (semester.isCurrent()) {
                    return semester;
                }
            }
        }
        return null;
    }
    
    /**
     * Get number of semesters in this academic year
     */
    public int getSemesterCount() {
        return semesters != null ? semesters.size() : 0;
    }
    
    /**
     * Check if academic year is ongoing
     */
    public boolean isOngoing() {
        return "ONGOING".equals(status);
    }
    
    /**
     * Check if academic year is upcoming
     */
    public boolean isUpcoming() {
        return "UPCOMING".equals(status);
    }
    
    /**
     * Check if academic year is completed
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
     * Get duration in months
     */
    public long getDurationInMonths() {
        return getDurationInDays() / 30;
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
     * Generate year code from dates
     */
    public static String generateYearCode(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return "";
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startYear = cal.get(Calendar.YEAR);
        
        cal.setTime(endDate);
        int endYear = cal.get(Calendar.YEAR);
        
        return startYear + "/" + endYear;
    }
    
    // /**
    //  * Create default semesters for this academic year
    //  */
    // public void createDefaultSemesters() {
    //     if (startDate == null || endDate == null) return;
        
    //     Calendar cal = Calendar.getInstance();
    //     cal.setTime(startDate);
        
    //     // Semester 1: Start date to mid-year
    //     Calendar sem1End = Calendar.getInstance();
    //     sem1End.setTime(startDate);
    //     sem1End.add(Calendar.MONTH, 5); // ~5 months for semester 1
        
    //     Semester semester1 = new Semester(
    //         this.academicYearId,
    //         "1",
    //         new Date(startDate.getTime()),
    //         new Date(sem1End.getTimeInMillis()),
    //         false,
    //         null
    //     );
        
    //     // Semester 2: Mid-year to end date
    //     Calendar sem2Start = Calendar.getInstance();
    //     sem2Start.setTime(sem1End.getTime());
    //     sem2Start.add(Calendar.DAY_OF_MONTH, 1);
        
    //     Semester semester2 = new Semester(
    //         this.academicYearId,
    //         "2",
    //         new Date(sem2Start.getTimeInMillis()),
    //         new Date(endDate.getTime()),
    //         false,
    //         null
    //     );
        
    //     addSemester(semester1);
    //     addSemester(semester2);
    // }
    
    /**
     * Check if a given date falls within this academic year
     */
    public boolean containsDate(Date date) {
        if (startDate == null || endDate == null || date == null) return false;
        return !date.before(startDate) && !date.after(endDate);
    }
    
    /**
     * Get the next academic year code
     */
    public String getNextYearCode() {
        if (yearCode == null || !yearCode.contains("/")) return "";
        
        String[] years = yearCode.split("/");
        if (years.length != 2) return "";
        
        try {
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            return (startYear + 1) + "/" + (endYear + 1);
        } catch (NumberFormatException e) {
            return "";
        }
    }
    
    /**
     * Get the previous academic year code
     */
    public String getPreviousYearCode() {
        if (yearCode == null || !yearCode.contains("/")) return "";
        
        String[] years = yearCode.split("/");
        if (years.length != 2) return "";
        
        try {
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            return (startYear - 1) + "/" + (endYear - 1);
        } catch (NumberFormatException e) {
            return "";
        }
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
     * Get academic year display name
     */
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        sb.append(yearCode != null ? yearCode : "N/A");
        
        if (isCurrent) {
            sb.append(" (Current)");
        } else if (isOngoing()) {
            sb.append(" (Ongoing)");
        } else if (isUpcoming()) {
            sb.append(" (Upcoming)");
        } else if (isCompleted()) {
            sb.append(" (Completed)");
        }
        
        return sb.toString();
    }
    
    /**
     * Validate the academic year dates
     */
    public boolean isValid() {
        if (startDate == null || endDate == null) return false;
        return !endDate.before(startDate);
    }
    
    /**
     * Activate this academic year (set as current)
     */
    public void activate() {
        this.isCurrent = true;
        this.status = "ONGOING";
    }
    
    /**
     * Deactivate this academic year
     */
    public void deactivate() {
        this.isCurrent = false;
        if (isOngoing()) {
            this.status = "COMPLETED";
        }
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicYear that = (AcademicYear) o;
        return academicYearId == that.academicYearId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(academicYearId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "AcademicYear{" +
               "academicYearId=" + academicYearId +
               ", yearCode='" + yearCode + '\'' +
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
        return "AcademicYear{" +
               "academicYearId=" + academicYearId +
               ", yearCode='" + yearCode + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", isCurrent=" + isCurrent +
               ", status='" + status + '\'' +
               ", description='" + description + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", semesterCount=" + (semesters != null ? semesters.size() : 0) +
               ", durationDays=" + getDurationInDays() +
               ", progress=" + String.format("%.1f", getProgressPercentage()) + "%" +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s (%s - %s) %s",
            yearCode != null ? yearCode : "N/A",
            startDate != null ? startDate.toString() : "N/A",
            endDate != null ? endDate.toString() : "N/A",
            isCurrent ? "[CURRENT]" : "");
    }
}