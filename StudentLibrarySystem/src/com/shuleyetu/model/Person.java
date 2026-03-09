package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Objects;

public abstract class Person {
    
    private int personId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;  // "STUDENT", "LECTURER", "ADMIN"
    private String gender;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Person() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public Person(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }
    
    /**
     * Constructor with ID (for database reads)
     */
    public Person(int personId, String name, String email) {
        this(name, email);
        this.personId = personId;
    }
    
    /**
     * Constructor with all basic fields
     */
    public Person(int personId, String name, String email, String phone, 
                  String address, String role, String gender) {
        this(personId, name, email);
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.gender = gender;
    }
    
    /**
     * Full constructor with timestamps
     */
    public Person(int personId, String name, String email, String phone, 
                  String address, String role, String gender, 
                  Timestamp createdAt, Timestamp updatedAt) {
        this.personId = personId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.gender = gender;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getPersonId() {
        return personId;
    }
    
    public void setPersonId(int personId) {
        this.personId = personId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
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
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Update the updatedAt timestamp
     */
    public void updateTimestamp() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Check if the person has a phone number
     */
    public boolean hasPhone() {
        return phone != null && !phone.trim().isEmpty();
    }
    
    /**
     * Check if the person has an address
     */
    public boolean hasAddress() {
        return address != null && !address.trim().isEmpty();
    }
    
    /**
     * Get display name with role
     */
    public String getDisplayName() {
        StringBuilder display = new StringBuilder();
        if (role != null) {
            switch (role) {
                case "STUDENT":
                    display.append("🎓 ");
                    break;
                case "LECTURER":
                    display.append("👨‍🏫 ");
                    break;
                case "ADMIN":
                    display.append("⚙️ ");
                    break;
            }
        }
        display.append(name != null ? name : "Unknown");
        return display.toString();
    }
    
    /**
     * Get initials (e.g., "JD" for John Doe)
     */
    public String getInitials() {
        if (name == null || name.isEmpty()) return "";
        
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        } else {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
    }
    
    /**
     * Get first name only
     */
    public String getFirstName() {
        if (name == null || name.isEmpty()) return "";
        return name.trim().split("\\s+")[0];
    }
    
    /**
     * Get last name only
     */
    public String getLastName() {
        if (name == null || name.isEmpty()) return "";
        String[] parts = name.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
    
    /**
     * Validate email format (basic check)
     */
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    /**
     * Validate phone format (basic check)
     */
    public boolean isValidPhone() {
        return phone == null || phone.matches("^[+]?[0-9]{10,15}$");
    }
    
    /**
     * Check if this is a student
     */
    public boolean isStudent() {
        return "STUDENT".equals(role);
    }
    
    /**
     * Check if this is a lecturer
     */
    public boolean isLecturer() {
        return "LECTURER".equals(role);
    }
    
    /**
     * Check if this is an admin
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    /**
     * Get role display name
     */
    public String getRoleDisplay() {
        if (role == null) return "";
        switch (role) {
            case "STUDENT": return "Student";
            case "LECTURER": return "Lecturer";
            case "ADMIN": return "Administrator";
            default: return role;
        }
    }
    
    /**
     * Get contact card string
     */
    public String getContactCard() {
        StringBuilder card = new StringBuilder();
        card.append("Name: ").append(name).append("\n");
        card.append("Email: ").append(email).append("\n");
        if (phone != null) card.append("Phone: ").append(phone).append("\n");
        if (address != null) card.append("Address: ").append(address);
        return card.toString();
    }
    
    // ============ ABSTRACT METHODS ============
    
    /**
     * Get the specific ID for the person type
     * Each subclass must implement this
     */
    public abstract int getTypeId();
    
    /**
     * Get the identifier (reg number, staff number, employee number)
     */
    public abstract String getIdentifier();
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId == person.personId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Person{" +
               "personId=" + personId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Person{" +
               "personId=" + personId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", address='" + address + '\'' +
               ", role='" + role + '\'' +
               ", gender='" + gender + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}