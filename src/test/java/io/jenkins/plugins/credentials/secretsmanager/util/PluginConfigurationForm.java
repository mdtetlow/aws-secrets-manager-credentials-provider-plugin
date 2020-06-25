package io.jenkins.plugins.credentials.secretsmanager.util;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import io.jenkins.plugins.credentials.secretsmanager.config.Tag;

import java.io.IOException;
import java.util.List;

public class PluginConfigurationForm {

    private final HtmlForm form;

    public PluginConfigurationForm(HtmlForm form) {
        this.form = form;
    }

    public void clear() {
        this.clearEndpointConfiguration();
        this.clearFilters();
    }

    public void clearFilters() {
        form.getInputByName("_.filters").setChecked(false);
    }

    public void setTagsFilter(Tag tag) {
        // TODO support multiple tags
        form.getInputByName("_.filters").setChecked(true);

        clickRepeatableAddButton("Tags");
        form.getInputByName("_.key").setValueAttribute(tag.getKey());
        form.getInputByName("_.value").setValueAttribute(tag.getValue());
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

    private void clickRepeatableAddButton(String settingName) {
        form.getByXPath(String.format("//td[contains(text(), '%s')]/following-sibling::td[@class='setting-main']//span[contains(string(@class),'repeatable-add')]//button[contains(text(), 'Add')]", settingName))
                .stream()
                .findFirst()
                .ifPresent(button -> clickOrThrowException((HtmlButton) button));
    }

    public FormValidationResult clickValidateButton(String textContent) {
        form.getByXPath(String.format("//span[contains(string(@class),'validate-button')]//button[contains(text(), '%s')]", textContent))
                .stream()
                .findFirst()
                .ifPresent(button -> clickOrThrowException((HtmlButton) button));

        try {
            final String success = this.getValidateSuccessMessage();
            return FormValidationResult.success(success);
        } catch (ElementNotFoundException ignored) {
            final String failure = this.getValidateErrorMessage();
            return FormValidationResult.error(failure);
        }
    }

    private static void clickOrThrowException(HtmlButton button) {
        try {
            button.click();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
