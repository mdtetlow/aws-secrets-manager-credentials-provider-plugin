package io.jenkins.plugins.credentials.secretsmanager.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

public abstract class AbstractPluginConfigurationIT {

    protected abstract PluginConfiguration getPluginConfiguration();

    protected abstract void setEndpointConfiguration(String serviceEndpoint, String signingRegion);

    protected abstract void setTagsFilter(Tag tag);

    @Test
    public void shouldHaveDefaultConfiguration() {
        final PluginConfiguration config = getPluginConfiguration();

        assertSoftly(s -> {
            s.assertThat(config.getEndpointConfiguration()).as("Endpoint Configuration").isNull();
            s.assertThat(config.getFilters()).as("Filters").isNull();
        });
    }

    @Test
    public void shouldCustomiseEndpointConfiguration() {
        // Given
        setEndpointConfiguration("http://localhost:4584", "us-east-1");

        // When
        final PluginConfiguration config = getPluginConfiguration();

        // Then
        assertSoftly(s -> {
            s.assertThat(config.getEndpointConfiguration().getServiceEndpoint()).as("Service Endpoint").isEqualTo("http://localhost:4584");
            s.assertThat(config.getEndpointConfiguration().getSigningRegion()).as("Signing Region").isEqualTo("us-east-1");
        });
    }

    @Test
    public void shouldCustomiseTagsFilter() {
        // Given
        setTagsFilter(new Tag("product", "foo"));

        // When
        final PluginConfiguration config = getPluginConfiguration();

        // Then
        assertThat(config.getFilters().getTags())
                .extracting("key", "value")
                .containsOnly(tuple("product", "foo"));
    }
}
