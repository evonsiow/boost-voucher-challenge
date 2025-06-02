package com.boost.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class DatabaseMigrationService {
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;

    // This will be the PostgreSQL server URL base
    @Value("${spring.datasource.url}")
    private String baseUrl;
    
    public void migrateRecipientDatabase() throws Exception {
        String recipientDbUrl = baseUrl.replace("/voucher_db", "/recipient_db");
        runMigration(recipientDbUrl, "recipient-db/changelog/recipient-db.changelog-master.xml");
    }
    
    public void migrateVoucherDatabase() throws Exception {
        String voucherDbUrl = baseUrl.replace("/voucher_db", "/voucher_db");
        runMigration(voucherDbUrl, "voucher-db/changelog/voucher-db.changelog-master.xml");
    }
    
    private void runMigration(String databaseUrl, String changelogPath) throws Exception {
        // Create database if it doesn't exist
        createDatabaseIfNotExists(databaseUrl);
        
        // Run migration
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            
            Liquibase liquibase = new Liquibase(changelogPath, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        }
    }
    
    private void createDatabaseIfNotExists(String databaseUrl) throws Exception {
        // Extract database name from URL
        String dbName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1);
        String serverUrl = databaseUrl.substring(0, databaseUrl.lastIndexOf("/"));
        
        // Connect to PostgreSQL server (without specifying database)
        String postgresUrl = serverUrl + "/postgres";
        
        try (Connection connection = DriverManager.getConnection(postgresUrl, username, password)) {
            String checkDbSql = "SELECT 1 FROM pg_database WHERE datname = ?";
            try (var checkStmt = connection.prepareStatement(checkDbSql)) {
                checkStmt.setString(1, dbName);
                var result = checkStmt.executeQuery();
                
                if (!result.next()) {
                    // Database doesn't exist, create it
                    String createDbSql = "CREATE DATABASE " + dbName;
                    try (var createStmt = connection.createStatement()) {
                        createStmt.execute(createDbSql);
                        System.out.println("Created database: " + dbName);
                    }
                } else {
                    System.out.println("Database already exists: " + dbName);
                }
            }
        }
    }
} 