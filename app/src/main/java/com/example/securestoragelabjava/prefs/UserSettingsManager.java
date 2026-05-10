package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettingsManager {
    private static final String SETTINGS_FILE = "user_settings";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final Context context;

    public UserSettingsManager(Context context) {
        this.context = context;
    }

    // Sauvegarde asynchrone (apply)
    public void saveSettingsAsync(String username, String language, boolean darkMode) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_LANGUAGE, language)
                .putBoolean(KEY_DARK_MODE, darkMode)
                .apply();  // Asynchrone, pas de retour
    }

    // Sauvegarde synchrone (commit) - retourne true/false
    public boolean saveSettingsSync(String username, String language, boolean darkMode) {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        return prefs.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_LANGUAGE, language)
                .putBoolean(KEY_DARK_MODE, darkMode)
                .commit();  // Synchrone, retourne le résultat
    }

    // Chargement des settings
    public UserSettings loadSettings() {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, "");
        String language = prefs.getString(KEY_LANGUAGE, "fr");
        boolean darkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        return new UserSettings(username, language, darkMode);
    }

    // Effacer toutes les préférences
    public void clearAll() {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    // Classe interne pour les résultats
    public static class UserSettings {
        public final String username;
        public final String language;
        public final boolean darkMode;

        public UserSettings(String username, String language, boolean darkMode) {
            this.username = username;
            this.language = language;
            this.darkMode = darkMode;
        }
    }
}