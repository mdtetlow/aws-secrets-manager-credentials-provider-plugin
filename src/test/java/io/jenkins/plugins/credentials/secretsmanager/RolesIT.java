package io.jenkins.plugins.credentials.secretsmanager;

import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.CreateSecretResult;
import com.amazonaws.services.secretsmanager.model.Tag;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.credentials.secretsmanager.factory.Type;
import io.jenkins.plugins.credentials.secretsmanager.util.*;
import org.assertj.core.api.Assertions;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RolesIT {

    public final MyJenkinsConfiguredWithCodeRule jenkins = new MyJenkinsConfiguredWithCodeRule();
    public final AWSSecretsManagerRule secretsManager = new AutoErasingAWSSecretsManagerRule();

    @Rule
    public final RuleChain chain = RuleChain
            .outerRule(Rules.awsAccessKey("fake", "fake"))
            .around(jenkins)
            .around(secretsManager);

    @Ignore("Moto does not support cross-account operations")
    public void shouldFetchCredentialsFromMultipleAccounts() {
        Assertions.fail("Implement this test");
    }

    @Ignore("Moto does not support cross-account operations")
    public void shouldThrowExceptionWhenDuplicateSecretNamesPresent() {
        Assertions.fail("Implement this test");
    }

    @Test
    @ConfiguredWithCode(value = "/invalid-roles.yml")
    public void shouldFailWhenRoleNotValid() {
        // Given
        final CreateSecretResult foo = createStringSecret("supersecret");

        // When
        final List<StringCredentials> credentials = jenkins.getCredentials().lookup(StringCredentials.class);

        // Then
        assertThat(credentials)
                .isEmpty();
    }

    private CreateSecretResult createStringSecret(String secretString) {
        final List<Tag> tags = Lists.of(AwsTags.type(Type.string));

        return createSecret(secretString, tags);
    }

    private CreateSecretResult createSecret(String secretString, List<Tag> tags) {
        final CreateSecretRequest request = new CreateSecretRequest()
                .withName(CredentialNames.random())
                .withSecretString(secretString)
                .withTags(tags);

        return secretsManager.getClient().createSecret(request);
    }
}
