package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private int courseId;
    private String courseCode;
    private String courseTitle;
    private int creditUnits;
    private String description;
    private String level;  
    private boolean isActive;
    private Timestamp createdAt;
    
    // Relationships
    private List<Programme> programmes;  // programmes this course belongs to
    private List<CourseOffering> offerings;  // when this course was offered
    private List<Prerequisite> prerequisites;  // courses required before this
    private List<Course> dependentCourses;  // courses that need this as prerequisite
    

    public Course() {
        this.programmes = new ArrayList<>();
        this.offerings = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
        this.dependentCourses = new ArrayList<>();
        this.isActive = true;
    }
    

    public Course(String courseCode, String courseTitle, int creditUnits, String level) {
        this();
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditUnits = creditUnits;
        this.level = level;
    }
    
    /**
     * Constructor with all basic fields (no relationships)
     */
    public Course(String courseCode, String courseTitle, int creditUnits, 
                  String description, String level, boolean isActive) {
        this();
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditUnits = creditUnits;
        this.description = description;
        this.level = level;
        this.isActive = isActive;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Course(int courseId, String courseCode, String courseTitle, int creditUnits,
                  String description, String level, boolean isActive, Timestamp createdAt) {
        this();
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditUnits = creditUnits;
        this.description = description;
        this.level = level;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseTitle() {
        return courseTitle;
    }
    
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
    
    public int getCreditUnits() {
        return creditUnits;
    }
    
    public void setCreditUnits(int creditUnits) {
        this.creditUnits = creditUnits;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
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
    
    public List<Programme> getProgrammes() {
        return programmes;
    }
    
    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }
    
    public List<CourseOffering> getOfferings() {
        return offerings;
    }
    
    public void setOfferings(List<CourseOffering> offerings) {
        this.offerings = offerings;
    }
    
    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }
    
    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }
    
    public List<Course> getDependentCourses() {
        return dependentCourses;
    }
    
    public void setDependentCourses(List<Course> dependentCourses) {
        this.dependentCourses = dependentCourses;
    }
    

    public void addProgramme(Programme programme) {
        if (this.programmes == null) {
            this.programmes = new ArrayList<>();
        }
        if (!this.programmes.contains(programme)) {
            this.programmes.add(programme);
        }
    }
    

    public boolean removeProgramme(Programme programme) {
        if (this.programmes != null) {
            return this.programmes.remove(programme);
        }
        return false;
    }
   
    public void addOffering(CourseOffering offering) {
        if (this.offerings == null) {
            this.offerings = new ArrayList<>();
        }
        this.offerings.add(offering);
    }
    

    public void addPrerequisite(Course prerequisite, String requirementType) {
        if (this.prerequisites == null) {
            this.prerequisites = new ArrayList<>();
        }
        
        Prerequisite prereq = new Prerequisite();
        prereq.setCourse(this);
        prereq.setPrerequisiteCourse(prerequisite);
        prereq.setRequirementType(requirementType);
        
        this.prerequisites.add(prereq);
    }
    
   
    public boolean hasPrerequisites() {
        return prerequisites != null && !prerequisites.isEmpty();
    }
    

    public List<Integer> getPrerequisiteIds() {
        List<Integer> ids = new ArrayList<>();
        if (prerequisites != null) {
            for (Prerequisite prereq : prerequisites) {
                if (prereq.getPrerequisiteCourse() != null) {
                    ids.add(prereq.getPrerequisiteCourse().getCourseId());
                }
            }
        }
        return ids;
    }
    
  
    public List<String> getPrerequisiteCodes() {
        List<String> codes = new ArrayList<>();
        if (prerequisites != null) {
            for (Prerequisite prereq : prerequisites) {
                if (prereq.getPrerequisiteCourse() != null) {
                    codes.add(prereq.getPrerequisiteCourse().getCourseCode());
                }
            }
        }
        return codes;
    }
 
    public boolean isOfferedInSemester(int semesterId) {
        if (offerings != null) {
            for (CourseOffering offering : offerings) {
                if (offering.getSemesterId() == semesterId) {
                    return true;
                }
            }
        }
        return false;
    }
    


    public int getTotalEnrolledStudents() {
        int total = 0;
        if (offerings != null) {
            for (CourseOffering offering : offerings) {
                total += offering.getEnrolledCount();
            }
        }
        return total;
    }
    
 
    public CourseOffering getCurrentOffering() {
        if (offerings != null) {
            for (CourseOffering offering : offerings) {
                if ("ONGOING".equals(offering.getStatus())) {
                    return offering;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if course is currently being offered
     */
    public boolean isCurrentlyOffered() {
        return getCurrentOffering() != null;
    }
    
    /**
     * Get the year level as integer
     */
    public int getYearLevelAsInt() {
        try {
            return Integer.parseInt(level);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Check if this is a first-year course
     */
    public boolean isFirstYear() {
        return "100".equals(level);
    }
    
    /**
     * Check if this is a final-year course
     */
    public boolean isFinalYear() {
        return "400".equals(level) || "500".equals(level);
    }
    
    /**
     * Get a display string with code and title
     */
    public String getDisplayName() {
        return courseCode + " - " + courseTitle;
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Course{" +
               "courseId=" + courseId +
               ", courseCode='" + courseCode + '\'' +
               ", courseTitle='" + courseTitle + '\'' +
               ", creditUnits=" + creditUnits +
               ", level='" + level + '\'' +
               ", isActive=" + isActive +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Course{" +
               "courseId=" + courseId +
               ", courseCode='" + courseCode + '\'' +
               ", courseTitle='" + courseTitle + '\'' +
               ", creditUnits=" + creditUnits +
               ", description='" + description + '\'' +
               ", level='" + level + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", programmeCount=" + (programmes != null ? programmes.size() : 0) +
               ", offeringCount=" + (offerings != null ? offerings.size() : 0) +
               ", prerequisiteCount=" + (prerequisites != null ? prerequisites.size() : 0) +
               '}';
    }
    
    // ============ INNER CLASS FOR PREREQUISITE ============
    
    /**
     * Inner class to handle course prerequisites
     * (This would normally be a separate file, but included here for completeness)
     */
    public static class Prerequisite {
        private int prerequisiteId;
        private Course course;
        private Course prerequisiteCourse;
        private String requirementType;  // "MANDATORY", "RECOMMENDED"
        private Integer minimumGrade;  // minimum grade required if any
        
        public Prerequisite() {}
        
        public int getPrerequisiteId() { return prerequisiteId; }
        public void setPrerequisiteId(int prerequisiteId) { this.prerequisiteId = prerequisiteId; }
        
        public Course getCourse() { return course; }
        public void setCourse(Course course) { this.course = course; }
        
        public Course getPrerequisiteCourse() { return prerequisiteCourse; }
        public void setPrerequisiteCourse(Course prerequisiteCourse) { this.prerequisiteCourse = prerequisiteCourse; }
        
        public String getRequirementType() { return requirementType; }
        public void setRequirementType(String requirementType) { this.requirementType = requirementType; }
        
        public Integer getMinimumGrade() { return minimumGrade; }
        public void setMinimumGrade(Integer minimumGrade) { this.minimumGrade = minimumGrade; }
        
        public String getPrerequisiteCode() {
            return prerequisiteCourse != null ? prerequisiteCourse.getCourseCode() : "";
        }
        
        public String getPrerequisiteTitle() {
            return prerequisiteCourse != null ? prerequisiteCourse.getCourseTitle() : "";
        }
        
        @Override
        public String toString() {
            return "Prerequisite{" +
                   "course=" + (course != null ? course.getCourseCode() : "null") +
                   " requires " + (prerequisiteCourse != null ? prerequisiteCourse.getCourseCode() : "null") +
                   ", type='" + requirementType + '\'' +
                   '}';
        }
    }
}