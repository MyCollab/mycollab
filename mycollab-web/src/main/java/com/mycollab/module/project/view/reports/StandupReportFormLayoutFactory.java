package com.mycollab.module.project.view.reports;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.StandupI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.AddViewLayout;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class StandupReportFormLayoutFactory extends AbstractFormLayoutFactory {

    private StandupCustomField whatTodayField;
    private StandupCustomField whatYesterdayField;
    private StandupCustomField whatProblemField;

    private final String title;

    StandupReportFormLayoutFactory(final String title) {
        this.title = title;
    }

    @Override
    public AbstractComponent getLayout() {
        AddViewLayout reportAddLayout = new AddViewLayout(title, ProjectAssetsManager.getAsset(ProjectTypeConstants.STANDUP));
        reportAddLayout.addHeaderRight(this.createTopPanel());

        MHorizontalLayout mainLayout = new MHorizontalLayout().withFullWidth();
        final MVerticalLayout layoutField = new MVerticalLayout().withMargin(new MarginInfo(false, false, true,
                false)).withFullWidth();

        final ELabel whatYesterdayLbl = ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_LASTDAY));
        layoutField.addComponent(whatYesterdayLbl);
        whatYesterdayField = new StandupCustomField();
        layoutField.addComponent(whatYesterdayField);

        final ELabel whatTodayLbl = ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_TODAY));
        layoutField.with(new Label(""), whatTodayLbl);
        whatTodayField = new StandupCustomField();
        layoutField.addComponent(whatTodayField);

        final ELabel roadblockLbl = ELabel.h3(UserUIContext.getMessage(StandupI18nEnum.STANDUP_ISSUE));
        roadblockLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
        layoutField.with(new Label(""), roadblockLbl);
        whatProblemField = new StandupCustomField();
        layoutField.addComponent(whatProblemField);

        mainLayout.addComponent(layoutField);
        mainLayout.setExpandRatio(layoutField, 2.0f);

        MVerticalLayout instructionLayout = new MVerticalLayout(ELabel.html(UserUIContext.getMessage(StandupI18nEnum.HINT1_MSG)).withFullWidth(),
                ELabel.html(UserUIContext.getMessage(StandupI18nEnum.HINT2_MG)).withFullWidth()).withStyleName("instruction-box").withWidth("300px");

        mainLayout.addComponent(instructionLayout);
        mainLayout.setExpandRatio(instructionLayout, 1.0f);
        mainLayout.setComponentAlignment(instructionLayout, Alignment.TOP_CENTER);

        reportAddLayout.addBody(mainLayout);
        return reportAddLayout;
    }

    @Override
    protected HasValue<?> onAttachField(Object propertyId, final HasValue<?> field) {
        if (propertyId.equals("whatlastday")) {
            whatYesterdayField.setContentComp((Component) field);
        } else if (propertyId.equals("whattoday")) {
            whatTodayField.setContentComp((Component) field);
        } else if (propertyId.equals("whatproblem")) {
            whatProblemField.setContentComp((Component) field);
        }
        return field;
    }

    protected abstract ComponentContainer createTopPanel();

    private static class StandupCustomField extends CustomComponent {
        private static final long serialVersionUID = 1L;

        void setContentComp(Component comp) {
            setCompositionRoot(comp);
        }
    }
}
