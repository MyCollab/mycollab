package com.mycollab.module.project.view.reports;

import com.mycollab.core.MyCollabException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.domain.SimpleStandupReport;
import com.mycollab.module.project.domain.StandupReportStatistic;
import com.mycollab.module.project.domain.StandupReportWithBLOBs;
import com.mycollab.module.project.event.StandUpEvent;
import com.mycollab.module.project.i18n.StandupI18nEnum;
import com.mycollab.module.project.service.StandupReportService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.data.HasValue;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.time.LocalDate;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
class StandupAddWindow extends MWindow implements IEditFormHandler<StandupReportWithBLOBs> {
    private StandupReportService standupReportService;
    private AdvancedEditBeanForm<StandupReportWithBLOBs> editForm;
    private LocalDate onDate;

    StandupAddWindow(StandupReportStatistic standupReportStatistic, LocalDate onDate) {
        this.withModal(true).withClosable(true).withResizable(false).withCenter().withWidth(UIUtils.INSTANCE.getBrowserWidth() + "px")
                .withHeight(UIUtils.INSTANCE.getBrowserHeight() + "px");

        this.onDate = onDate;
        standupReportService = AppContextUtil.getSpringBean(StandupReportService.class);
        SimpleStandupReport report = standupReportService.findStandupReportByDateUser(standupReportStatistic.getProjectId(),
                UserUIContext.getUsername(), onDate, AppUI.getAccountId());
        if (report == null) {
            report = new SimpleStandupReport();
            report.setProjectid(standupReportStatistic.getProjectId());
            report.setSaccountid(AppUI.getAccountId());
            report.setForday(onDate);
            report.setLogby(UserUIContext.getUsername());
        }

        editForm = new AdvancedEditBeanForm<>();
        editForm.setFormLayoutFactory(new FormLayoutFactory());
        editForm.setBeanFormFieldFactory(new EditFormFieldFactory(editForm));
        editForm.setBean(report);
        Component projectLogo = ProjectAssetsUtil.projectLogoComp(standupReportStatistic.getProjectKey(), standupReportStatistic.getProjectId(),
                standupReportStatistic.getProjectAvatarId(), 32);
        ELabel projectLbl = ELabel.h2(standupReportStatistic.getProjectName()).withStyleName(WebThemes.TEXT_ELLIPSIS);
        setContent(new MVerticalLayout(new MHorizontalLayout(projectLogo, projectLbl).expand(projectLbl), editForm));
        editForm.addFormHandler(this);
    }

    @Override
    public void onSave(StandupReportWithBLOBs standupReport) {
        if (standupReport.getId() == null) {
            standupReportService.saveWithSession(standupReport, UserUIContext.getUsername());
        } else {
            standupReportService.updateWithSession(standupReport, UserUIContext.getUsername());
        }
        EventBusFactory.getInstance().post(new StandUpEvent.DisplayStandupInProject(this, standupReport.getProjectid()));
        close();
    }

    @Override
    public void onSaveAndNew(StandupReportWithBLOBs bean) {
        throw new MyCollabException("Not support operation");
    }

    @Override
    public void onCancel() {
        close();
    }

    private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<StandupReportWithBLOBs> {
        private static final long serialVersionUID = 1L;

        EditFormFieldFactory(GenericBeanForm<StandupReportWithBLOBs> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("whatlastday") || propertyId.equals("whattoday") || propertyId.equals("whatproblem")) {
                final RichTextArea richText = new RichTextArea();
                richText.setWidth("100%");
                return richText;
            }
            return null;
        }
    }

    class FormLayoutFactory extends StandupReportFormLayoutFactory {

        FormLayoutFactory() {
            super(UserUIContext.getMessage(StandupI18nEnum.FORM_EDIT_TITLE, UserUIContext.formatDate(onDate)));
        }

        private ComponentContainer createButtonControls() {
            return generateEditFormControls(editForm, true, false, true);
        }

        @Override
        protected ComponentContainer createTopPanel() {
            return this.createButtonControls();
        }
    }
}
