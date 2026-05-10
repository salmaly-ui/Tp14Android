package com.example.securestoragelabjava.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securestoragelabjava.R;
import com.example.securestoragelabjava.cache.CacheManager;
import com.example.securestoragelabjava.external.ExternalStorageHelper;
import com.example.securestoragelabjava.models.Student;
import com.example.securestoragelabjava.prefs.SecureTokenStorage;
import com.example.securestoragelabjava.prefs.UserSettingsManager;
import com.example.securestoragelabjava.storage.StudentJsonStorage;
import com.example.securestoragelabjava.storage.TextFileManager;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "StorageLab";

     private UserSettingsManager settingsManager;
    private SecureTokenStorage tokenStorage;
    private TextFileManager textFileManager;
    private StudentJsonStorage jsonStorage;
    private CacheManager cacheManager;
    private ExternalStorageHelper externalHelper;

     private EditText etUsername;
    private EditText etToken;
    private Spinner spinnerLanguage;
    private Switch switchDarkMode;
    private TextView tvResult;

    private final List<String> languages = Arrays.asList("Français", "English", "العربية");
    private final List<String> languageCodes = Arrays.asList("fr", "en", "ar");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         settingsManager = new UserSettingsManager(this);
        tokenStorage = new SecureTokenStorage(this);
        textFileManager = new TextFileManager(this);
        jsonStorage = new StudentJsonStorage(this);
        cacheManager = new CacheManager(this);
        externalHelper = new ExternalStorageHelper(this);


        initViews();

         setupLanguageSpinner();

         setupButtons();

         loadPreferencesToUI();
    }

    private void initViews() {
        // Utiliser findViewById avec les bons types
        etUsername = findViewById(R.id.etUsername);
        etToken = findViewById(R.id.etToken);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        tvResult = findViewById(R.id.tvResult);
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, languages);
        spinnerLanguage.setAdapter(adapter);
    }

    private void setupButtons() {
        findViewById(R.id.btnSavePreferences).setOnClickListener(v -> savePreferences());
        findViewById(R.id.btnLoadPreferences).setOnClickListener(v -> loadPreferencesToUI());
        findViewById(R.id.btnSaveJson).setOnClickListener(v -> saveStudentsToJson());
        findViewById(R.id.btnLoadJson).setOnClickListener(v -> loadStudentsFromJson());
        findViewById(R.id.btnExportExternal).setOnClickListener(v -> exportToExternal());
        findViewById(R.id.btnClearAll).setOnClickListener(v -> clearAllData());
    }

    private void savePreferences() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        int langPosition = spinnerLanguage.getSelectedItemPosition();
        String languageCode = langPosition >= 0 ? languageCodes.get(langPosition) : "fr";
        boolean darkMode = switchDarkMode.isChecked();

         settingsManager.saveSettingsAsync(username, languageCode, darkMode);

         String token = etToken.getText() != null ? etToken.getText().toString() : "";
        if (!token.isEmpty()) {
            tokenStorage.saveToken(token);
        }

         cacheManager.writeToCache("last_save.txt", "Dernière sauvegarde: " + System.currentTimeMillis());

         Log.d(TAG, "Préférences sauvegardées - Utilisateur: " + username +
                ", Langue: " + languageCode + ", Mode sombre: " + darkMode);

        tvResult.setText("✓ Préférences sauvegardées !\n" +
                "Utilisateur: " + username + "\n" +
                "Langue: " + languages.get(langPosition) + "\n" +
                "Mode sombre: " + (darkMode ? "Activé" : "Désactivé") + "\n" +
                "Token: Stocké chiffré (" + token.length() + " caractères)");
    }

    private void loadPreferencesToUI() {
        UserSettingsManager.UserSettings settings = settingsManager.loadSettings();

        etUsername.setText(settings.username);

         int langIndex = languageCodes.indexOf(settings.language);
        if (langIndex >= 0) {
            spinnerLanguage.setSelection(langIndex);
        }

        switchDarkMode.setChecked(settings.darkMode);

         String token = tokenStorage.loadToken();
        if (!token.isEmpty()) {
            etToken.setText(token);
        }

         String lastSave = cacheManager.readFromCache("last_save.txt");

        tvResult.setText("✓ Préférences chargées !\n" +
                "Utilisateur: " + settings.username + "\n" +
                "Langue: " + settings.language + "\n" +
                "Mode sombre: " + (settings.darkMode ? "Activé" : "Désactivé") + "\n" +
                "Token présent: " + (!token.isEmpty() ? "Oui (" + token.length() + " caractères)" : "Non") + "\n" +
                (lastSave != null ? "Cache: " + lastSave : ""));

        Log.d(TAG, "Préférences chargées - Utilisateur: " + settings.username);
    }

    private void saveStudentsToJson() {
        List<Student> students = Arrays.asList(
                new Student(1, "Ahmed Benali", 22),
                new Student(2, "Fatima Zahra", 20),
                new Student(3, "Youssef Mansouri", 23),
                new Student(4, "Leila Hakim", 21)
        );

        boolean success = jsonStorage.saveStudents(students);

        if (success) {
            textFileManager.writeTextFile("backup_info.txt",
                    "Sauvegarde JSON effectuée le " + new java.util.Date());

            tvResult.setText("✓ Fichier JSON sauvegardé !\n" +
                    "Fichier: students_data.json\n" +
                    "Nombre d'étudiants: " + students.size() + "\n" +
                    "Emplacement: /data/data/" + getPackageName() + "/files/");

            Log.d(TAG, "JSON sauvegardé - " + students.size() + " étudiants");
        } else {
            tvResult.setText("✗ Erreur lors de la sauvegarde JSON");
        }
    }

    private void loadStudentsFromJson() {
        List<Student> students = jsonStorage.loadStudents();
        String backupInfo = textFileManager.readTextFile("backup_info.txt");

        StringBuilder result = new StringBuilder();
        result.append("✓ Chargement JSON terminé !\n");
        result.append("Fichier: students_data.json\n");
        result.append("Étudiants trouvés: ").append(students.size()).append("\n\n");

        if (!students.isEmpty()) {
            result.append("Liste des étudiants:\n");
            for (Student s : students) {
                result.append("  • ").append(s.toString()).append("\n");
            }
        } else {
            result.append("Aucun étudiant trouvé. Veuillez d'abord sauvegarder.\n");
        }

        if (backupInfo != null) {
            result.append("\nInfo sauvegarde: ").append(backupInfo);
        }

        tvResult.setText(result.toString());
        Log.d(TAG, "JSON chargé - " + students.size() + " étudiants");
    }

    private void exportToExternal() {
        String exportContent = "Export depuis l'application\n" +
                "Date: " + new java.util.Date() + "\n" +
                "Utilisateur: " + settingsManager.loadSettings().username;

        String filePath = externalHelper.exportToExternal("export_data.txt", exportContent);

        if (filePath != null) {
            tvResult.setText("✓ Fichier exporté vers stockage externe !\n" +
                    "Chemin: " + filePath + "\n" +
                    "Contenu exporté:\n" + exportContent);
            Log.d(TAG, "Fichier exporté: " + filePath);
        } else {
            tvResult.setText("✗ Erreur d'export - Stockage externe non disponible");
        }
    }

    private void clearAllData() {
         settingsManager.clearAll();

         tokenStorage.clearToken();

         jsonStorage.deleteStudentsFile();
        textFileManager.deleteFile("backup_info.txt");

        // Vider le cache
        int cacheCleared = cacheManager.clearCache();

         externalHelper.deleteFromExternal("export_data.txt");

         etUsername.setText("");
        etToken.setText("");
        spinnerLanguage.setSelection(0);
        switchDarkMode.setChecked(false);

        tvResult.setText("✓ Nettoyage complet terminé !\n" +
                "- Préférences utilisateur: effacées\n" +
                "- Token sécurisé: effacé\n" +
                "- Fichiers JSON: supprimés\n" +
                "- Fichiers texte: supprimés\n" +
                "- Cache: " + cacheCleared + " fichier(s) supprimé(s)\n" +
                "- Fichiers externes: supprimés");

        Log.d(TAG, "Nettoyage complet effectué - " + cacheCleared + " fichiers cache supprimés");
    }
}