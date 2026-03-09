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
    
    // Relationships
    private Student student;
    private Book book;
    
    // Business method
    public boolean isOverdue() {
        return status.equals("BORROWED") && new Date().after(dueDate);
    }
    
    public double calculateFine() {
        if (returnDate != null && returnDate.after(dueDate)) {
            long daysOverdue = (returnDate.getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
            return daysOverdue * 10;  // KSh 10 per day
        }
        return 0;
    }
    
    // getters/setters
}