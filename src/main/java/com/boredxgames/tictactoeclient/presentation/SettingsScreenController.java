package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.audio.AudioManager;
import com.boredxgames.tictactoeclient.domain.managers.localization.Languages;
import com.boredxgames.tictactoeclient.domain.managers.localization.LocalizationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.theme.Theme;
import com.boredxgames.tictactoeclient.domain.managers.theme.ThemeManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 */
public class SettingsScreenController implements Initializable {

    @FXML
    private ComboBox<String> languageCombo;
    @FXML
    private ToggleButton blueTheme, pinkTheme, greenTheme;
    @FXML
    private CheckBox musicToggle, sfxToggle;
    @FXML
    private Slider musicSlider, sfxSlider;
    @FXML
    private Button backBtn;
    @FXML
    private Label settingsTitle;
    @FXML
    private Label languageTitle;
    @FXML
    private Label languageSubtitle;
    @FXML
    private Label themeTitle;
    @FXML
    private Label audioTitle;
    @FXML
    private Label musicLabel;
    @FXML
    private Label sfxLabel;
    @FXML
    private Label footerLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        // ===== Music Toggle =====
//        musicToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
//            if (isSelected) {
//                AudioManager.playMusic("/assets/sounds/bgm.mp3", musicSlider.getValue() / 100.0);
//            } else {
//                AudioManager.stopMusic();
//            }
//        });
//
//        musicToggle.selectedProperty().addListener((obs, was, is) -> {
//            if (is) {
//                AudioManager.playMusic("/assets/sounds/bgm.mp3", musicSlider.getValue() / 100.0);
//            } else {
//                AudioManager.stopMusic();
//            }
//        });
//
//        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
//            AudioManager.setMusicVolume(newVal.doubleValue() / 100.0);
//        });
//
//// ===== SFX Toggle + Slider =====
//        sfxToggle.selectedProperty().addListener((obs, was, is) -> {
//            AudioManager.setSfxEnabled(is);
//            if (is) {
//                AudioManager.playSfx("/assets/sounds/bgm.mp3", sfxSlider.getValue() / 100.0);
//            }
//        });
//
//        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
//            AudioManager.setSfxVolume(newVal.doubleValue() / 100.0);
//        });

        // ===== Languages =====
        languageCombo.getItems().addAll(
                "English (US)",
                "Arabic (EG)"
        );
        languageCombo.getSelectionModel().selectFirst();

        languageCombo.setOnAction(e -> {
            int selectedIndex = languageCombo.getSelectionModel().getSelectedIndex();

            if (selectedIndex == 0) {
                // English
                LocalizationManager.changeLocale(Languages.ENGLISH);
            } else if (selectedIndex == 1) {
                // Arabic
                LocalizationManager.changeLocale(Languages.ARABIC);
            }

            updateTexts();
        });

        // ===== Theme =====
        ToggleGroup themeGroup = new ToggleGroup();
        blueTheme.setToggleGroup(themeGroup);
        pinkTheme.setToggleGroup(themeGroup);
        greenTheme.setToggleGroup(themeGroup);

        
        Theme current = ThemeManager.getTheme();
        if (current == Theme.NEON_BLUE) {
            blueTheme.setSelected(true);
        } else if (current == Theme.CYBER_PINK) {
            pinkTheme.setSelected(true);
        } else if (current == Theme.TOXIC_GREEN) {
            greenTheme.setSelected(true);
        }

        
        themeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == blueTheme) {
                ThemeManager.setTheme(Theme.NEON_BLUE);
            } else if (newToggle == pinkTheme) {
                ThemeManager.setTheme(Theme.CYBER_PINK);
            } else if (newToggle == greenTheme) {
                ThemeManager.setTheme(Theme.TOXIC_GREEN);
            }
        });

        musicSlider.disableProperty().bind(musicToggle.selectedProperty().not());
        sfxSlider.disableProperty().bind(sfxToggle.selectedProperty().not());

        // ===== Back button =====
        backBtn.setOnAction(e -> {
            System.out.println("Back pressed");
            NavigationManager.pop();
        });

        try {
            updateTexts();
        } catch (Exception e) {
            System.out.println("Localization failed: " + e.getMessage());
        }

    }

    private String safeGet(ResourceBundle bundle, String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    public void updateTexts() {
        ResourceBundle bundle = LocalizationManager.getLocalizationBundle();

        if (settingsTitle != null) {
            settingsTitle.setText(safeGet(bundle, "settings.title", "Settings"));
        }
        if (backBtn != null) {
            backBtn.setText(safeGet(bundle, "settings.back", "Back"));
        }
        if (blueTheme != null) {
            blueTheme.setText(safeGet(bundle, "settings.theme.blue", "Neon Blue"));
        }
        if (pinkTheme != null) {
            pinkTheme.setText(safeGet(bundle, "settings.theme.pink", "Cyber Pink"));
        }
        if (greenTheme != null) {
            greenTheme.setText(safeGet(bundle, "settings.theme.green", "Toxic Green"));
        }
        if (audioTitle != null) {
            audioTitle.setText(safeGet(bundle, "settings.audio.title", "Audio"));
        }
        if (musicLabel != null) {
            musicLabel.setText(safeGet(bundle, "settings.audio.music.title", "Music"));
        }
        if (sfxLabel != null) {
            sfxLabel.setText(safeGet(bundle, "settings.audio.sfx.title", "Sound Effects"));
        }
        if (languageTitle != null) {
            languageTitle.setText(safeGet(bundle, "settings.language.title", "Language"));
        }
        if (languageSubtitle != null) {
            languageSubtitle.setText(safeGet(bundle, "settings.language.interface", "Interface Language"));
        }
        if (footerLabel != null) {
            footerLabel.setText(safeGet(bundle, "footer.text", "Tic-Tac-Toe v2.0.4 • Build 8821"));
        }
        if (themeTitle != null) {
            themeTitle.setText(safeGet(bundle, "settings.theme.title", "Theme"));
        }

        // تحديث ComboBox اللغة
        if (languageCombo != null) {
            updateLanguageComboSelection(LocalizationManager.getCurrentLocale().getLanguage(), bundle);
        }
    }

    private void updateLanguageComboSelection(String langCode, ResourceBundle bundle) {
        languageCombo.getItems().clear();

        languageCombo.getItems().addAll(
                safeGet(bundle, "language.english", "English"),
                safeGet(bundle, "language.arabic", "Arabic")
        );

        if ("ar".equals(LocalizationManager.getCurrentLocale().getLanguage())) {
            languageCombo.getSelectionModel().select(1); // Arabic
        } else {
            languageCombo.getSelectionModel().select(0); // English
        }
    }
}
