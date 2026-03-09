package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Book() {
        this.borrowRecords = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }
    
    /**
     * Constructor with required fields
     */
    public Book(String title, String author, String isbn, int totalCopies) {
        this();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    /**
     * Constructor with all basic fields (no relationships)
     */
    public Book(String isbn, String title, String author, String publisher, 
                String edition, String version, Integer yearPublished, 
                String category, String shelfLocation, int totalCopies) {
        this();
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.version = version;
        this.yearPublished = yearPublished;
        this.category = category;
        this.shelfLocation = shelfLocation;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Book(int bookId, String isbn, String title, String author, 
                String publisher, String edition, String version, 
                Integer yearPublished, String category, String shelfLocation, 
                int totalCopies, int availableCopies, Timestamp addedDate) {
        this();
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.version = version;
        this.yearPublished = yearPublished;
        this.category = category;
        this.shelfLocation = shelfLocation;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.addedDate = addedDate;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getEdition() {
        return edition;
    }
    
    public void setEdition(String edition) {
        this.edition = edition;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Integer getYearPublished() {
        return yearPublished;
    }
    
    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getShelfLocation() {
        return shelfLocation;
    }
    
    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }
    
    public int getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public int getAvailableCopies() {
        return availableCopies;
    }
    
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public Timestamp getAddedDate() {
        return addedDate;
    }
    
    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }
    
    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }
    
    public void setBorrowRecords(List<BorrowRecord> borrowRecords) {
        this.borrowRecords = borrowRecords;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Check if book is available for borrowing
     */
    public boolean isAvailable() {
        return availableCopies > 0;
    }
    
    /**
     * Get number of copies currently borrowed
     */
    public int getBorrowedCopies() {
        return totalCopies - availableCopies;
    }
    
    /**
     * Check if book is fully borrowed (no copies available)
     */
    public boolean isFullyBorrowed() {
        return availableCopies == 0;
    }
    
    /**
     * Get borrow percentage
     */
    public double getBorrowPercentage() {
        if (totalCopies == 0) return 0;
        return ((double) (totalCopies - availableCopies) / totalCopies) * 100;
    }
    
    /**
     * Add a borrow record to this book
     */
    public void addBorrowRecord(BorrowRecord record) {
        if (this.borrowRecords == null) {
            this.borrowRecords = new ArrayList<>();
        }
        this.borrowRecords.add(record);
    }
    
    /**
     * Add a reservation to this book
     */
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
    }
    
    /**
     * Get active reservations count
     */
    public long getActiveReservationCount() {
        if (reservations == null) return 0;
        return reservations.stream()
                .filter(r -> "PENDING".equals(r.getStatus()))
                .count();
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Book{" +
               "bookId=" + bookId +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", isbn='" + isbn + '\'' +
               ", available=" + availableCopies + "/" + totalCopies +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Book{" +
               "bookId=" + bookId +
               ", isbn='" + isbn + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", publisher='" + publisher + '\'' +
               ", edition='" + edition + '\'' +
               ", version='" + version + '\'' +
               ", yearPublished=" + yearPublished +
               ", category='" + category + '\'' +
               ", shelfLocation='" + shelfLocation + '\'' +
               ", totalCopies=" + totalCopies +
               ", availableCopies=" + availableCopies +
               ", addedDate=" + addedDate +
               ", borrowCount=" + (borrowRecords != null ? borrowRecords.size() : 0) +
               ", reservationCount=" + (reservations != null ? reservations.size() : 0) +
               '}';
    }
}