package io.jenkins.plugins.credentials.secretsmanager.config;

import com.google.common.collect.Sets;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import io.jenkins.plugins.casc.Attribute;
import io.jenkins.plugins.casc.BaseConfigurator;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.RootElementConfigurator;
import io.jenkins.plugins.casc.impl.attributes.MultivaluedAttribute;
import io.jenkins.plugins.casc.model.CNode;
import io.jenkins.plugins.casc.model.Mapping;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Extension(optional = true, ordinal = 2)
@Restricted(NoExternalUse.class)
@SuppressWarnings("unused")
public class PluginRootConfigurator extends BaseConfigurator<PluginConfiguration>
        implements RootElementConfigurator<PluginConfiguration> {

    // NOTE: this MUST be the same as the PluginConfiguration @Symbol annotation
    @Override
    @NonNull
    public String getName() {
        return "awsCredentialsProvider";
    }

    @Override
    public Class<PluginConfiguration> getTarget() {
        return PluginConfiguration.class;
    }

    @Override
    public PluginConfiguration getTargetComponent(ConfigurationContext context) {
        return PluginConfiguration.all().get(PluginConfiguration.class);
    }

    @Override
    public PluginConfiguration instance(Mapping mapping, ConfigurationContext context) {
        return getTargetComponent(context);
    }

    @Override
    @NonNull
    public Set<Attribute<PluginConfiguration,?>> describe() {
        return Sets.newHashSet(
                new Attribute<PluginConfiguration, EndpointConfiguration>("endpointConfiguration", EndpointConfiguration.class)
                        .setter(PluginConfiguration::setEndpointConfiguration),
                new Attribute<PluginConfiguration, Filters>("filters", Filters.class)
                        .setter(PluginConfiguration::setFilters),
                new MultivaluedAttribute<PluginConfiguration, String>("roles", String.class)
                        .setter((target, roleArns) -> {
                            final List<ARN> arns = roleArns.stream().map(ARN::new).collect(Collectors.toList());
                            target.setRoles(new Roles(arns));
                        }));
    }

    @CheckForNull
    @Override
    public CNode describe(PluginConfiguration instance, ConfigurationContext context) throws Exception {
        Mapping mapping = new Mapping();
        for (Attribute<PluginConfiguration, ?> attribute : describe()) {
            mapping.put(attribute.getName(), attribute.describe(instance, context));
        }
        return mapping;
    }

}