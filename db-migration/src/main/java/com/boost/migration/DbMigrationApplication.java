package com.boost.migration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class DbMigrationApplication implements CommandLineRunner {
    
    @Autowired
    private DatabaseMigrationService migrationService;
    
    public static void main(String[] args) {
        SpringApplication.run(DbMigrationApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting database migrations...");
        
        try {
            // Run recipient database migration
            System.out.println("Migrating recipient database...");
            migrationService.migrateRecipientDatabase();
            System.out.println("Recipient database migration completed successfully!");
            
            // Run voucher database migration
            System.out.println("Migrating voucher database...");
            migrationService.migrateVoucherDatabase();
            System.out.println("Voucher database migration completed successfully!");
            
            System.out.println("All database migrations completed successfully!");
        } catch (Exception e) {
            System.err.println("Database migration failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.exit(0);
    }
} 