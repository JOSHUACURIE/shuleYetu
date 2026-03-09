public class Reservation {
    private int reservationId;
    private int studentId;
    private int bookId;
    private Timestamp reservationDate;
    private Date expiryDate;
    private String status;  // "PENDING", "NOTIFIED", "CANCELLED", "COMPLETED"
    private Timestamp notifiedDate;
    private Integer queuePosition;
    
    // Relationships
    private Student student;
    private Book book;
    
    // Business method
    public boolean isExpired() {
        return expiryDate != null && new Date().after(expiryDate);
    }
    
    // getters/setters
}