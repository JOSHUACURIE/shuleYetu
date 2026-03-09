package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Objects;

public class UserAccount {
    
    private int userId;
    private int personId;
    private String username;
    private String passwordHash;
    private Timestamp lastLogin;
    private int loginAttempts;
    private boolean isLocked;
    private Timestamp passwordChangedAt;
    private boolean isActive;
    private String securityQuestion;
    private String securityAnswerHash;
    private Timestamp lastPasswordResetRequest;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    private String twoFactorSecret;
    private boolean twoFactorEnabled;
    private String lastLoginIp;
    private String lastUserAgent;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Relationships
    private Person person;
    
    // Constants
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int LOCK_DURATION_MINUTES = 30;
    public static final int PASSWORD_EXPIRY_DAYS = 90;
    public static final int RESET_TOKEN_EXPIRY_HOURS = 24;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public UserAccount() {
        this.loginAttempts = 0;
        this.isLocked = false;
        this.isActive = true;
        this.twoFactorEnabled = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
        this.passwordChangedAt = this.createdAt;
    }
    
    /**
     * Constructor with required fields
     */
    public UserAccount(int personId, String username, String passwordHash) {
        this();
        this.personId = personId;
        this.username = username;
        this.passwordHash = passwordHash;
    }
    
    /**
     * Constructor with person object
     */
    public UserAccount(Person person, String username, String passwordHash) {
        this();
        this.person = person;
        this.personId = person != null ? person.getPersonId() : 0;
        this.username = username;
        this.passwordHash = passwordHash;
    }
    
    /**
     * Constructor with security question
     */
    public UserAccount(int personId, String username, String passwordHash,
                      String securityQuestion, String securityAnswerHash) {
        this(personId, username, passwordHash);
        this.securityQuestion = securityQuestion;
        this.securityAnswerHash = securityAnswerHash;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public UserAccount(int userId, int personId, String username, String passwordHash,
                      Timestamp lastLogin, int loginAttempts, boolean isLocked,
                      Timestamp passwordChangedAt, boolean isActive, String securityQuestion,
                      String securityAnswerHash, Timestamp lastPasswordResetRequest,
                      String resetToken, Timestamp resetTokenExpiry, String twoFactorSecret,
                      boolean twoFactorEnabled, String lastLoginIp, String lastUserAgent,
                      Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.personId = personId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.lastLogin = lastLogin;
        this.loginAttempts = loginAttempts;
        this.isLocked = isLocked;
        this.passwordChangedAt = passwordChangedAt;
        this.isActive = isActive;
        this.securityQuestion = securityQuestion;
        this.securityAnswerHash = securityAnswerHash;
        this.lastPasswordResetRequest = lastPasswordResetRequest;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.twoFactorSecret = twoFactorSecret;
        this.twoFactorEnabled = twoFactorEnabled;
        this.lastLoginIp = lastLoginIp;
        this.lastUserAgent = lastUserAgent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
        updateTimestamp();
    }
    
    public int getPersonId() {
        return personId;
    }
    
    public void setPersonId(int personId) {
        this.personId = personId;
        updateTimestamp();
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        updateTimestamp();
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        this.passwordChangedAt = new Timestamp(System.currentTimeMillis());
        updateTimestamp();
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
        updateTimestamp();
    }
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
        updateTimestamp();
    }
    
    public boolean isLocked() {
        return isLocked;
    }
    
    public void setLocked(boolean locked) {
        isLocked = locked;
        updateTimestamp();
    }
    
    public Timestamp getPasswordChangedAt() {
        return passwordChangedAt;
    }
    
