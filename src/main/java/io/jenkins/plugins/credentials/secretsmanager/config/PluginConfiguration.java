package io.jenkins.plugins.credentials.secretsmanager.config;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
@Symbol("awsCredentialsProvider")
public class PluginConfiguration extends GlobalConfiguration {

    /**
     * The AWS Secrets Manager endpoint configuration. If this is null, the default will be used. If
     * this is specified, the user's override will be used.
     */
    private EndpointConfiguration endpointConfiguration;

    private Filters filters;

    /**
     * The IAM role ARNs to assume. For multi-account secrets retrieval.
     */
    private Roles roles;

    public PluginConfiguration() {
        load();
    }

    public static PluginConfiguration getInstance() {
        return all().get(PluginConfiguration.class);
    }

    public EndpointConfiguration getEndpointConfiguration() {
        return endpointConfiguration;
    }

    @DataBoundSetter
    @SuppressWarnings("unused")
    public void setEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
        this.endpointConfiguration = endpointConfiguration;
        save();
    }

    public Filters getFilters() {
        return filters;
    }

    @DataBoundSetter
    @SuppressWarnings("unused")
    public void setFilters(Filters filters) {
        this.filters = filters;
        save();
    }

    public Roles getRoles() {
        return roles;
    }

    @DataBoundSetter
    public void setRoles(Roles roles) {
        this.roles = roles;
        save();
    }

    @Override
    public synchronized boolean configure(StaplerRequest req, JSONObject json) {
        // This method is unnecessary, except to apply the following workaround.
        // Workaround: Set any optional struct fields to null before binding configuration.
        // https://groups.google.com/forum/#!msg/jenkinsci-dev/MuRJ-yPRRoo/AvoPZAgbAAAJ
        this.endpointConfiguration = null;
        this.filters = null;
        this.roles = null;

        req.bindJSON(this, json);
        save();
        return true;
    }
}
