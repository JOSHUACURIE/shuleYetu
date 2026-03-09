package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BorrowRecord {
    private int borrowId;
    private int studentId;
    private int bookId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private boolean finePaid;
    private String status;  // "BORROWED", "RETURNED", "OVERDUE", "LOST"
    private String remarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Student student;
    private Book book;
    
    // Constants
    public static final double FINE_PER_DAY = 10.0;  // KSh 10 per day
    public static final int DEFAULT_BORROW_DAYS = 14;  // 2 weeks
    public static final int MAX_BORROW_DAYS = 30;  // maximum allowed
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public BorrowRecord() {
        this.status = "BORROWED";
        this.fineAmount = 0.0;
        this.finePaid = false;
    }
    
    /**
     * Constructor with required fields
     */
    public BorrowRecord(int studentId, int bookId, Date borrowDate, Date dueDate) {
        this();
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    /**
     * Constructor with student and book objects
     */
    public BorrowRecord(Student student, Book book, Date borrowDate, Date dueDate) {
        this();
        this.student = student;
        this.book = book;
        this.studentId = student != null ? student.getStudentId() : 0;
        this.bookId = book != null ? book.getBookId() : 0;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    /**
     * Constructor for creating a new borrow (auto-calculates due date)
     */
    public BorrowRecord(Student student, Book book, int borrowDays) {
        this();
        this.student = student;
        this.book = book;
        this.studentId = student != null ? student.getStudentId() : 0;
        this.bookId = book != null ? book.getBookId() : 0;
        this.borrowDate = new Date();
        
        // Calculate due date
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(this.borrowDate);
        cal.add(java.util.Calendar.DAY_OF_MONTH, borrowDays);
        this.dueDate = cal.getTime();
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public BorrowRecord(int borrowId, int studentId, int bookId, Date borrowDate, 
                       Date dueDate, Date returnDate, double fineAmount, 
                       boolean finePaid, String status, String remarks,
                       Timestamp createdAt, Timestamp updatedAt) {
        this();
        this.borrowId = borrowId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.finePaid = finePaid;
        this.status = status;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getBorrowId() {
        return borrowId;
    }
    
    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public Date getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public boolean isFinePaid() {
        return finePaid;
    }
    
    public void setFinePaid(boolean finePaid) {
        this.finePaid = finePaid;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getBookId();
        }
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Check if the book is overdue
     */
    public boolean isOverdue() {
        Date today = new Date();
        return ("BORROWED".equals(status) || "OVERDUE".equals(status)) && 
               dueDate != null && today.after(dueDate);
    }
    
    /**
     * Update overdue status based on current date
     */
    public void updateOverdueStatus() {
        if (isOverdue() && !"RETURNED".equals(status) && !"LOST".equals(status)) {
            this.status = "OVERDUE";
        }
    }
    
    /**
     * Calculate fine based on days overdue
     */
    public double calculateFine() {
        if (returnDate != null && dueDate != null) {
            if (returnDate.after(dueDate)) {
                long diffInMillies = returnDate.getTime() - dueDate.getTime();
                long daysOverdue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                return daysOverdue * FINE_PER_DAY;
            }
        } else if (isOverdue() && dueDate != null) {
            // Not returned yet, calculate up to today
            long diffInMillies = new Date().getTime() - dueDate.getTime();
            long daysOverdue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return daysOverdue * FINE_PER_DAY;
        }
        return 0;
    }
    
    /**
     * Update fine amount (call this regularly)
     */
    public void updateFine() {
        this.fineAmount = calculateFine();
    }
    
    /**
     * Mark as returned
     */
    public void markAsReturned() {
        this.returnDate = new Date();
        this.status = "RETURNED";
        updateFine();  // calculate final fine
    }
    
    /**
     * Mark as lost
     */
    public void markAsLost() {
        this.status = "LOST";
        // Lost books might have additional fine
        if (book != null) {
            this.fineAmount += book.getTotalCopies() * 500;  // example: KSh 500 lost book fine
        }
    }
    
    /**
     * Pay the fine
     */
    public void payFine() {
        this.finePaid = true;
    }
    
    /**
     * Check if fine is pending
     */
    public boolean hasPendingFine() {
        return fineAmount > 0 && !finePaid;
    }
    
    /**
     * Get days overdue
     */
    public long getDaysOverdue() {
        Date compareDate = returnDate != null ? returnDate : new Date();
        if (dueDate != null && compareDate.after(dueDate)) {
            long diffInMillies = compareDate.getTime() - dueDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }
        return 0;
    }
    
    /**
     * Get days borrowed
     */
    public long getDaysBorrowed() {
        if (borrowDate == null) return 0;
        Date endDate = returnDate != null ? returnDate : new Date();
        long diffInMillies = endDate.getTime() - borrowDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Check if borrow period is valid
     */
    public boolean isValidBorrowPeriod() {
        if (borrowDate == null || dueDate == null) return false;
        return !dueDate.before(borrowDate);
    }
    
    /**
     * Extend due date
     */
    public boolean extendDueDate(int extraDays) {
        if ("BORROWED".equals(status) && !isOverdue()) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(dueDate);
            cal.add(java.util.Calendar.DAY_OF_MONTH, extraDays);
            
            // Check if extension exceeds max days from borrow date
            long totalDays = (cal.getTime().getTime() - borrowDate.getTime()) / (1000 * 60 * 60 * 24);
            if (totalDays <= MAX_BORROW_DAYS) {
                this.dueDate = cal.getTime();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if renewal is allowed
     */
    public boolean canRenew() {
        return "BORROWED".equals(status) && !isOverdue() && 
               getDaysBorrowed() < MAX_BORROW_DAYS;
    }
    
    /**
     * Get status display with color indicator
     */
    public String getStatusDisplay() {
        switch (status) {
            case "BORROWED":
                return "🟢 Borrowed";
            case "RETURNED":
                return "✅ Returned";
            case "OVERDUE":
                return "🔴 Overdue";
            case "LOST":
                return "⚠️ Lost";
            default:
                return status;
        }
    }
    
    /**
     * Get fine status display
     */
    public String getFineStatusDisplay() {
        if (fineAmount == 0) return "No fine";
        if (finePaid) return "Paid: KSh " + fineAmount;
        return "Pending: KSh " + fineAmount;
    }
    
    /**
     * Get formatted date range
     */
    public String getDateRangeString() {
        if (borrowDate == null || dueDate == null) return "";
        return String.format("%s to %s", 
            borrowDate.toString(), dueDate.toString());
    }
    
    /**
     * Get student name (if student object is set)
     */
    public String getStudentName() {
        return student != null ? student.getName() : "";
    }
    
    /**
     * Get book title (if book object is set)
     */
    public String getBookTitle() {
        return book != null ? book.getTitle() : "";
    }
    
    /**
     * Get registration number (if student object is set)
     */
    public String getStudentRegNumber() {
        return student != null ? student.getRegistrationNumber() : "";
    }
    
    /**
     * Get book ISBN (if book object is set)
     */
    public String getBookIsbn() {
        return book != null ? book.getIsbn() : "";
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecord that = (BorrowRecord) o;
        return borrowId == that.borrowId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(borrowId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "BorrowRecord{" +
               "borrowId=" + borrowId +
               ", studentId=" + studentId +
               ", bookId=" + bookId +
               ", borrowDate=" + borrowDate +
               ", dueDate=" + dueDate +
               ", returnDate=" + returnDate +
               ", status='" + status + '\'' +
               ", fine=" + fineAmount +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "BorrowRecord{" +
               "borrowId=" + borrowId +
               ", studentId=" + studentId +
               ", bookId=" + bookId +
               ", borrowDate=" + borrowDate +
               ", dueDate=" + dueDate +
               ", returnDate=" + returnDate +
               ", fineAmount=" + fineAmount +
               ", finePaid=" + finePaid +
               ", status='" + status + '\'' +
               ", remarks='" + remarks + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", studentName='" + getStudentName() + '\'' +
               ", bookTitle='" + getBookTitle() + '\'' +
               ", daysBorrowed=" + getDaysBorrowed() +
               ", daysOverdue=" + getDaysOverdue() +
               '}';
    }
    
    /**
     * Summary string for UI display
     */
    public String toSummaryString() {
        return String.format("%s borrowed '%s' on %s - %s",
            getStudentName(),
            getBookTitle(),
            borrowDate != null ? borrowDate.toString() : "unknown",
            getStatusDisplay()
        );
    }
    
    /**
     * Receipt string for printing
     */
    public String toReceiptString() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=".repeat(50)).append("\n");
        receipt.append("LIBRARY BORROWING RECEIPT\n");
        receipt.append("=".repeat(50)).append("\n");
        receipt.append("Borrow ID: ").append(borrowId).append("\n");
        receipt.append("Student: ").append(getStudentName()).append(" (").append(getStudentRegNumber()).append(")\n");
        receipt.append("Book: ").append(getBookTitle()).append("\n");
        receipt.append("ISBN: ").append(getBookIsbn()).append("\n");
        receipt.append("Borrow Date: ").append(borrowDate).append("\n");
        receipt.append("Due Date: ").append(dueDate).append("\n");
        if (returnDate != null) {
            receipt.append("Return Date: ").append(returnDate).append("\n");
        }
        receipt.append("Status: ").append(getStatusDisplay()).append("\n");
        if (fineAmount > 0) {
            receipt.append("Fine: KSh ").append(fineAmount);
            if (finePaid) receipt.append(" (Paid)");
            receipt.append("\n");
        }
        receipt.append("=".repeat(50));
        return receipt.toString();
    }
}