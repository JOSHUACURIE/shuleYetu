package com.shuleyetu.model;
import java.sql.Timestamp;
public abstract class Person {
    private int personId;
    private String name;
    private String email;
    private String phone;
    private String role;  // "STUDENT", "LECTURER", "ADMIN"
    private Timestamp createdAt;
    
    // Constructors
    public Person() {}
    
    public Person(int personId, String name, String email) {
        this.personId = personId;
        this.name = name;
        this.email = email;
    }
    
    // Getters and Setters for ALL fields
    public int getPersonId() { return personId; }
    public void setPersonId(int personId) { this.personId = personId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Person{" +
               "personId=" + personId +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               '}';
    }
}