    public void setPasswordChangedAt(Timestamp passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
        updateTimestamp();
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        updateTimestamp();
    }
    
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
        updateTimestamp();
    }
    
    public String getSecurityAnswerHash() {
        return securityAnswerHash;
    }
    
    public void setSecurityAnswerHash(String securityAnswerHash) {
        this.securityAnswerHash = securityAnswerHash;
        updateTimestamp();
    }
    
    public Timestamp getLastPasswordResetRequest() {
        return lastPasswordResetRequest;
    }
    
    public void setLastPasswordResetRequest(Timestamp lastPasswordResetRequest) {
        this.lastPasswordResetRequest = lastPasswordResetRequest;
        updateTimestamp();
    }
    
    public String getResetToken() {
        return resetToken;
    }
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
        updateTimestamp();
    }
    
    public Timestamp getResetTokenExpiry() {
        return resetTokenExpiry;
    }
    
    public void setResetTokenExpiry(Timestamp resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
        updateTimestamp();
    }
    
    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }
    
    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
        updateTimestamp();
    }
    
    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }
    
    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
        updateTimestamp();
    }
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        updateTimestamp();
    }
    
    public String getLastUserAgent() {
        return lastUserAgent;
    }
    
    public void setLastUserAgent(String lastUserAgent) {
        this.lastUserAgent = lastUserAgent;
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
    
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
        if (person != null) {
            this.personId = person.getPersonId();
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
     * Increment login attempts and lock account if threshold reached
     */
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            this.isLocked = true;
        }
        updateTimestamp();
    }
    
    /**
     * Reset login attempts on successful login
     */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.isLocked = false;
        updateTimestamp();
    }
    
    /**
     * Record successful login
     */
    public void recordSuccessfulLogin(String ipAddress, String userAgent) {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.lastLoginIp = ipAddress;
        this.lastUserAgent = userAgent;
        this.loginAttempts = 0;
        this.isLocked = false;
        updateTimestamp();
    }
    
    /**
     * Record failed login attempt
     */
    public void recordFailedLogin() {
        incrementLoginAttempts();
    }
    
    /**
     * Check if account is locked
     */
    public boolean isAccountLocked() {
        return isLocked;
    }
    
    /**
     * Check if account is active
     */
    public boolean isAccountActive() {
        return isActive && !isLocked;
    }
    
    /**
     * Unlock account
     */
    public void unlockAccount() {
        this.isLocked = false;
        this.loginAttempts = 0;
        updateTimestamp();
    }
    
    /**
     * Lock account
     */
    public void lockAccount() {
        this.isLocked = true;
        updateTimestamp();
    }
    
    /**
     * Check if password is expired (older than 90 days)
     */
    public boolean isPasswordExpired() {
        if (passwordChangedAt == null) return true;
        long now = System.currentTimeMillis();
        long passwordAge = now - passwordChangedAt.getTime();
        long expiryMillis = PASSWORD_EXPIRY_DAYS * 24L * 60 * 60 * 1000;
        return passwordAge > expiryMillis;
    }
    
    /**
     * Get days until password expires
     */
    public long getDaysUntilPasswordExpiry() {
        if (passwordChangedAt == null) return 0;
        long now = System.currentTimeMillis();
        long passwordAge = now - passwordChangedAt.getTime();
        long expiryMillis = PASSWORD_EXPIRY_DAYS * 24L * 60 * 60 * 1000;
        long remainingMillis = expiryMillis - passwordAge;
        return remainingMillis > 0 ? remainingMillis / (24 * 60 * 60 * 1000) : 0;
    }
    
    /**
     * Generate password reset token
     */
    public void generateResetToken(String token) {
        this.resetToken = token;
        this.lastPasswordResetRequest = new Timestamp(System.currentTimeMillis());
        
        // Token expires in 24 hours
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(this.lastPasswordResetRequest.getTime());
        cal.add(java.util.Calendar.HOUR, RESET_TOKEN_EXPIRY_HOURS);
        this.resetTokenExpiry = new Timestamp(cal.getTimeInMillis());
        
        updateTimestamp();
    }
    
    /**
     * Validate reset token
     */
    public boolean isValidResetToken(String token) {
        if (resetToken == null || resetTokenExpiry == null) return false;
        if (!resetToken.equals(token)) return false;
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return !now.after(resetTokenExpiry);
    }
    
    /**
     * Clear reset token after use
     */
    public void clearResetToken() {
        this.resetToken = null;
        this.resetTokenExpiry = null;
        updateTimestamp();
    }
    
    /**
     * Enable two-factor authentication
     */
    public void enableTwoFactor(String secret) {
        this.twoFactorSecret = secret;
        this.twoFactorEnabled = true;
        updateTimestamp();
    }
    
    /**
     * Disable two-factor authentication
     */
    public void disableTwoFactor() {
        this.twoFactorSecret = null;
        this.twoFactorEnabled = false;
        updateTimestamp();
    }
    
    /**
     * Verify security answer
     */
    public boolean verifySecurityAnswer(String answerHash) {
        if (securityAnswerHash == null) return false;
        return securityAnswerHash.equals(answerHash);
    }
    
    /**
     * Get person name
     */
    public String getPersonName() {
        return person != null ? person.getName() : "";
    }
    
    /**
     * Get person email
     */
    public String getPersonEmail() {
        return person != null ? person.getEmail() : "";
    }
    
    /**
     * Get person role
     */
    public String getPersonRole() {
        return person != null ? person.getRole() : "";
    }
    
    /**
     * Get account status display
     */
    public String getStatusDisplay() {
        if (!isActive) return "❌ Inactive";
        if (isLocked) return "🔒 Locked";
        return "✅ Active";
    }
    
    /**
     * Get formatted last login
     */
    public String getFormattedLastLogin() {
        if (lastLogin == null) return "Never";
        return lastLogin.toString();
    }
    
    /**
     * Check if user is online (last login within last 15 minutes)
     */
    public boolean isOnline() {
        if (lastLogin == null) return false;
        long now = System.currentTimeMillis();
        long fifteenMinutes = 15 * 60 * 1000;
        return (now - lastLogin.getTime()) < fifteenMinutes;
    }
    
    /**
     * Get account summary
     */
    public String getSummary() {
        return String.format("%s (%s) - %s, Last login: %s",
            username,
            getPersonName(),
            getStatusDisplay(),
            getFormattedLastLogin()
        );
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userId == that.userId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "UserAccount{" +
               "userId=" + userId +
               ", username='" + username + '\'' +
               ", personId=" + personId +
               ", isLocked=" + isLocked +
               ", isActive=" + isActive +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "UserAccount{" +
               "userId=" + userId +
               ", personId=" + personId +
               ", username='" + username + '\'' +
               ", lastLogin=" + lastLogin +
               ", loginAttempts=" + loginAttempts +
               ", isLocked=" + isLocked +
               ", isActive=" + isActive +
               ", passwordChangedAt=" + passwordChangedAt +
               ", twoFactorEnabled=" + twoFactorEnabled +
               ", lastLoginIp='" + lastLoginIp + '\'' +
               ", lastUserAgent='" + lastUserAgent + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", personName='" + getPersonName() + '\'' +
               ", personRole='" + getPersonRole() + '\'' +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s (%s) [%s]",
            username,
            getPersonName(),
            getStatusDisplay()
        );
    }
}