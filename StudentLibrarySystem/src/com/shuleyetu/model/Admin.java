package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Admin extends Person {
    private int adminId;
    private String employeeNumber;
    private String jobTitle;
    private String department;  // e.g., "IT Department", "Registry", "Library"
    private String officeLocation;
    private String responsibility;  // e.g., "System Admin", "Registrar", "Finance Officer"
    private String accessLevel;  // "SUPER_ADMIN", "ADMIN", "MODERATOR"
    private Timestamp lastActive;
    private String createdBy;  // username or ID of who created this admin
    private String notes;
    
    // Relationships
    private List<AuditLog> auditLogs;  // actions performed by this admin
    private List<ResultSlip> publishedResults;  // results published by this admin
    private List<String> permissions;  // list of permissions granted
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Admin() {
        super();
        this.role = "ADMIN";
        this.auditLogs = new ArrayList<>();
        this.publishedResults = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.accessLevel = "ADMIN";  // default access level
    }
    
    /**
     * Constructor with required fields
     */
    public Admin(String name, String email, String employeeNumber) {
        super(name, email);
        this.employeeNumber = employeeNumber;
        this.role = "ADMIN";
        this.auditLogs = new ArrayList<>();
        this.publishedResults = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.accessLevel = "ADMIN";
    }
    
    /**
     * Constructor with all basic fields
     */
    public Admin(String name, String email, String phone, String employeeNumber,
                String jobTitle, String department, String officeLocation,
                String responsibility, String accessLevel) {
        super(name, email, phone, "ADMIN");
        this.employeeNumber = employeeNumber;
        this.jobTitle = jobTitle;
        this.department = department;
        this.officeLocation = officeLocation;
        this.responsibility = responsibility;
        this.accessLevel = accessLevel;
        this.auditLogs = new ArrayList<>();
        this.publishedResults = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public Admin(int personId, int adminId, String name, String email, String phone,
                String employeeNumber, String jobTitle, String department,
                String officeLocation, String responsibility, String accessLevel,
                Timestamp lastActive, String createdBy, String notes,
                Timestamp createdAt, Timestamp updatedAt) {
        super(personId, name, email, phone, "ADMIN", createdAt, updatedAt);
        this.adminId = adminId;
        this.employeeNumber = employeeNumber;
        this.jobTitle = jobTitle;
        this.department = department;
        this.officeLocation = officeLocation;
        this.responsibility = responsibility;
        this.accessLevel = accessLevel;
        this.lastActive = lastActive;
        this.createdBy = createdBy;
        this.notes = notes;
        this.auditLogs = new ArrayList<>();
        this.publishedResults = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getAdminId() {
        return adminId;
    }
    
    public void setAdminId(int adminId) {
        this.adminId = adminId;
        super.setPersonId(adminId);  // keep in sync with personId
    }
    
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getOfficeLocation() {
        return officeLocation;
    }
    
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
    
    public String getResponsibility() {
        return responsibility;
    }
    
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }
    
    public String getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    public Timestamp getLastActive() {
        return lastActive;
    }
    
    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }
    
    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
    
    public List<ResultSlip> getPublishedResults() {
        return publishedResults;
    }
    
    public void setPublishedResults(List<ResultSlip> publishedResults) {
        this.publishedResults = publishedResults;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    // ============ INHERITED PERSON METHODS ============
    
    @Override
    public int getPersonId() {
        return super.getPersonId();
    }
    
    @Override
    public void setPersonId(int personId) {
        super.setPersonId(personId);
        if (this.adminId == 0) {
            this.adminId = personId;  // sync if adminId not set
        }
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Check if admin is super admin
     */
    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(accessLevel);
    }
    
    /**
     * Check if admin has specific permission
     */
    public boolean hasPermission(String permission) {
        if (isSuperAdmin()) return true;  // super admin has all permissions
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * Add a permission
     */
    public void addPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<>();
        }
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }
    
    /**
     * Remove a permission
     */
    public boolean removePermission(String permission) {
        if (this.permissions != null) {
            return this.permissions.remove(permission);
        }
        return false;
    }
    
    /**
     * Add an audit log entry
     */
    public void addAuditLog(AuditLog log) {
        if (this.auditLogs == null) {
            this.auditLogs = new ArrayList<>();
        }
        this.auditLogs.add(log);
    }
    
    /**
     * Add a published result slip
     */
    public void addPublishedResult(ResultSlip resultSlip) {
        if (this.publishedResults == null) {
            this.publishedResults = new ArrayList<>();
        }
        this.publishedResults.add(resultSlip);
    }
    
    /**
     * Get number of actions performed
     */
    public int getActionCount() {
        return auditLogs != null ? auditLogs.size() : 0;
    }
    
    /**
     * Get number of results published
     */
    public int getPublishedResultsCount() {
        return publishedResults != null ? publishedResults.size() : 0;
    }
    
    /**
     * Update last active timestamp
     */
    public void updateLastActive() {
        this.lastActive = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Get full name with title
     */
    public String getFullNameWithTitle() {
        if (jobTitle != null && !jobTitle.isEmpty()) {
            return jobTitle + " " + getName();
        }
        return getName();
    }
    
    /**
     * Get admin info for display
     */
    public String getAdminInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getFullNameWithTitle());
        info.append(" (Emp: ").append(employeeNumber).append(")");
        if (department != null) {
            info.append(" - ").append(department);
        }
        return info.toString();
    }
    
    /**
     * Get default permissions based on access level
     */
    public List<String> getDefaultPermissions() {
        List<String> defaultPerms = new ArrayList<>();
        
        // All admins can do these
        defaultPerms.add("VIEW_DASHBOARD");
        defaultPerms.add("VIEW_REPORTS");
        
        if ("SUPER_ADMIN".equals(accessLevel)) {
            // Super admin can do everything
            defaultPerms.add("MANAGE_USERS");
            defaultPerms.add("MANAGE_ADMINS");
            defaultPerms.add("MANAGE_SYSTEM");
            defaultPerms.add("DELETE_DATA");
            defaultPerms.add("VIEW_AUDIT_LOGS");
        } else if ("ADMIN".equals(accessLevel)) {
            // Regular admin
            defaultPerms.add("MANAGE_USERS");
            defaultPerms.add("MANAGE_BOOKS");
            defaultPerms.add("MANAGE_COURSES");
            defaultPerms.add("PUBLISH_RESULTS");
        } else if ("MODERATOR".equals(accessLevel)) {
            // Moderator - limited access
            defaultPerms.add("VIEW_USERS");
            defaultPerms.add("VIEW_BOOKS");
            defaultPerms.add("VIEW_COURSES");
        }
        
        return defaultPerms;
    }
    
    /**
     * Load default permissions for this access level
     */
    public void loadDefaultPermissions() {
        this.permissions = getDefaultPermissions();
    }
    
    /**
     * Check if admin can manage users
     */
    public boolean canManageUsers() {
        return hasPermission("MANAGE_USERS") || isSuperAdmin();
    }
    
    /**
     * Check if admin can manage books
     */
    public boolean canManageBooks() {
        return hasPermission("MANAGE_BOOKS") || isSuperAdmin();
    }
    
    /**
     * Check if admin can manage courses
     */
    public boolean canManageCourses() {
        return hasPermission("MANAGE_COURSES") || isSuperAdmin();
    }
    
    /**
     * Check if admin can publish results
     */
    public boolean canPublishResults() {
        return hasPermission("PUBLISH_RESULTS") || isSuperAdmin();
    }
    
    /**
     * Check if admin can view audit logs
     */
    public boolean canViewAuditLogs() {
        return hasPermission("VIEW_AUDIT_LOGS") || isSuperAdmin();
    }
    
    /**
     * Get time since last active
     */
    public String getLastActiveTimeAgo() {
        if (lastActive == null) return "Never";
        
        long now = System.currentTimeMillis();
        long last = lastActive.getTime();
        long diff = now - last;
        
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) return days + " days ago";
        if (hours > 0) return hours + " hours ago";
        if (minutes > 0) return minutes + " minutes ago";
        return "Just now";
    }
     
    @Override
    public int getTypeId() {
        return adminId;
    }
    
    @Override
    public String getIdentifier() {
        return employeeNumber;
    }
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return adminId == admin.adminId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), adminId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "Admin{" +
               "adminId=" + adminId +
               ", name='" + getName() + '\'' +
               ", employeeNumber='" + employeeNumber + '\'' +
               ", jobTitle='" + jobTitle + '\'' +
               ", accessLevel='" + accessLevel + '\'' +
               '}';
    }
    
    /**
     * Detailed toString for debugging
     */
    public String toDetailedString() {
        return "Admin{" +
               "adminId=" + adminId +
               ", personId=" + getPersonId() +
               ", name='" + getName() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", phone='" + getPhone() + '\'' +
               ", employeeNumber='" + employeeNumber + '\'' +
               ", jobTitle='" + jobTitle + '\'' +
               ", department='" + department + '\'' +
               ", officeLocation='" + officeLocation + '\'' +
               ", responsibility='" + responsibility + '\'' +
               ", accessLevel='" + accessLevel + '\'' +
               ", lastActive=" + lastActive +
               ", createdBy='" + createdBy + '\'' +
               ", notes='" + notes + '\'' +
               ", permissionCount=" + (permissions != null ? permissions.size() : 0) +
               ", actionCount=" + getActionCount() +
               ", publishedCount=" + getPublishedResultsCount() +
               ", createdAt=" + getCreatedAt() +
               ", updatedAt=" + getUpdatedAt() +
               '}';
    }
    
    /**
     * Summary string for UI
     */
    public String toSummaryString() {
        return String.format("%s (%s) - %s",
            getFullNameWithTitle(),
            employeeNumber,
            accessLevel
        );
    }
}