public class Book {
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private String version;
    private Integer yearPublished;
    private String category;
    private String shelfLocation;
    private int totalCopies;
    private int availableCopies;
    private Timestamp addedDate;
    
    // Relationships
    private List<BorrowRecord> borrowRecords;
    private List<Reservation> reservations;
    
    // Business method
    public boolean isAvailable() {
        return availableCopies > 0;
    }
    
    // getters/setters
}