package com.shuleyetu.config;

import com.shuleyetu.utils.LoggerUtil;
import com.shuleyetu.exception.database.ConnectionFailedException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration manager
 * Loads database properties from configuration file
 */
public class DatabaseConfig {
    
    private static final String CONFIG_FILE = "/config/database.properties";
    private static Properties properties;
    private static boolean initialized = false;
    
    // Default values (fallback if config file not found)
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DATABASE = "student_library_system";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "leraa";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection pool settings
    private static int maxPoolSize = 10;
    private static int minPoolSize = 2;
    private static int connectionTimeout = 30000;
    private static int idleTimeout = 600000;
    private static int maxLifetime = 1800000;
    
    /**
     * Static initializer - loads configuration on class load
     */
    static {
        loadConfiguration();
    }
    
    /**
     * Load database configuration from properties file
     */
    private static void loadConfiguration() {
        properties = new Properties();
        
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                initialized = true;
                LoggerUtil.info("Database configuration loaded from: " + CONFIG_FILE);
            } else {
                // Use default values
                setDefaultProperties();
                LoggerUtil.warn("Configuration file not found. Using default database settings.");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error loading database configuration", e);
            setDefaultProperties();
        }
    }
    
    /**
     * Set default properties
     */
    private static void setDefaultProperties() {
        properties = new Properties();
        properties.setProperty("db.host", DEFAULT_HOST);
        properties.setProperty("db.port", DEFAULT_PORT);
        properties.setProperty("db.name", DEFAULT_DATABASE);
        properties.setProperty("db.username", DEFAULT_USERNAME);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
        properties.setProperty("db.driver", DEFAULT_DRIVER);
        properties.setProperty("db.maxPoolSize", String.valueOf(maxPoolSize));
        properties.setProperty("db.minPoolSize", String.valueOf(minPoolSize));
        properties.setProperty("db.connectionTimeout", String.valueOf(connectionTimeout));
        properties.setProperty("db.idleTimeout", String.valueOf(idleTimeout));
        properties.setProperty("db.maxLifetime", String.valueOf(maxLifetime));
        properties.setProperty("db.useSSL", "false");
        properties.setProperty("db.allowPublicKeyRetrieval", "true");
        properties.setProperty("db.serverTimezone", "UTC");
        initialized = false;
    }
    
    /**
     * Get database URL
     */
    public static String getDatabaseUrl() {
        String host = properties.getProperty("db.host", DEFAULT_HOST);
        String port = properties.getProperty("db.port", DEFAULT_PORT);
        String database = properties.getProperty("db.name", DEFAULT_DATABASE);
        String useSSL = properties.getProperty("db.useSSL", "false");
        String allowPublicKeyRetrieval = properties.getProperty("db.allowPublicKeyRetrieval", "true");
        String serverTimezone = properties.getProperty("db.serverTimezone", "UTC");
        
        return String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=%s&allowPublicKeyRetrieval=%s&serverTimezone=%s&createDatabaseIfNotExist=true",
            host, port, database, useSSL, allowPublicKeyRetrieval, serverTimezone
        );
    }
    
    /**
     * Get database username
     */
    public static String getDatabaseUsername() {
        return properties.getProperty("db.username", DEFAULT_USERNAME);
    }
    
    /**
     * Get database password
     */
    public static String getDatabasePassword() {
        return properties.getProperty("db.password", DEFAULT_PASSWORD);
    }
    
    /**
     * Get JDBC driver class
     */
    public static String getJdbcDriver() {
        return properties.getProperty("db.driver", DEFAULT_DRIVER);
    }
    
    /**
     * Get connection properties for DriverManager
     */
    public static Properties getConnectionProperties() {
        Properties connProps = new Properties();
        connProps.setProperty("user", getDatabaseUsername());
        connProps.setProperty("password", getDatabasePassword());
        connProps.setProperty("useSSL", properties.getProperty("db.useSSL", "false"));
        connProps.setProperty("allowPublicKeyRetrieval", properties.getProperty("db.allowPublicKeyRetrieval", "true"));
        connProps.setProperty("serverTimezone", properties.getProperty("db.serverTimezone", "UTC"));
        connProps.setProperty("connectTimeout", properties.getProperty("db.connectionTimeout", "30000"));
        return connProps;
    }
    
    /**
     * Get max pool size
     */
    public static int getMaxPoolSize() {
        return Integer.parseInt(properties.getProperty("db.maxPoolSize", String.valueOf(maxPoolSize)));
    }
    
    /**
     * Get min pool size
     */
    public static int getMinPoolSize() {
        return Integer.parseInt(properties.getProperty("db.minPoolSize", String.valueOf(minPoolSize)));
    }
    
    /**
     * Get connection timeout (ms)
     */
    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("db.connectionTimeout", String.valueOf(connectionTimeout)));
    }
    
    /**
     * Get idle timeout (ms)
     */
    public static int getIdleTimeout() {
        return Integer.parseInt(properties.getProperty("db.idleTimeout", String.valueOf(idleTimeout)));
    }
    
    /**
     * Get max connection lifetime (ms)
     */
    public static int getMaxLifetime() {
        return Integer.parseInt(properties.getProperty("db.maxLifetime", String.valueOf(maxLifetime)));
    }
    
    /**
     * Get database name
     */
    public static String getDatabaseName() {
        return properties.getProperty("db.name", DEFAULT_DATABASE);
    }
    
    /**
     * Get database host
     */
    public static String getDatabaseHost() {
        return properties.getProperty("db.host", DEFAULT_HOST);
    }
    
    /**
     * Get database port
     */
    public static String getDatabasePort() {
        return properties.getProperty("db.port", DEFAULT_PORT);
    }
    
    /**
     * Check if configuration was loaded from file
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Reload configuration
     */
    public static void reloadConfiguration() {
        loadConfiguration();
        LoggerUtil.info("Database configuration reloaded");
    }
    
    /**
     * Test database connection with current settings
     */
    public static boolean testConnection() {
        try {
            Class.forName(getJdbcDriver());
            try (Connection conn = DriverManager.getConnection(
                    getDatabaseUrl(), 
                    getDatabaseUsername(), 
                    getDatabasePassword())) {
                boolean valid = conn.isValid(5);
                LoggerUtil.info("Database connection test: " + (valid ? "SUCCESS" : "FAILED"));
                return valid;
            }
        } catch (ClassNotFoundException | SQLException e) {
            LoggerUtil.error("Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Get all configuration as string
     */
    public static String getConfigInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Database Configuration:\n");
        sb.append("  Host: ").append(getDatabaseHost()).append("\n");
        sb.append("  Port: ").append(getDatabasePort()).append("\n");
        sb.append("  Database: ").append(getDatabaseName()).append("\n");
        sb.append("  Username: ").append(getDatabaseUsername()).append("\n");
        sb.append("  Max Pool Size: ").append(getMaxPoolSize()).append("\n");
        sb.append("  Min Pool Size: ").append(getMinPoolSize()).append("\n");
        sb.append("  Connection Timeout: ").append(getConnectionTimeout()).append("ms\n");
        sb.append("  Loaded from file: ").append(isInitialized());
        return sb.toString();
    }
    
    /**
     * Validate configuration
     */
    public static boolean validateConfiguration() {
        if (getDatabaseUsername() == null || getDatabaseUsername().trim().isEmpty()) {
            LoggerUtil.error("Database username not configured");
            return false;
        }
        
        if (getDatabaseHost() == null || getDatabaseHost().trim().isEmpty()) {
            LoggerUtil.error("Database host not configured");
            return false;
        }
        
        try {
            Integer.parseInt(getDatabasePort());
        } catch (NumberFormatException e) {
            LoggerUtil.error("Invalid database port: " + getDatabasePort());
            return false;
        }
        
        return true;
    }
}