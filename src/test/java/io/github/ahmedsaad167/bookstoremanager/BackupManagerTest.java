package io.github.ahmedsaad167.bookstoremanager;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import io.github.ahmedsaad167.bookstoremanager.backup.BackupManager;

public class BackupManagerTest {

    @Test
    void createBackup_shouldCreateDatabaseCopy() throws Exception {

        BackupManager backupManager = new BackupManager();

        Path backupPath = backupManager.createBackup();

        assertNotNull(backupPath);
        assertTrue(Files.exists(backupPath));
        assertTrue(Files.size(backupPath) > 0);

        System.out.println("Backup created at: " + backupPath);
    }
}