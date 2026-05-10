package com.example.securestoragelabjava.storage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TextFileManager {
    private final Context context;

    public TextFileManager(Context context) {
        this.context = context;
    }

    // Écrire un fichier texte en UTF-8
    public boolean writeTextFile(String fileName, String content) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lire un fichier texte
    public String readTextFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // Supprimer un fichier
    public boolean deleteFile(String fileName) {
        return context.deleteFile(fileName);
    }
}