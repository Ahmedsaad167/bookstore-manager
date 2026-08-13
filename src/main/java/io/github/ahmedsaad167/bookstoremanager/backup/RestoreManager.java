package io.github.ahmedsaad167.bookstoremanager.backup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RestoreManager {
    
    private static final Path DEFAULT_DATABASE_PATH = Path.of("bookstore.db");

    private final Path databasePath;

    public RestoreManager() {
        this(DEFAULT_DATABASE_PATH);
    }

    public RestoreManager(Path databasePath) {
        if (databasePath == null) {
            throw new IllegalArgumentException("Database path cannot be null.");
        }

        this.databasePath = databasePath;
    }

    public void restore(Path backupPath) throws IOException {

        validateRestore(backupPath);

        Files.copy(backupPath, databasePath, StandardCopyOption.REPLACE_EXISTING);
    }
    
    private void validateRestore(Path backupPath) throws IOException {
        if (backupPath == null) {
            throw new IllegalArgumentException("Backup path cannot be null.");
        }
    
        if (!Files.exists(backupPath)) {
            throw new IOException("Backup file does not exist.");
        }
    
        if (!Files.isRegularFile(backupPath)) {
            throw new IOException("Backup path is not a file.");
        }
    }
}
