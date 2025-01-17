package com.andersenlab;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SettingsTests {

    @Test
    void givenFileExists_whenReadSettings_thenExpectedSettingsReturned() {
        var settings = Settings.from(Paths.get("src/test/resources/test.toml"));

        assertThat(settings.stateFilePath()).isEqualTo(Paths.get("state.json"));
        assertThat(settings.isGarageSlotAdditionEnabled()).isFalse();
        assertThat(settings.isGarageSlotDeletionEnabled()).isTrue();
    }

    @Test
    void givenFileDoNotExist_whenCreateSettings_thenIllegalArgumentExceptionThrown() {
        var path = Paths.get("non-existent-file");

        assertThatThrownBy(() -> Settings.from(path)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenFileContainsErrors_whenCreateSettings_thenIllegalArgumentExceptionThrown() {
        var path = Paths.get("src/test/resources/unreadable.toml");

        assertThatThrownBy(() -> Settings.from(path)).isInstanceOf(IllegalArgumentException.class);
    }
}