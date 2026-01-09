package com.boredxgames.tictactoeclient.domain.managers.localization;

import javafx.fxml.FXMLLoader;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static Locale currentLocale;
    private static ResourceBundle localizationBundle;

    static{
        Languages initialLanguage = Languages.ENGLISH;
        changeLocale(initialLanguage);
    }


    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static ResourceBundle getLocalizationBundle(){
        return localizationBundle;
    }

    public static void changeLocale(Languages newLanguage) {
        currentLocale = Locale.of(newLanguage.getCode(), newLanguage.getCountry());
        setLocalizationBundle(currentLocale);
    }
    private static void setLocalizationBundle(Locale locale) {
        localizationBundle = ResourceBundle.getBundle("assets/localization.message", locale);
    }

    // Added: helper method to get the localized name of a language for UI (e.g., ComboBox)
    public static String getLanguageName(Languages lang) {
        ResourceBundle bundle = getLocalizationBundle();
        if (lang == Languages.ENGLISH) {
            return bundle.getString("language.english");
        } else if (lang == Languages.ARABIC) {
            return bundle.getString("language.arabic");
        }
        return "";
    }

    
    // Added: helper method to get the other language (not currently selected)
    public static Languages getOtherLanguage(Languages lang) {
        return lang == Languages.ARABIC ? Languages.ENGLISH : Languages.ARABIC;
    }
}