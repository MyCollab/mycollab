package com.mycollab.module.project.view;

import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ViewScope;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.viritin.layouts.MWindow;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public abstract class AbstractProjectAddWindow extends MWindow implements CacheableComponent {
    protected Project project;

    public AbstractProjectAddWindow(Project valuePrj) {
        super(UserUIContext.getMessage(ProjectI18nEnum.NEW));
        this.withWidth("900px").withModal(true).withResizable(false).withCenter();
        this.project = valuePrj;
        if (project.getProjectstatus() == null) {
            project.setProjectstatus(StatusI18nEnum.Open.name());
        }
    }

    public interface FormWizardStep extends WizardStep {
        boolean commit();
    }
}
