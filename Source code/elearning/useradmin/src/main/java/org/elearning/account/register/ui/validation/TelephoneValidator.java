package org.elearning.account.register.ui.validation;

import org.exoplatform.webui.form.UIFormInput;
import org.exoplatform.webui.form.validator.AbstractValidator;

public class TelephoneValidator extends AbstractValidator {

    @Override
    protected String getMessageLocalizationKey() {
        return "TelephoneValidator.msg.Invalid";
    }

    @Override
    protected boolean isValid(String value, UIFormInput uiInput) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
