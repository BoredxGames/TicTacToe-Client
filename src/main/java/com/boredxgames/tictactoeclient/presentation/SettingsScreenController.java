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
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
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

    @FXML
    private ProgressBar musicProgressBar, sfxProgressBar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ===== Music Toggle & Slider Initialization =====
musicToggle.setSelected(AudioManager.isMusicEnabled());
musicSlider.setValue(AudioManager.getMusicVolume() * 100);
musicProgressBar.prefWidthProperty().bind(musicSlider.widthProperty());
musicProgressBar.setProgress(AudioManager.getMusicVolume());

musicSlider.disableProperty().bind(musicToggle.selectedProperty().not());

musicToggle.selectedProperty().addListener((obs, oldVal, isOn) -> {
    AudioManager.setMusicEnabled(isOn);
    if (isOn && musicSlider.getValue() == 0) {
        musicSlider.setValue(50); // قيمة افتراضية إذا كانت 0
    } else if (!isOn) {
        musicSlider.setValue(0);
    }
});

musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
    double vol = newVal.doubleValue() / 100.0;
    AudioManager.setMusicVolume(vol);

    if (musicProgressBar != null) {
        musicProgressBar.setProgress(vol);
    }

    // Synchronize toggle with slider
    if (vol == 0 && AudioManager.isMusicEnabled()) {
        AudioManager.setMusicEnabled(false);
        musicToggle.setSelected(false);
    } else if (vol > 0 && !AudioManager.isMusicEnabled()) {
        AudioManager.setMusicEnabled(true);
        musicToggle.setSelected(true);
    }
});

// ===== SFX Toggle & Slider Initialization =====
sfxToggle.setSelected(AudioManager.isSfxEnabled());
sfxSlider.setValue(AudioManager.getSfxVolume() * 100);
sfxProgressBar.prefWidthProperty().bind(sfxSlider.widthProperty());
sfxProgressBar.setProgress(AudioManager.getSfxVolume());

sfxSlider.disableProperty().bind(sfxToggle.selectedProperty().not());

sfxToggle.selectedProperty().addListener((obs, oldVal, isOn) -> {
    AudioManager.setSfxEnabled(isOn);
    if (isOn && sfxSlider.getValue() == 0) {
        sfxSlider.setValue(50);
    } else if (!isOn) {
        sfxSlider.setValue(0);
    }
});

sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
    double vol = newVal.doubleValue() / 100.0;
    AudioManager.setSfxVolume(vol);

    if (sfxProgressBar != null) {
        sfxProgressBar.setProgress(vol);
    }

    // Synchronize toggle with slider
    if (vol == 0 && AudioManager.isSfxEnabled()) {
        AudioManager.setSfxEnabled(false);
        sfxToggle.setSelected(false);
    } else if (vol > 0 && !AudioManager.isSfxEnabled()) {
        AudioManager.setSfxEnabled(true);
        sfxToggle.setSelected(true);
    }
});

        // ===== Language Listener =====
        languageCombo.setOnAction(e -> {
            int selectedIndex = languageCombo.getSelectionModel().getSelectedIndex();
            Languages currentLang = LocalizationManager.getCurrentLocale().getLanguage().equals("ar") ? Languages.ARABIC : Languages.ENGLISH;

            if (selectedIndex == 0 && currentLang != Languages.ENGLISH) {
                LocalizationManager.changeLocale(Languages.ENGLISH);
                updateTexts();
            } else if (selectedIndex == 1 && currentLang != Languages.ARABIC) {
                LocalizationManager.changeLocale(Languages.ARABIC);
                updateTexts();
            }
        });

        languageCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    if (isSelected()) {
                        setStyle("-fx-background-color: #00FFFF; -fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight;");
                    }
                }
            }
        });

        languageCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold;");
                }
            }
        });

        // ===== Theme toggle group =====
        ToggleGroup themeGroup = new ToggleGroup();
        blueTheme.setToggleGroup(themeGroup);
        pinkTheme.setToggleGroup(themeGroup);
        greenTheme.setToggleGroup(themeGroup);

        // ===== Set selected based on current theme =====
        Theme current = ThemeManager.getTheme();
        if (current == Theme.NEON_BLUE) {
            blueTheme.setSelected(true);
        } else if (current == Theme.CYBER_PINK) {
            pinkTheme.setSelected(true);
        } else if (current == Theme.TOXIC_GREEN) {
            greenTheme.setSelected(true);
        }

        // ===== Listener to change Theme =====
        themeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == blueTheme) {
                ThemeManager.setTheme(Theme.NEON_BLUE);
            } else if (newToggle == pinkTheme) {
                ThemeManager.setTheme(Theme.CYBER_PINK);
            } else if (newToggle == greenTheme) {
                ThemeManager.setTheme(Theme.TOXIC_GREEN);
            }
        });

        // ===== Disable sliders when unchecked =====
        musicSlider.disableProperty().bind(musicToggle.selectedProperty().not());
        sfxSlider.disableProperty().bind(sfxToggle.selectedProperty().not());

        // ===== Back button =====
        backBtn.setOnAction(e -> {
            System.out.println("Back pressed");
            NavigationManager.pop();
        });

        // ===== Initialize texts =====
        try {
            updateTexts();
        } catch (Exception e) {
            System.out.println("Localization failed: " + e.getMessage());
        }

    }
    // ===== Helper Methods =====

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

        if ("ar".equals(langCode)) {
            languageCombo.getSelectionModel().select(1); // Arabic
        } else {
            languageCombo.getSelectionModel().select(0); // English
        }
    }
}
