package com.shuleyetu.dao;

import com.shuleyetu.config.DatabaseConfig;
import com.shuleyetu.exception.database.ConnectionFailedException;
import com.shuleyetu.exception.database.QueryExecutionException;
import com.shuleyetu.utils.LoggerUtil;

import java.sql.*;
import java.util.Properties;


public class DBConnection {
    
    private static DBConnection instance;
    private Connection connection;
    private static final Object lock = new Object();
    
    // Connection pool properties
    private int activeConnections = 0;
    private int maxConnections = 10;
    
    /**
     * Private constructor for Singleton pattern
     */
    private DBConnection() {
        // Initialize connection pool
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LoggerUtil.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LoggerUtil.error("MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get Singleton instance
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() throws ConnectionFailedException {
        try {
            // Check if connection is closed or null
            if (connection == null || connection.isClosed()) {
                synchronized (lock) {
                    if (connection == null || connection.isClosed()) {
                        createNewConnection();
                    }
                }
            }
            
            // Test connection
            if (!connection.isValid(5)) {
                synchronized (lock) {
                    createNewConnection();
                }
            }
            
            activeConnections++;
            LoggerUtil.debug("Database connection obtained. Active connections: " + activeConnections);
            return connection;
            
        } catch (SQLException e) {
            LoggerUtil.error("Failed to get database connection", e);
            throw new ConnectionFailedException(
                "Unable to connect to database: " + e.getMessage(),
                "DB_001",
                "Database connection error. Please check if MySQL server is running."
            );
        }
    }
    
    /**
     * Create new database connection
     */
    private void createNewConnection() throws SQLException {
        Properties props = DatabaseConfig.getConnectionProperties();
        
        String url = DatabaseConfig.getDatabaseUrl();
        String username = DatabaseConfig.getDatabaseUsername();
        String password = DatabaseConfig.getDatabasePassword();
        
        connection = DriverManager.getConnection(url, username, password);
        
        // Set connection properties
        connection.setAutoCommit(true);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        
        LoggerUtil.info("New database connection created successfully");
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                activeConnections--;
                LoggerUtil.info("Database connection closed. Active connections: " + activeConnections);
            }
        } catch (SQLException e) {
            LoggerUtil.error("Error closing database connection", e);
        }
    }
    
    /**
     * Close all connections (for application shutdown)
     */
    public void shutdown() {
        closeConnection();
        instance = null;
        LoggerUtil.info("Database connection pool shut down");
    }
    
    /**
     * Execute query and return ResultSet (use with try-with-resources)
     */
    public ResultSet executeQuery(String query, Object... params) throws QueryExecutionException {
        try {
            PreparedStatement stmt = prepareStatement(query, params);
            return stmt.executeQuery();
        } catch (SQLException e) {
            LoggerUtil.error("Query execution failed: " + query, e);
            throw new QueryExecutionException(
                "Failed to execute query: " + e.getMessage(),
                "DB_002",
                "Database query error"
            );
        }
    }
    
    /**
     * Execute update (INSERT, UPDATE, DELETE)
     */
    public int executeUpdate(String query, Object... params) throws QueryExecutionException {
        try (PreparedStatement stmt = prepareStatement(query, params)) {
            int affectedRows = stmt.executeUpdate();
            LoggerUtil.debug("Update executed. Rows affected: " + affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            LoggerUtil.error("Update execution failed: " + query, e);
            throw new QueryExecutionException(
                "Failed to execute update: " + e.getMessage(),
                "DB_003",
                "Database update error"
            );
        }
    }
    
    /**
     * Execute insert and return generated keys
     */
    public int executeInsert(String query, Object... params) throws QueryExecutionException {
        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setParameters(stmt, params);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insert failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LoggerUtil.error("Insert execution failed: " + query, e);
            throw new QueryExecutionException(
                "Failed to execute insert: " + e.getMessage(),
                "DB_004",
                "Database insert error"
            );
        }
    }
    
    /**
     * Prepare statement with parameters
     */
    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        setParameters(stmt, params);
        return stmt;
    }
    
    /**
     * Set parameters for prepared statement
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1;
            
            if (param == null) {
                stmt.setNull(index, Types.NULL);
            } else if (param instanceof String) {
                stmt.setString(index, (String) param);
            } else if (param instanceof Integer) {
                stmt.setInt(index, (Integer) param);
            } else if (param instanceof Long) {
                stmt.setLong(index, (Long) param);
            } else if (param instanceof Double) {
                stmt.setDouble(index, (Double) param);
            } else if (param instanceof Float) {
                stmt.setFloat(index, (Float) param);
            } else if (param instanceof Boolean) {
                stmt.setBoolean(index, (Boolean) param);
            } else if (param instanceof Date) {
                stmt.setDate(index, (Date) param);
            } else if (param instanceof Timestamp) {
                stmt.setTimestamp(index, (Timestamp) param);
            } else if (param instanceof java.util.Date) {
                stmt.setTimestamp(index, new Timestamp(((java.util.Date) param).getTime()));
            } else {
                stmt.setObject(index, param);
            }
        }
    }
    
    /**
     * Begin transaction
     */
    public void beginTransaction() throws ConnectionFailedException {
        try {
            Connection conn = getConnection();
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
                LoggerUtil.debug("Transaction started");
            }
        } catch (SQLException e) {
            LoggerUtil.error("Failed to begin transaction", e);
            throw new ConnectionFailedException(
                "Failed to begin transaction: " + e.getMessage(),
                "DB_005",
                "Transaction error"
            );
        }
    }
    
    /**
     * Commit transaction
     */
    public void commitTransaction() throws QueryExecutionException {
        try {
            Connection conn = getConnection();
            if (!conn.getAutoCommit()) {
                conn.commit();
                conn.setAutoCommit(true);
                LoggerUtil.debug("Transaction committed");
            }
        } catch (SQLException e) {
            LoggerUtil.error("Failed to commit transaction", e);
            throw new QueryExecutionException(
                "Failed to commit transaction: " + e.getMessage(),
                "DB_006",
                "Transaction commit error"
            );
        }
    }
    
    /**
     * Rollback transaction
     */
    public void rollbackTransaction() {
        try {
            Connection conn = getConnection();
            if (!conn.getAutoCommit()) {
                conn.rollback();
                conn.setAutoCommit(true);
                LoggerUtil.debug("Transaction rolled back");
            }
        } catch (SQLException e) {
            LoggerUtil.error("Failed to rollback transaction", e);
        }
    }
    
    /**
     * Check if connection is valid
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Get active connection count
     */
    public int getActiveConnections() {
        return activeConnections;
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();
            boolean isValid = conn.isValid(5);
            db.closeConnection();
            return isValid;
        } catch (Exception e) {
            LoggerUtil.error("Connection test failed", e);
            return false;
        }
    }
}