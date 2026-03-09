package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Reservation {
    
    private int reservationId;
    private int studentId;
    private int bookId;
    private Timestamp reservationDate;
    private Date expiryDate;
    private String status;  // "PENDING", "NOTIFIED", "CANCELLED", "COMPLETED", "EXPIRED"
    private Timestamp notifiedDate;
    private Integer queuePosition;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Student student;
    private Book book;
    
    // Constants
    public static final int DEFAULT_EXPIRY_DAYS = 3;  // Days after notification to collect
    public static final int MAX_QUEUE_POSITION = 999;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Reservation() {
        this.reservationDate = new Timestamp(System.currentTimeMillis());
        this.status = "PENDING";
        this.createdAt = this.reservationDate;
        this.updatedAt = this.createdAt;
        calculateExpiryDate();  // Default 7 days from reservation
    }
    
    /**
     * Constructor with required fields
     */
    public Reservation(int studentId, int bookId) {
        this();
        this.studentId = studentId;
        this.bookId = bookId;
    }
    
    /**
     * Constructor with student and book objects
     */
    public Reservation(Student student, Book book) {
        this();
        this.student = student;
        this.book = book;
        this.studentId = student != null ? student.getStudentId() : 0;
        this.bookId = book != null ? book.getBookId() : 0;
    }
    
    /**
     * Constructor with queue position
     */
    public Reservation(int studentId, int bookId, int queuePosition) {
        this(studentId, bookId);
        this.queuePosition = queuePosition;
    }
    
    /**
     * Constructor with all basic fields
     */
    public Reservation(int studentId, int bookId, Timestamp reservationDate, 
                      Date expiryDate, String status, Integer queuePosition, String notes) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.reservationDate = reservationDate != null ? reservationDate : new Timestamp(System.currentTimeMillis());
        this.expiryDate = expiryDate;
        this.status = status != null ? status : "PENDING";
        this.queuePosition = queuePosition;
        this.notes = notes;
        this.createdAt = this.reservationDate;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Reservation(int reservationId, int studentId, int bookId, 
                      Timestamp reservationDate, Date expiryDate, String status,
                      Timestamp notifiedDate, Integer queuePosition, String notes,
                      Timestamp createdAt, Timestamp updatedAt) {
        this.reservationId = reservationId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.notifiedDate = notifiedDate;
        this.queuePosition = queuePosition;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
        updateTimestamp();
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
        updateTimestamp();
    }
    
    public Timestamp getReservationDate() {
        return reservationDate;
    }
    
    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
        updateTimestamp();
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
        updateTimestamp();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        updateTimestamp();
    }
    
    public Timestamp getNotifiedDate() {
        return notifiedDate;
    }
    
    public void setNotifiedDate(Timestamp notifiedDate) {
        this.notifiedDate = notifiedDate;
        updateTimestamp();
    }
    
    public Integer getQueuePosition() {
        return queuePosition;
    }
    
    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
        updateTimestamp();
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getBookId();
        }
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
     * Calculate default expiry date (7 days from reservation)
     */
    private void calculateExpiryDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        this.expiryDate = new Date(cal.getTimeInMillis());
    }
    
    /**
     * Check if reservation is expired
     */
    public boolean isExpired() {
        if (expiryDate == null) return false;
        boolean expired = new Date().after(expiryDate);
        if (expired && "PENDING".equals(status)) {
            // Auto-update status if we're checking and it's expired
            this.status = "EXPIRED";
        }
        return expired;
    }
    
    /**
     * Check if reservation is pending
     */
    public boolean isPending() {
        return "PENDING".equals(status) && !isExpired();
    }
    
    /**
     * Check if reservation is notified (ready for collection)
     */
    public boolean isNotified() {
        return "NOTIFIED".equals(status);
    }
    
    /**
     * Check if reservation is cancelled
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Check if reservation is completed (book borrowed)
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Check if reservation is expired
     */
    public boolean isExpiredStatus() {
        return "EXPIRED".equals(status) || isExpired();
    }
    
    /**
     * Mark as notified (ready for collection)
     */
    public void markAsNotified() {
        this.status = "NOTIFIED";
        this.notifiedDate = new Timestamp(System.currentTimeMillis());
        
        // Set expiry date for collection (e.g., 3 days to collect)
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DAY_OF_MONTH, DEFAULT_EXPIRY_DAYS);
        this.expiryDate = new Date(cal.getTimeInMillis());
    }
    
    /**
     * Mark as completed (book borrowed)
     */
    public void markAsCompleted() {
        this.status = "COMPLETED";
    }
    
    /**
     * Mark as cancelled
     */
    public void markAsCancelled() {
        this.status = "CANCELLED";
    }
    
    /**
     * Mark as expired
     */
    public void markAsExpired() {
        this.status = "EXPIRED";
    }
    
    /**
     * Get days until expiry
     */
    public long getDaysUntilExpiry() {
        if (expiryDate == null) return 0;
        Date now = new Date();
        if (now.after(expiryDate)) return 0;
        long diffInMillies = expiryDate.getTime() - now.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Get days since reservation
     */
    public long getDaysSinceReservation() {
        if (reservationDate == null) return 0;
        long diffInMillies = System.currentTimeMillis() - reservationDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Move up in queue (decrease position number)
     */
    public void moveUpInQueue() {
        if (queuePosition != null && queuePosition > 1) {
            this.queuePosition--;
            updateTimestamp();
        }
    }
    
    /**
     * Move down in queue (increase position number)
     */
    public void moveDownInQueue() {
        if (queuePosition != null && queuePosition < MAX_QUEUE_POSITION) {
            this.queuePosition++;
            updateTimestamp();
        }
    }
    
    /**
     * Check if this reservation is next in line
     */
    public boolean isNextInQueue() {
        return queuePosition != null && queuePosition == 1;
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
     * Get book title
     */
    public String getBookTitle() {
        return book != null ? book.getTitle() : "";
    }
    
    /**
     * Get book ISBN
     */
    public String getBookIsbn() {
        return book != null ? book.getIsbn() : "";
    }
    
    /**
     * Get status display with emoji
     */
    public String getStatusDisplay() {
        switch (status) {
            case "PENDING":
                return "⏳ Pending (Queue: " + queuePosition + ")";
            case "NOTIFIED":
                return "📢 Ready for Collection";
            case "CANCELLED":
                return "❌ Cancelled";
            case "COMPLETED":
                return "✅ Completed";
            case "EXPIRED":
                return "⌛ Expired";
            default:
                return status;
        }
    }
    
    /**
     * Get simple status display without queue
     */
    public String getSimpleStatusDisplay() {
        switch (status) {
            case "PENDING":
                return "⏳ Pending";
            case "NOTIFIED":
                return "📢 Ready";
            case "CANCELLED":
                return "❌ Cancelled";
            case "COMPLETED":
                return "✅ Completed";
            case "EXPIRED":
                return "⌛ Expired";
            default:
                return status;
        }
    }
    
    /**
     * Get formatted reservation info
     */
    public String getReservationInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Reservation #").append(reservationId).append("\n");
        info.append("Student: ").append(getStudentName()).append(" (").append(getStudentRegNumber()).append(")\n");
        info.append("Book: ").append(getBookTitle()).append("\n");
        info.append("Status: ").append(getStatusDisplay()).append("\n");
        info.append("Reserved: ").append(reservationDate).append("\n");
        if (notifiedDate != null) {
            info.append("Notified: ").append(notifiedDate).append("\n");
        }
        if (expiryDate != null) {
            info.append("Expires: ").append(expiryDate).append("\n");
        }
        return info.toString();
    }
    
    /**
     * Check if reservation can be cancelled
     */
    public boolean canBeCancelled() {
        return "PENDING".equals(status) || "NOTIFIED".equals(status);
    }
    
    /**
     * Check if reservation can be notified (ready for collection)
     */
    public boolean canBeNotified() {
        return "PENDING".equals(status) && !isExpired();
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationId == that.reservationId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Reservation{" +
               "reservationId=" + reservationId +
               ", studentId=" + studentId +
               ", bookId=" + bookId +
               ", status='" + status + '\'' +
               ", queuePosition=" + queuePosition +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Reservation{" +
               "reservationId=" + reservationId +
               ", studentId=" + studentId +
               ", bookId=" + bookId +
               ", reservationDate=" + reservationDate +
               ", expiryDate=" + expiryDate +
               ", status='" + status + '\'' +
               ", notifiedDate=" + notifiedDate +
               ", queuePosition=" + queuePosition +
               ", notes='" + notes + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentName='" + getStudentName() + '\'' +
               ", bookTitle='" + getBookTitle() + '\'' +
               ", daysUntilExpiry=" + getDaysUntilExpiry() +
               '}';
    }
    
    /**
     * Summary string for UI lists
     */
    public String toSummaryString() {
        return String.format("%s - %s [%s]",
            getStudentName(),
            getBookTitle(),
            getSimpleStatusDisplay()
        );
    }
    
    /**
     * Queue display string
     */
    public String toQueueString() {
        return String.format("#%d: %s - %s (%s)",
            queuePosition != null ? queuePosition : 0,
            getStudentName(),
            getBookTitle(),
            getSimpleStatusDisplay()
        );
    }
}