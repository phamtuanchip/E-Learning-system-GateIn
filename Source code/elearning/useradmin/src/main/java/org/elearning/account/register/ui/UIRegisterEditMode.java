package org.elearning.account.register.ui;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import org.elearning.account.register.UIRegisterPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.CaptchaValidator;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;

@ComponentConfig(
        lifecycle = UIFormLifecycle.class,
        template = "system:/groovy/webui/form/UIFormWithTitle.gtmpl",
        events = {
            @EventConfig(listeners = UIRegisterEditMode.SaveActionListener.class)}
        )

@Serialized
public class UIRegisterEditMode extends UIForm {

    public static final String USE_CAPTCHA = "useCaptcha";

    public UIRegisterEditMode() {
        PortletRequestContext pcontext = (PortletRequestContext) WebuiRequestContext.getCurrentInstance();
        PortletPreferences pref = pcontext.getRequest().getPreferences();
        boolean useCaptcha = Boolean.parseBoolean(pref.getValue(USE_CAPTCHA, "true"));
        addUIFormInput(new UIFormCheckBoxInput<Boolean>(USE_CAPTCHA, USE_CAPTCHA, useCaptcha).setValue(useCaptcha));
    }

    public static class SaveActionListener extends EventListener<UIRegisterEditMode> {

        @Override
        public void execute(Event<UIRegisterEditMode> event) throws Exception {
            // TODO Auto-generated method stub
            UIRegisterEditMode uiForm = event.getSource();
            boolean useCaptcha = uiForm.getUIFormCheckBoxInput(USE_CAPTCHA).isChecked();
            PortletRequestContext pcontext = (PortletRequestContext) WebuiRequestContext.getCurrentInstance();
            PortletPreferences pref = pcontext.getRequest().getPreferences();
            pref.setValue(USE_CAPTCHA, Boolean.toString(useCaptcha));
            pref.store();

            // Show/hide the captcha input in UIRegisterInputSet
            UIRegisterPortlet registerPortlet = uiForm.getParent();
            UIRegisterInputSet registerInputSet = registerPortlet.findFirstComponentOfType(UIRegisterInputSet.class);

            if (useCaptcha) {
                if (!registerInputSet.getCaptchaInputAvailability()) {
                    registerInputSet.addUIFormInput(new UICaptcha(UIRegisterInputSet.CAPTCHA, UIRegisterInputSet.CAPTCHA, null)
                            .addValidator(MandatoryValidator.class).addValidator(CaptchaValidator.class));
                    registerInputSet.setCaptchaInputAvailability(true);
                }
            } else {
                if (registerInputSet.getCaptchaInputAvailability()) {
                    registerInputSet.removeChildById(UIRegisterInputSet.CAPTCHA);
                    registerInputSet.setCaptchaInputAvailability(false);
                }
            }

            UIPortalApplication portalApp = Util.getUIPortalApplication();
            if (portalApp.getModeState() == UIPortalApplication.NORMAL_MODE) {
                pcontext.setApplicationMode(PortletMode.VIEW);
            }

        }

    }
}
