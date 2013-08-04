package org.elearning.account.register;

import org.elearning.account.register.ui.UIRegisterForm;
import org.elearning.account.register.ui.UIRegisterEditMode;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig(
        lifecycle = UIApplicationLifecycle.class,
        template = "app:/templates/account/register/UIRegisterPortlet.gtmpl"
        )

public class UIRegisterPortlet extends UIPortletApplication {

    public UIRegisterPortlet() throws Exception {
        addChild(UIRegisterForm.class, null, null);
        addChild(UIRegisterEditMode.class, null, null).setRendered(false);
    }
}
