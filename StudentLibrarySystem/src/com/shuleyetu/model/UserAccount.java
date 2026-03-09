public class UserAccount {
    private int userId;
    private int personId;
    private String username;
    private String passwordHash;
    private Timestamp lastLogin;
    private int loginAttempts;
    private boolean isLocked;
    private Timestamp passwordChangedAt;
    
    // Relationships
    private Person person;
    
    // Business method
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (loginAttempts >= 5) {
            this.isLocked = true;
        }
    }
    
    // getters/setters
}