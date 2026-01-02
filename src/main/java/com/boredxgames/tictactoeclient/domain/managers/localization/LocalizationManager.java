package com.boredxgames.tictactoeclient.domain.managers.localization;

import javafx.fxml.FXMLLoader;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static Locale currentLocale;
    private static ResourceBundle localizationBundle;

    static{
        Languages initialLanguage = Languages.ARABIC;
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

}