package io.jenkins.plugins.credentials.secretsmanager;

import com.amazonaws.services.secretsmanager.model.SecretListEntry;
import com.amazonaws.services.secretsmanager.model.Tag;
import org.junit.Test;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterBuilderTest {

    @Test
    public void shouldNotFilterByDefault() {
        final SecretListEntry foo = createSecretWithTag("foo", "1");
        final SecretListEntry bar = createSecretWithTag("bar", "1");

        final Predicate<SecretListEntry> filter = new FilterBuilder().build();

        assertThat(Stream.of(foo, bar).filter(filter))
                .contains(foo, bar);
    }

    @Test
    public void shouldFilterByTagKey() {
        final SecretListEntry foo = createSecretWithTag("foo");
        final SecretListEntry foo_1 = createSecretWithTag("foo", "1");
        final SecretListEntry bar = createSecretWithTag("bar");

        final Predicate<SecretListEntry> filter = new FilterBuilder()
                .or("foo")
                .build();

        assertThat(Stream.of(foo, foo_1, bar).filter(filter))
                .contains(foo, foo_1)
                .doesNotContain(bar);
    }

    @Test
    public void shouldFilterByTag() {
        final SecretListEntry foo_1 = createSecretWithTag("foo", "1");
        final SecretListEntry foo_2 = createSecretWithTag("foo", "2");

        final Predicate<SecretListEntry> filter = new FilterBuilder()
                .or("foo", "1")
                .build();

        assertThat(Stream.of(foo_1, foo_2).filter(filter))
                .contains(foo_1)
                .doesNotContain(foo_2);
    }

    @Test
    public void shouldFilterByTags() {
        final SecretListEntry foo_1 = createSecretWithTag("foo", "1");
        final SecretListEntry foo_2 = createSecretWithTag("foo", "2");
        final SecretListEntry bar_1 = createSecretWithTag("bar", "1");
        final SecretListEntry baz_1 = createSecretWithTag("baz", "1");

        final Predicate<SecretListEntry> filter = new FilterBuilder()
                .or("foo", "1")
                .or("foo", "2")
                .or("bar", "1")
                .build();

        assertThat(Stream.of(foo_1, foo_2, bar_1, baz_1).filter(filter))
                .contains(foo_1, foo_2, bar_1)
                .doesNotContain(baz_1);
    }

    private static SecretListEntry createSecretWithTag(String key) {
        return new SecretListEntry()
                .withName(randomSecretName())
                .withTags(new Tag().withKey(key));
    }

    private static SecretListEntry createSecretWithTag(String key, String value) {
        return new SecretListEntry()
                .withName(randomSecretName())
                .withTags(new Tag().withKey(key).withValue(value));
    }

    private static String randomSecretName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
