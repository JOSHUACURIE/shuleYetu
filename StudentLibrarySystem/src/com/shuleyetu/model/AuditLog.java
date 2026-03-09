package com.shuleyetu.model;

import java.sql.Timestamp;
import java.util.Objects;

public class AuditLog {
    private int logId;
    private Integer userId;
    private String actionType;  // "LOGIN", "CREATE", "UPDATE", etc.
    private String tableName;
    private Integer recordId;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private String userAgent;
    private Timestamp timestamp;
    
    // Relationships
    private UserAccount user;
 
    public AuditLog() {
     
    }
    
 
    public AuditLog(Integer userId, String actionType, String tableName, 
                    Integer recordId, String oldValue, String newValue, 
                    String ipAddress, String userAgent) {
        this.userId = userId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
    

    public AuditLog(Integer userId, String actionType, String ipAddress) {
        this.userId = userId;
        this.actionType = actionType;
        this.ipAddress = ipAddress;
    }
    

    public AuditLog(Integer userId, String actionType, String tableName, 
                    Integer recordId, String oldValue, String newValue) {
        this.userId = userId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    /**
     * Full constructor including ID (for when reading from database)
     */
    public AuditLog(int logId, Integer userId, String actionType, String tableName, 
                    Integer recordId, String oldValue, String newValue, 
                    String ipAddress, String userAgent, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.timestamp = timestamp;
    }
    
    // ============ GETTERS AND SETTERS ============
    
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public Integer getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public UserAccount getUser() {
        return user;
    }
    
    public void setUser(UserAccount user) {
        this.user = user;
    }
    
    // ============ BUSINESS METHODS ============
    
    /**
     * Get a formatted description of the audit action
     */
    public String getActionDescription() {
        StringBuilder desc = new StringBuilder();
        
        if (actionType != null) {
            switch (actionType) {
                case "LOGIN":
                    desc.append("User logged in");
                    break;
                case "LOGOUT":
                    desc.append("User logged out");
                    break;
                case "CREATE":
                    desc.append("Created ").append(tableName).append(" record");
                    break;
                case "UPDATE":
                    desc.append("Updated ").append(tableName).append(" record");
                    break;
                case "DELETE":
                    desc.append("Deleted ").append(tableName).append(" record");
                    break;
                case "BORROW":
                    desc.append("Borrowed book");
                    break;
                case "RETURN":
                    desc.append("Returned book");
                    break;
                case "RESERVE":
                    desc.append("Reserved book");
                    break;
                default:
                    desc.append(actionType);
            }
        }
        
        if (recordId != null) {
            desc.append(" (ID: ").append(recordId).append(")");
        }
        
        return desc.toString();
    }
    
    /**
     * Check if this is a login action
     */
    public boolean isLoginAction() {
        return "LOGIN".equals(actionType);
    }
    
    /**
     * Check if this is a database modification action
     */
    public boolean isModificationAction() {
        return "CREATE".equals(actionType) || 
               "UPDATE".equals(actionType) || 
               "DELETE".equals(actionType);
    }
    
    // ============ equals() and hashCode() ============
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return logId == auditLog.logId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(logId);
    }
    
    // ============ toString() ============
    
    @Override
    public String toString() {
        return "AuditLog{" +
               "logId=" + logId +
               ", userId=" + userId +
               ", actionType='" + actionType + '\'' +
               ", tableName='" + tableName + '\'' +
               ", recordId=" + recordId +
               ", oldValue='" + (oldValue != null ? oldValue.substring(0, Math.min(oldValue.length(), 20)) + "..." : null) + '\'' +
               ", newValue='" + (newValue != null ? newValue.substring(0, Math.min(newValue.length(), 20)) + "..." : null) + '\'' +
               ", ipAddress='" + ipAddress + '\'' +
               ", userAgent='" + userAgent + '\'' +
               ", timestamp=" + timestamp +
               '}';
    }
    
    /**
     * Simplified toString for logging
     */
    public String toShortString() {
        return String.format("[%s] %s - User: %d, %s", 
            timestamp, actionType, userId, getActionDescription());
    }
}