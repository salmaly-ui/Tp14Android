# SecureStorageLabJava - Application de stockage sécurisé Android

## Description

Application Android Java démontrant les bonnes pratiques de persistance locale : SharedPreferences, EncryptedSharedPreferences, fichiers internes, cache, stockage externe app-specific, avec règles strictes de securite.

## Pre-requis

- Android Studio Hedgehog | 2023.1.1 ou superieur
- Android SDK API 26 minimum
- Gradle 8.0+

## Installation

1. Ouvrir Android Studio
2. File -> Open -> Selectionner le dossier du projet
3. Attendre la synchronisation Gradle
4. Build -> Make Project
5. Run -> Run 'app' (Shift+F10)


## Demo 



https://github.com/user-attachments/assets/21b78cf5-3375-48ab-97cc-374a851cdbdb


## Structure du projet

app/src/main/java/com/example/securestoragelabjava/
├── ui/MainActivity.java              # Interface principale
├── prefs/UserSettingsManager.java    # Preferences simples
├── prefs/SecureTokenStorage.java     # Stockage chiffre
├── storage/TextFileManager.java      # Fichiers texte
├── storage/StudentJsonStorage.java   # Fichiers JSON
├── cache/CacheManager.java           # Gestion du cache
├── external/ExternalStorageHelper.java # Stockage externe
└── models/Student.java               # Modele de donnees

## Fonctionnalites

SAUVEGARDE PREFERENCES
- Saisir nom, langue, mode sombre
- Cliquer sur "Sauvegarder les preferences"
- Le token est stocke chiffre via EncryptedSharedPreferences

CHARGEMENT PREFERENCES
- Redemarrer l'application
- Cliquer sur "Charger les preferences"
- Les valeurs sont restaurees

FICHIERS JSON
- Cliquer sur "Sauvegarder liste etudiants (JSON)"
- Cliquer sur "Charger liste etudiants (JSON)"

EXPORT EXTERNE
- Cliquer sur "Exporter vers stockage externe"
- Le fichier est cree dans app-specific external storage

NETTOYAGE
- Cliquer sur "Tout effacer"
- Toutes les donnees sont supprimees

## Verification avec Device File Explorer

1. Android Studio -> View -> Tool Windows -> Device File Explorer
2. Naviguer vers : /data/data/com.example.securestoragelabjava/
3. Dossiers :
   - files/ : contient students_data.json et backup_info.txt
   - shared_prefs/ : contient user_settings.xml et secure_storage.xml
   - cache/ : fichiers temporaires

## Securite implementee

- Token jamais logge en clair (seule la longueur apparait dans Logcat)
- EncryptedSharedPreferences avec MasterKey pour les secrets
- MODE_PRIVATE pour tous les fichiers internes
- Champ token en inputType="textPassword"
- Bouton "Tout effacer" pour nettoyage complet
- Cache reserve aux donnees temporaires regenerables
- Export limite au stockage externe app-specific
- Encodage UTF-8 pour tous les fichiers texte
- Gestion des exceptions sans fuite d'informations

## Fichier build.gradle (Module: app)

plugins {
    id 'com.android.application'
}

android {
    namespace 'com.example.securestoragelabjava'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.securestoragelabjava"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.security:security-crypto:1.1.0-alpha06'
}


## Concepts appris

SharedPreferences : stockage de configurations simples non sensibles
apply() vs commit() : asynchrone sans retour versus synchrone avec retour booleen
EncryptedSharedPreferences : chiffrement des tokens et secrets via MasterKey et Keystore
Stockage interne : fichiers prives a l'application sans permission
JSON : format structure pour echanger des listes d'objets
Cache : stockage temporaire purgable manuellement
Stockage externe app-specific : export controle sans permission supplementaires
Securite : logs limites, chiffrement, nettoyage explicite, UTF-8 impose
