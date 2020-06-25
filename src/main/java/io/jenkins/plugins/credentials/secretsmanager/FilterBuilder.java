package io.jenkins.plugins.credentials.secretsmanager;

import com.amazonaws.services.secretsmanager.model.SecretListEntry;
import com.amazonaws.services.secretsmanager.model.Tag;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

class FilterBuilder {

    private Predicate<List<Tag>> tagPredicates;

    FilterBuilder or(String key) {
        return or(key, null);
    }

    FilterBuilder or(String key, String value) {
        final Predicate<List<Tag>> pred = tags -> tags.stream().anyMatch(matches(key, value));
        if (tagPredicates == null) {
            tagPredicates = pred;
        } else {
            tagPredicates = tagPredicates.or(pred);
        }
        return this;
    }

    private Predicate<List<Tag>> predicateOrTrue() {
        return Optional.ofNullable(tagPredicates).orElse(tags -> true);
    }

    Predicate<SecretListEntry> build() {
        final Predicate<List<Tag>> pred = predicateOrTrue();
        return secretListEntry -> pred.test(secretListEntry.getTags());
    }

    private static Predicate<Tag> matches(String key, @Nullable String value) {
        final Predicate<Tag> p;
        if (value == null) {
            p = tag -> tag.getKey().equals(key);
        } else {
            p = tag -> tag.getKey().equals(key) && tag.getValue().equals(value);
        }
        return p;
    }
}
