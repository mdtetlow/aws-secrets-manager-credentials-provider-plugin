package io.jenkins.plugins.credentials.secretsmanager.util;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

import java.io.IOException;
import java.util.stream.Collectors;

public class PluginConfigurationForm {

    private final HtmlForm form;

    public PluginConfigurationForm(HtmlForm form) {
        this.form = form;
    }

    public void clear() {
        this.clearEndpointConfiguration();
        this.clearFilters();
        this.clearRoles();
    }

    public void clearFilters() {
        form.getInputByName("_.filters").setChecked(false);
    }

    public void setFilter(String key, String value) {
        form.getInputByName("_.filters").setChecked(true);

        form.getInputByName("_.tag").setChecked(true);
        form.getInputByName("_.key").setValueAttribute(key);
        form.getInputByName("_.value").setValueAttribute(value);
    }

    public void clearRoles() {
        form.getInputByName("_.roles").setChecked(false);
    }

    public void setRole(String arn) {
        form.getInputByName("_.roles").setChecked(true);
        // TODO Use the 'Add' button to test multiple roles
        final HtmlInput input = form
                .getElementsByAttribute("div", "name", "arns").get(0)
                .getOneHtmlElementByAttribute("input", "name", "_.value");
        input.setValueAttribute(arn);
    }

    public void clearEndpointConfiguration() {
        form.getInputByName("_.endpointConfiguration").setChecked(false);
    }

    public void setEndpointConfiguration(String serviceEndpoint, String signingRegion) {
        form.getInputByName("_.endpointConfiguration").setChecked(true);
        form.getInputByName("_.serviceEndpoint").setValueAttribute(serviceEndpoint);
        form.getInputByName("_.signingRegion").setValueAttribute(signingRegion);
    }

    private String getValidateSuccessMessage() {
        return form.getOneHtmlElementByAttribute("div", "class", "ok").getTextContent();
    }

    private String getValidateErrorMessage() {
        return form.getOneHtmlElementByAttribute("div", "class", "error").getTextContent();
    }

    private HtmlButton getValidateButton(String textContent) {
        return form.getByXPath("//span[contains(string(@class),'validate-button')]//button")
                .stream()
                .map(obj -> (HtmlButton) (obj))
                .filter(button -> button.getTextContent().equals(textContent))
                .collect(Collectors.toList())
                .get(0);
    }

    public FormValidationResult clickValidateButton(String textContent) {
        try {
            this.getValidateButton(textContent).click();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            final String success = this.getValidateSuccessMessage();
            return FormValidationResult.success(success);
        } catch (ElementNotFoundException ignored) {
            final String failure = this.getValidateErrorMessage();
            return FormValidationResult.error(failure);
        }
    }
}
