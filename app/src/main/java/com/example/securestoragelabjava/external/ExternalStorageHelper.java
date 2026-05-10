package com.example.securestoragelabjava.external;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ExternalStorageHelper {
    private final Context context;

    public ExternalStorageHelper(Context context) {
        this.context = context;
    }

    // Exporter un fichier vers le stockage externe app-specific
    public String exportToExternal(String fileName, String content) {
        // Vérifier si le stockage externe est disponible
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }

        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return null;

        try {
            File exportFile = new File(externalDir, fileName);
            Files.write(exportFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            return exportFile.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    // Lire depuis le stockage externe
    public String readFromExternal(String fileName) {
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return null;

        try {
            File file = new File(externalDir, fileName);
            if (!file.exists()) return null;
            byte[] data = Files.readAllBytes(file.toPath());
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    // Supprimer du stockage externe
    public boolean deleteFromExternal(String fileName) {
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return false;

        File file = new File(externalDir, fileName);
        return file.exists() && file.delete();
    }
}