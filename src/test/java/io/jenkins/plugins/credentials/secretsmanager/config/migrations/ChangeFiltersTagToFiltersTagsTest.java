package io.jenkins.plugins.credentials.secretsmanager.config.migrations;

import io.jenkins.plugins.credentials.secretsmanager.config.PluginConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ChangeFiltersTagToFiltersTagsTest extends MigrationTest {

    @Override
    public void change(PluginConfiguration config) {
        assertThat(config.getFilters().getTags())
                .extracting("key", "value")
                .containsOnly(tuple("foo", "bar"));
    }
}
