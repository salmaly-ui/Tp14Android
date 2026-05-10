package com.example.securestoragelabjava.cache;

import android.content.Context;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CacheManager {
    private final Context context;

    public CacheManager(Context context) {
        this.context = context;
    }

    // Écrire dans le cache
    public boolean writeToCache(String fileName, String content) {
        try {
            File cacheFile = new File(context.getCacheDir(), fileName);
            Files.write(cacheFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Lire depuis le cache
    public String readFromCache(String fileName) {
        try {
            File cacheFile = new File(context.getCacheDir(), fileName);
            if (!cacheFile.exists()) return null;
            byte[] data = Files.readAllBytes(cacheFile.toPath());
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    // Vider tout le cache
    public int clearCache() {
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        int deletedCount = 0;

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    deletedCount++;
                }
            }
        }
        return deletedCount;
    }
}