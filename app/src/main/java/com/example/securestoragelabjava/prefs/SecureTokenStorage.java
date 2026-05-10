package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureTokenStorage {
    private static final String SECURE_FILE = "secure_storage";
    private static final String KEY_TOKEN = "user_token";
    private static final String TAG = "SecureStorage";

    private final Context context;
    private SharedPreferences securePrefs;

    public SecureTokenStorage(Context context) {
        this.context = context;
        initSecurePreferences();
    }

    private void initSecurePreferences() {
        try {
            // Création de la MasterKey avec le keystore Android
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Création des SharedPreferences chiffrées
            securePrefs = EncryptedSharedPreferences.create(
                    context,
                    SECURE_FILE,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Erreur d'initialisation du stockage sécurisé: " + e.getMessage());
            securePrefs = null;
        }
    }

    // Sauvegarde du token (ne JAMAIS logger le token en clair)
    public void saveToken(String token) {
        if (securePrefs == null) {
            Log.e(TAG, "Stockage sécurisé non disponible");
            return;
        }
        // IMPORTANT: Ne pas logger le token
        Log.d(TAG, "Sauvegarde token - longueur: " + (token != null ? token.length() : 0) + " caractères");
        securePrefs.edit().putString(KEY_TOKEN, token).apply();
    }

    // Chargement du token
    public String loadToken() {
        if (securePrefs == null) {
            Log.e(TAG, "Stockage sécurisé non disponible");
            return "";
        }
        String token = securePrefs.getString(KEY_TOKEN, "");
        // Ne jamais logger le token en clair
        Log.d(TAG, "Token chargé - présent: " + (token != null && !token.isEmpty()));
        return token;
    }

    // Suppression du token
    public void clearToken() {
        if (securePrefs != null) {
            securePrefs.edit().remove(KEY_TOKEN).apply();
            Log.d(TAG, "Token supprimé");
        }
    }
}