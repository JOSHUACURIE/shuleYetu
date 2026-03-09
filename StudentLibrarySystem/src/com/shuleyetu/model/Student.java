package com.shuleyetu.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends Person {
    
    private int studentId;
    private String registrationNumber;
    private int programmeId;
    private Date enrollmentDate;
    private Date expectedGraduationDate;
    private int currentYear;
    private String status;  // "ACTIVE", "GRADUATED", "SUSPENDED", "WITHDRAWN"
    private String studyMode;  // "FULL_TIME", "PART_TIME"
    private String gender;
    private String nationality;
    private String address;
    private String phoneNumber;
    private String emergencyContact;
    private String emergencyPhone;
    private String profilePicture;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joins
    private String programmeName;
    private String departmentName;
    private String facultyName;
    
    // Relationships
    private Programme programme;
    private List<Enrollment> enrollments;
    private List<BorrowRecord> borrowRecords;
    private List<Reservation> reservations;
    private List<ResultSlip> resultSlips;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Student() {
        super();
        this.role = "STUDENT";
        this.status = "ACTIVE";
        this.currentYear = 1;
        this.enrollments = new ArrayList<>();
        this.borrowRecords = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.resultSlips = new ArrayList<>();
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public Student(String name, String email, String registrationNumber, int programmeId) {
        this();
        setName(name);
        setEmail(email);
        this.registrationNumber = registrationNumber;
        this.programmeId = programmeId;
    }
    
    /**
     * Constructor with all basic fields
     */
    public Student(String name, String email, String phone, String registrationNumber,
                  int programmeId, Date enrollmentDate, int currentYear, String status,
                  String studyMode, String gender, String nationality, String address) {
        this(name, email, registrationNumber, programmeId);
        setPhone(phone);
        this.enrollmentDate = enrollmentDate;
        this.currentYear = currentYear;
        this.status = status;
        this.studyMode = studyMode;
        this.gender = gender;
        this.nationality = nationality;
        this.address = address;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Student(int personId, int studentId, String name, String email, String phone,
                  String registrationNumber, int programmeId, Date enrollmentDate,
                  Date expectedGraduationDate, int currentYear, String status,
                  String studyMode, String gender, String nationality, String address,
                  String phoneNumber, String emergencyContact, String emergencyPhone,
                  String profilePicture, Timestamp lastLogin, Timestamp createdAt,
                  Timestamp updatedAt) {
        super(personId, name, email, phone, "STUDENT", createdAt, updatedAt);
        this.studentId = studentId;
        this.registrationNumber = registrationNumber;
        this.programmeId = programmeId;
        this.enrollmentDate = enrollmentDate;
        this.expectedGraduationDate = expectedGraduationDate;
        this.currentYear = currentYear;
        this.status = status;
        this.studyMode = studyMode;
        this.gender = gender;
        this.nationality = nationality;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.profilePicture = profilePicture;
        this.lastLogin = lastLogin;
        this.enrollments = new ArrayList<>();
        this.borrowRecords = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.resultSlips = new ArrayList<>();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
        super.setPersonId(studentId);  // Keep in sync with personId
        updateTimestamp();
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        updateTimestamp();
    }
    
    public int getProgrammeId() {
        return programmeId;
    }
    
    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
        updateTimestamp();
    }
    
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        updateTimestamp();
    }
    
    // Fixed method name (was setEntollmentId)
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        updateTimestamp();
    }
    
    public Date getExpectedGraduationDate() {
        return expectedGraduationDate;
    }
    
    public void setExpectedGraduationDate(Date expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
        updateTimestamp();
    }
    
    public int getCurrentYear() {
        return currentYear;
    }
    
    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
        updateTimestamp();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        updateTimestamp();
    }
    
    public String getStudyMode() {
        return studyMode;
    }
    
    public void setStudyMode(String studyMode) {
        this.studyMode = studyMode;
        updateTimestamp();
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
        updateTimestamp();
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
        updateTimestamp();
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
        updateTimestamp();
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateTimestamp();
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
        updateTimestamp();
    }
    
    public String getEmergencyPhone() {
        return emergencyPhone;
    }
    
    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
        updateTimestamp();
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        updateTimestamp();
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
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
    
    // Joined fields
    public String getProgrammeName() {
        return programmeName;
    }
    
    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getFacultyName() {
        return facultyName;
    }
    
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
    
    // Relationships
    public Programme getProgramme() {
        return programme;
    }
    
    public void setProgramme(Programme programme) {
        this.programme = programme;
        if (programme != null) {
            this.programmeId = programme.getProgrammeId();
            this.programmeName = programme.getProgrammeName();
        }
        updateTimestamp();
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
        updateTimestamp();
    }
    
    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }
    
    public void setBorrowRecords(List<BorrowRecord> borrowRecords) {
        this.borrowRecords = borrowRecords;
        updateTimestamp();
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        updateTimestamp();
    }
    
    public List<ResultSlip> getResultSlips() {
        return resultSlips;
    }
    
    public void setResultSlips(List<ResultSlip> resultSlips) {
        this.resultSlips = resultSlips;
        updateTimestamp();
    }
    
    // ============ INHERITED PERSON METHODS ============
    
    @Override
    public int getPersonId() {
        return super.getPersonId();
    }
    
    @Override
    public void setPersonId(int personId) {
        super.setPersonId(personId);
        if (this.studentId == 0) {
            this.studentId = personId;  // Sync if studentId not set
        }
    }
    
    @Override
    public int getTypeId() {
        return studentId;
    }
    
    @Override
    public String getIdentifier() {
        return registrationNumber;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Update the updatedAt timestamp
     */
    private void updateTimestamp() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Add an enrollment to this student
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
     * Remove an enrollment from this student
     */
    public boolean removeEnrollment(Enrollment enrollment) {
        if (this.enrollments != null) {
            updateTimestamp();
            return this.enrollments.remove(enrollment);
        }
        return false;
    }
    
    /**
     * Add a borrow record to this student
     */
    public void addBorrowRecord(BorrowRecord record) {
        if (this.borrowRecords == null) {
            this.borrowRecords = new ArrayList<>();
        }
        if (!this.borrowRecords.contains(record)) {
            this.borrowRecords.add(record);
            updateTimestamp();
        }
    }
    
    /**
     * Add a reservation to this student
     */
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        if (!this.reservations.contains(reservation)) {
            this.reservations.add(reservation);
            updateTimestamp();
        }
    }
    
    /**
     * Add a result slip to this student
     */
    public void addResultSlip(ResultSlip resultSlip) {
        if (this.resultSlips == null) {
            this.resultSlips = new ArrayList<>();
        }
        if (!this.resultSlips.contains(resultSlip)) {
            this.resultSlips.add(resultSlip);
            updateTimestamp();
        }
    }
    
    /**
     * Get current enrollments (active only)
     */
    public List<Enrollment> getCurrentEnrollments() {
        List<Enrollment> current = new ArrayList<>();
        if (enrollments != null) {
            for (Enrollment e : enrollments) {
                if ("ENROLLED".equals(e.getStatus())) {
                    current.add(e);
                }
            }
        }
        return current;
    }
    
    /**
     * Get current borrowings (not returned)
     */
    public List<BorrowRecord> getCurrentBorrowings() {
        List<BorrowRecord> current = new ArrayList<>();
        if (borrowRecords != null) {
            for (BorrowRecord br : borrowRecords) {
                if ("BORROWED".equals(br.getStatus()) || "OVERDUE".equals(br.getStatus())) {
                    current.add(br);
                }
            }
        }
        return current;
    }
    
    /**
     * Get active reservations
     */
    public List<Reservation> getActiveReservations() {
        List<Reservation> active = new ArrayList<>();
        if (reservations != null) {
            for (Reservation r : reservations) {
                if ("PENDING".equals(r.getStatus()) || "NOTIFIED".equals(r.getStatus())) {
                    active.add(r);
                }
            }
        }
        return active;
    }
    
    /**
     * Check if student is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    /**
     * Check if student has overdue books
     */
    public boolean hasOverdueBooks() {
        if (borrowRecords != null) {
            for (BorrowRecord br : borrowRecords) {
                if ("OVERDUE".equals(br.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get total fines owed
     */
    public double getTotalFines() {
        double total = 0.0;
        if (borrowRecords != null) {
            for (BorrowRecord br : borrowRecords) {
                if (br.hasPendingFine()) {
                    total += br.getFineAmount();
                }
            }
        }
        return total;
    }
    
    /**
     * Get enrollment count for current semester
     */
    public int getCurrentEnrollmentCount() {
        return getCurrentEnrollments().size();
    }
    
    /**
     * Check if student can enroll in more courses (max 5)
     */
    public boolean canEnrollInMoreCourses() {
        return getCurrentEnrollmentCount() < 5;
    }
    
    /**
     * Get available enrollment slots
     */
    public int getAvailableEnrollmentSlots() {
        return 5 - getCurrentEnrollmentCount();
    }
    
    /**
     * Calculate expected graduation year based on enrollment
     */
    public void calculateExpectedGraduation() {
        if (enrollmentDate != null && programme != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(enrollmentDate);
            cal.add(java.util.Calendar.YEAR, programme.getDurationYears());
            this.expectedGraduationDate = new Date(cal.getTimeInMillis());
            updateTimestamp();
        }
    }
    
    /**
     * Promote to next year
     */
    public void promoteToNextYear() {
        if (currentYear < (programme != null ? programme.getDurationYears() : 4)) {
            this.currentYear++;
            updateTimestamp();
        }
    }
    
    /**
     * Get full student name with registration number
     */
    public String getDisplayName() {
        return getName() + " (" + registrationNumber + ")";
    }
    
    /**
     * Get student summary
     */
    public String getSummary() {
        return String.format("%s - Year %d, %s - %d courses, %d books borrowed",
            getDisplayName(),
            currentYear,
            status,
            getCurrentEnrollmentCount(),
            getCurrentBorrowings().size()
        );
    }
    
    /**
     * Update last login timestamp
     */
    public void updateLastLogin() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        updateTimestamp();
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Student{" +
               "studentId=" + studentId +
               ", name='" + getName() + '\'' +
               ", regNumber='" + registrationNumber + '\'' +
               ", programmeId=" + programmeId +
               ", currentYear=" + currentYear +
               ", status='" + status + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Student{" +
               "studentId=" + studentId +
               ", personId=" + getPersonId() +
               ", name='" + getName() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", registrationNumber='" + registrationNumber + '\'' +
               ", programmeId=" + programmeId +
               ", programmeName='" + programmeName + '\'' +
               ", enrollmentDate=" + enrollmentDate +
               ", expectedGraduationDate=" + expectedGraduationDate +
               ", currentYear=" + currentYear +
               ", status='" + status + '\'' +
               ", studyMode='" + studyMode + '\'' +
               ", gender='" + gender + '\'' +
               ", nationality='" + nationality + '\'' +
               ", enrollmentCount=" + (enrollments != null ? enrollments.size() : 0) +
               ", borrowCount=" + (borrowRecords != null ? borrowRecords.size() : 0) +
               ", reservationCount=" + (reservations != null ? reservations.size() : 0) +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s - Year %d %s",
            getDisplayName(),
            currentYear,
            status
        );
    }
}