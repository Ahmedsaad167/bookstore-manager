package io.github.ahmedsaad167.bookstoremanager;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.ahmedsaad167.bookstoremanager.backup.BackupManager;

public class BackupManagerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void createBackup_shouldCreateDatabaseCopy() throws Exception {

        // Arrange
        Path databasePath = tempDirectory.resolve("test.db");

        Files.writeString(
                databasePath,
                "test database content"
        );

        BackupManager backupManager =
                new BackupManager(databasePath);

        // Act
        Path backupPath = backupManager.createBackup();

        // Assert
        assertNotNull(backupPath);
        assertTrue(Files.exists(backupPath));
        assertTrue(Files.size(backupPath) > 0);

        assertEquals(
                Files.readString(databasePath),
                Files.readString(backupPath)
        );

        System.out.println("Backup created at: " + backupPath);
    }
}