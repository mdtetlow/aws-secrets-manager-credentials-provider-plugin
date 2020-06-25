package io.jenkins.plugins.credentials.secretsmanager.config;

import io.jenkins.plugins.credentials.secretsmanager.Messages;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

public class Filters extends AbstractDescribableImpl<Filters> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Filter secrets received by their AWS tags. This is an implicit Boolean OR: if a secret has ANY matching tag, it will be included.
     */
    private List<Tag> tags;

    @Deprecated
    private transient Tag tag;

    @DataBoundConstructor
    public Filters(List<Tag> tags) {
        this.tags = tags;
    }

    protected Object readResolve() {
        if (tag != null) {
            tags = Collections.singletonList(tag);
            tag = null;
        }

        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @DataBoundSetter
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Extension
    @Symbol("filters")
    @SuppressWarnings("unused")
    public static class DescriptorImpl extends Descriptor<Filters> {
        @Override
        @Nonnull
        public String getDisplayName() {
            return Messages.filters();
        }
    }
}
