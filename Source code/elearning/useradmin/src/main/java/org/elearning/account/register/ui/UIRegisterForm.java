package org.elearning.account.register.ui;

import java.util.ArrayList;
import java.util.List;

import nl.captcha.Captcha;

import org.exoplatform.portal.registration.PostRegistrationService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.exception.MessageException;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputWithActions;
import org.exoplatform.webui.form.UIFormInputWithActions.ActionData;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.Validator;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 *
 * @author <a href="mailto:hoang281283@gmail.com">Minh Hoang TO</a>
 * @version $Id$
 *
 */
@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "system:/groovy/webui/form/UIFormWithTitle.gtmpl", events = {
    @EventConfig(listeners = UIRegisterForm.SubscribeActionListener.class),
    @EventConfig(listeners = UIRegisterForm.ResetActionListener.class, phase = Phase.DECODE),
    @EventConfig(name = UIRegisterForm.CheckUsernameAvailability.LISTENER_NAME, listeners = UIRegisterForm.CheckUsernameAvailability.class, phase = Phase.DECODE)})
public class UIRegisterForm extends UIForm {

    private static final String[] ACTIONS = {"Subscribe", "Reset"};

    static final String ATTR_USER = "UIRegisterForm$User";

    public UIRegisterForm() throws Exception {
        UIFormInputWithActions registerInput = new UIRegisterInputSet("RegisterInputSet");
        // Set actions on registerInput 's User Name field
        List<ActionData> fieldActions = new ArrayList<ActionData>();
        ActionData checkAvailable = new ActionData();
        checkAvailable.setActionListener(CheckUsernameAvailability.LISTENER_NAME);
        checkAvailable.setActionName(CheckUsernameAvailability.LISTENER_NAME);
        checkAvailable.setActionType(ActionData.TYPE_ICON);
        checkAvailable.setCssIconClass("SearchIcon");
        fieldActions.add(checkAvailable);
        registerInput.setActionField(UIRegisterInputSet.USER_NAME, fieldActions);

        addUIFormInput(registerInput);
        setActions(ACTIONS);
    }

    private void resetInput() {
        getChild(UIRegisterInputSet.class).reset();
    }

    @Override
    public void processAction(WebuiRequestContext context) throws Exception {
        super.processAction(context);

        if (context.getProcessRender()) {
            // Invalidate the capcha
            if (context instanceof PortletRequestContext) {
                PortletRequestContext prc = (PortletRequestContext) context;
                prc.getRequest().getPortletSession().removeAttribute(Captcha.NAME);
            }
            context.addUIComponentToUpdateByAjax(getChild(UIRegisterInputSet.class));
        }
    }

    public static class SubscribeActionListener extends EventListener<UIRegisterForm> {

        @Override
        public void execute(Event<UIRegisterForm> event) throws Exception {
            UIRegisterForm registerForm = event.getSource();
            OrganizationService orgService = registerForm.getApplicationComponent(OrganizationService.class);
            UserHandler userHandler = orgService.getUserHandler();
            UserProfileHandler userProfileHandler = orgService.getUserProfileHandler();
            WebuiRequestContext context = WebuiRequestContext.getCurrentInstance();
            UIRegisterInputSet registerInput = registerForm.getChild(UIRegisterInputSet.class);

            if (registerInput.save(userHandler, userProfileHandler, context)) {
                // TODO: Add Account Activating feature
                UIApplication uiApp = context.getUIApplication();
                uiApp.addMessage(new ApplicationMessage("UIRegisterForm.registerWithSuccess.message", null));

                // Send mail to administrator after successful registration of user
                PostRegistrationService postRegistrationService = uiApp.getApplicationComponent(PostRegistrationService.class);
                User user = (User) context.getAttribute(ATTR_USER);
                postRegistrationService.sendMailAfterSuccessfulRegistration(user);
            }

            // Invalidate the capcha
            if (context instanceof PortletRequestContext) {
                PortletRequestContext prc = (PortletRequestContext) context;
                prc.getRequest().getPortletSession().removeAttribute(Captcha.NAME);
            }
        }
    }

    public static class CheckUsernameAvailability extends EventListener<UIRegisterForm> {

        /**
         * .
         */
        private final Logger log = LoggerFactory.getLogger(CheckUsernameAvailability.class);

        static final String LISTENER_NAME = "CheckUsernameAvailability";

        @Override
        public void execute(Event<UIRegisterForm> event) throws Exception {
            UIRegisterForm registerForm = event.getSource();
            OrganizationService orgService = registerForm.getApplicationComponent(OrganizationService.class);
            UIRegisterInputSet registerInput = registerForm.getChild(UIRegisterInputSet.class);
            UIFormStringInput userNameInput = registerInput.getUIStringInput(UIRegisterInputSet.USER_NAME);
            List<Validator> validators = userNameInput.getValidators();
            for (Validator validator : validators) {
                try {
                    validator.validate(userNameInput);
                } catch (MessageException e) {
                    event.getRequestContext().getUIApplication().addMessage(e.getDetailMessage());
                    return;
                }
            }

            String typedUsername = userNameInput.getValue();
            WebuiRequestContext context = WebuiRequestContext.getCurrentInstance();
            UIApplication uiApp = context.getUIApplication();
            if (usernameIsUsed(typedUsername, orgService)) {
                uiApp.addMessage(new ApplicationMessage("UIAccountInputSet.msg.user-exist", new String[]{typedUsername},
                        ApplicationMessage.WARNING));
            } else {
                uiApp.addMessage(new ApplicationMessage("UIAccountInputSet.msg.user-not-exist", new String[]{typedUsername},
                        ApplicationMessage.INFO));
            }
        }

        private boolean usernameIsUsed(String username, OrganizationService orgService) {
            UserHandler userHandler = orgService.getUserHandler();
            try {
                if (userHandler.findUserByName(username) != null) {
                    return true;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            return false;
        }
    }

    public static class ResetActionListener extends EventListener<UIRegisterForm> {

        @Override
        public void execute(Event<UIRegisterForm> event) throws Exception {
            UIRegisterForm registerForm = event.getSource();
            registerForm.resetInput();
        }
    }
}
