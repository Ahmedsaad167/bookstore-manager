package io.github.ahmedsaad167.bookstoremanager;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.ahmedsaad167.bookstoremanager.backup.RestoreManager;

public class RestoreManagerTest {

    @TempDir
    Path tempDirectory;

    @Test
    void restore_shouldRestoreDatabaseCopy() throws Exception {

        // Arrange
        Path databasePath =
                tempDirectory.resolve("bookstore.db");

        Path backupPath =
                tempDirectory.resolve("bookstore_backup.db");

        Files.writeString(
                databasePath,
                "current database"
        );

        Files.writeString(
                backupPath,
                "backup database"
        );

        RestoreManager restoreManager =
                new RestoreManager(databasePath);

        // Act
        restoreManager.restore(backupPath);

        // Assert
        assertTrue(Files.exists(databasePath));

        assertEquals(
                "backup database",
                Files.readString(databasePath)
        );
    }
}