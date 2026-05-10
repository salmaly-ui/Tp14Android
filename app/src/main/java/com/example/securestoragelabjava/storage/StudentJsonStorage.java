package com.example.securestoragelabjava.storage;

import android.content.Context;

import com.example.securestoragelabjava.models.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StudentJsonStorage {
    private static final String STUDENTS_FILE = "students_data.json";
    private final Context context;

    public StudentJsonStorage(Context context) {
        this.context = context;
    }

    // Sauvegarder la liste des étudiants en JSON
    public boolean saveStudents(List<Student> students) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Student s : students) {
                JSONObject obj = new JSONObject();
                obj.put("id", s.getId());
                obj.put("fullName", s.getFullName());
                obj.put("studentAge", s.getStudentAge());
                jsonArray.put(obj);
            }

            try (FileOutputStream fos = context.openFileOutput(STUDENTS_FILE, Context.MODE_PRIVATE)) {
                fos.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Charger la liste des étudiants depuis JSON
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(STUDENTS_FILE)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String jsonString = new String(data, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Student s = new Student(
                        obj.getInt("id"),
                        obj.getString("fullName"),
                        obj.getInt("studentAge")
                );
                students.add(s);
            }
        } catch (Exception e) {
            // Fichier inexistant ou corrompu - retourner liste vide
        }
        return students;
    }

    // Supprimer le fichier JSON
    public boolean deleteStudentsFile() {
        return context.deleteFile(STUDENTS_FILE);
    }
}