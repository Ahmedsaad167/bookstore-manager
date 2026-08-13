package io.github.ahmedsaad167.bookstoremanager.backup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupManager {

    private static final Path DEFAULT_DATABASE_PATH = Path.of("bookstore.db");
    private static final Path BACKUP_DIRECTORY = Path.of("backup");

    private static final DateTimeFormatter BACKUP_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final Path databasePath;

    public BackupManager() {
        this(DEFAULT_DATABASE_PATH);
    }

    public BackupManager(Path databasePath) {
        if (databasePath == null) {
            throw new IllegalArgumentException("Database path cannot be null.");
        }

        this.databasePath = databasePath;
    }

    public Path createBackup() throws IOException {

        if (!Files.exists(databasePath)) {
            throw new IOException("Database file does not exist.");
        }

        Files.createDirectories(BACKUP_DIRECTORY);

        String timestamp = LocalDateTime.now()
                .format(BACKUP_DATE_FORMAT);

        Path backupPath = BACKUP_DIRECTORY.resolve(
                "bookstore_backup_" + timestamp + ".db"
        );

        Files.copy(
                databasePath,
                backupPath,
                StandardCopyOption.COPY_ATTRIBUTES
        );

        return backupPath;
    }
}