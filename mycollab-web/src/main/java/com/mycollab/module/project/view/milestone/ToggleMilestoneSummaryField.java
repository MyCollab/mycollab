package com.mycollab.module.project.view.milestone;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.components.BlockRowRender;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.CssCheckBox;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleMilestoneSummaryField extends AbstractToggleSummaryField {
    private boolean isRead = true;
    private SimpleMilestone milestone;
    private int maxLength;
    private CssCheckBox toggleStatusSelect;

    ToggleMilestoneSummaryField(final SimpleMilestone milestone, boolean toggleStatusSupport, boolean isDeleteSupport) {
        this(milestone, Integer.MAX_VALUE, toggleStatusSupport, isDeleteSupport);
    }

    ToggleMilestoneSummaryField(final SimpleMilestone milestone, int maxLength, boolean toggleStatusSupport, boolean isDeleteSupport) {
        this.milestone = milestone;
        this.maxLength = maxLength;
        this.setWidth("100%");
        this.addStyleName("editable-field");
        if (toggleStatusSupport && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
            toggleStatusSelect = new CssCheckBox();
            toggleStatusSelect.setSimpleMode(true);
            toggleStatusSelect.setValue(milestone.isCompleted());
            this.addComponent(toggleStatusSelect);
            this.addComponent(ELabel.EMPTY_SPACE());
            displayTooltip();

            toggleStatusSelect.addValueChangeListener(valueChangeEvent -> {
                if (milestone.isCompleted()) {
                    milestone.setStatus(MilestoneStatus.InProgress.name());
                    titleLinkLbl.removeStyleName(WebThemes.LINK_COMPLETED);
                } else {
                    milestone.setStatus(MilestoneStatus.Closed.name());
                    titleLinkLbl.addStyleName(WebThemes.LINK_COMPLETED);
                }
                displayTooltip();
                MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
                milestoneService.updateSelectiveWithSession(milestone, UserUIContext.getUsername());
                ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
                searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.RISK,
                        ProjectTypeConstants.TASK));
                searchCriteria.setMilestoneId(NumberSearchField.equal(milestone.getId()));
                searchCriteria.setIsOpenned(new SearchField());
                ProjectTicketService genericTaskService = AppContextUtil.getSpringBean(ProjectTicketService.class);
                int openAssignmentsCount = genericTaskService.getTotalCount(searchCriteria);
                if (openAssignmentsCount > 0) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.OPT_QUESTION, AppUI.getSiteName()),
                            UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_CLOSE_SUB_ASSIGNMENTS),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    genericTaskService.closeSubAssignmentOfMilestone(milestone.getId());
                                }
                            });
                }
            });
        }

        titleLinkLbl = ELabel.h3(buildMilestoneLink()).withStyleName(UIConstants.LABEL_WORD_WRAP).withWidthUndefined();
        this.addComponent(titleLinkLbl);
        buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(false, false, false, true)).withStyleName("toggle");
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
            MButton instantEditBtn = new MButton("", clickEvent -> {
                if (isRead) {
                    ToggleMilestoneSummaryField.this.removeComponent(titleLinkLbl);
                    ToggleMilestoneSummaryField.this.removeComponent(buttonControls);
                    final TextField editField = new TextField();
                    editField.setValue(milestone.getName());
                    editField.setWidth("100%");
                    editField.focus();
                    ToggleMilestoneSummaryField.this.addComponent(editField);
                    ToggleMilestoneSummaryField.this.removeStyleName("editable-field");
                    editField.addValueChangeListener(valueChangeEvent -> updateFieldValue(editField));
                    editField.addBlurListener(blurEvent -> updateFieldValue(editField));
                    isRead = !isRead;
                }
            }).withDescription(UserUIContext.getMessage(MilestoneI18nEnum.OPT_EDIT_PHASE_NAME))
                    .withIcon(FontAwesome.EDIT).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
            buttonControls.with(instantEditBtn);
        }
        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MILESTONES)) {
            MButton removeBtn = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                AppContextUtil.getSpringBean(MilestoneService.class).removeWithSession(milestone,
                                        UserUIContext.getUsername(), AppUI.getAccountId());
                                BlockRowRender rowRenderer = UIUtils.getRoot(ToggleMilestoneSummaryField.this, BlockRowRender.class);
                                if (rowRenderer != null) {
                                    rowRenderer.selfRemoved();
                                }
                                EventBusFactory.getInstance().post(new MilestoneEvent.MilestoneDeleted(this, milestone.getId()));
                            }
                        });
            }).withIcon(FontAwesome.TRASH).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
            buttonControls.with(removeBtn);
        }
        if (buttonControls.getComponentCount() > 0) {
            this.addComponent(buttonControls);
        }
    }

    private void displayTooltip() {
        if (milestone.isCompleted()) {
            toggleStatusSelect.setDescription(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_MARK_INCOMPLETE));
        } else {
            toggleStatusSelect.setDescription(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_MARK_COMPLETE));
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(titleLinkLbl);
        addComponent(buttonControls);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(milestone.getName())) {
            milestone.setName(newValue);
            titleLinkLbl.setValue(buildMilestoneLink());
            MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
            milestoneService.updateSelectiveWithSession(BeanUtility.deepClone(milestone), UserUIContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildMilestoneLink() {
        A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink(milestone.getProjectid(), milestone.getId()));
        milestoneLink.appendText(StringUtils.trim(milestone.getName(), maxLength, true));

        Div milestoneDiv = new Div().appendChild(milestoneLink);
        if (milestone.isOverdue()) {
            milestoneLink.setCSSClass("overdue");
            milestoneDiv.appendChild(new Span().setCSSClass(UIConstants.META_INFO).appendText(" - " + UserUIContext
                    .getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, UserUIContext.formatDuration(milestone.getEnddate()))));
        } else if (MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
            milestoneLink.setCSSClass("completed");
        }
        return milestoneDiv.write();
    }
}
