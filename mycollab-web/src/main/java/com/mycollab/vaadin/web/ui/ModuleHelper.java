package com.mycollab.vaadin.web.ui;

import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.module.file.view.IFileModule;
import com.mycollab.module.project.view.ProjectModule;
import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.vaadin.mvp.IModule;
import com.mycollab.vaadin.ui.MyCollabSession;

import static com.mycollab.vaadin.ui.MyCollabSession.CURRENT_MODULE;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ModuleHelper {

    public static void setCurrentModule(IModule module) {
        MyCollabSession.putCurrentUIVariable(MyCollabSession.CURRENT_MODULE, module);
    }

    public static IModule getCurrentModule() {
        return (IModule) MyCollabSession.getCurrentUIVariable(MyCollabSession.CURRENT_MODULE);
    }

    public static boolean isCurrentProjectModule() {
        IModule module = getCurrentModule();
        return (module != null) && (module instanceof ProjectModule);
    }

    public static boolean isCurrentCrmModule() {
        IModule module = getCurrentModule();
        return (module != null) && (module instanceof CrmModule);
    }

    public static boolean isCurrentFileModule() {
        IModule module = getCurrentModule();
        return (module != null) && (IFileModule.class.isAssignableFrom(module.getClass()));
    }

    public static boolean isCurrentAccountModule() {
        IModule module = getCurrentModule();
        return (module != null) && (module instanceof AccountModule);
    }
}
