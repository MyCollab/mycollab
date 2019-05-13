package com.mycollab.module.project.view.ticket;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IgnoreBindingField;
import com.mycollab.vaadin.ui.VerticalRemoveInlineComponentMarker;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SplitButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 7.0.3
 */
public class SubTicketsComp extends IgnoreBindingField {
    private static final long serialVersionUID = 1L;

        private ApplicationEventListener<TicketEvent.SubTicketAdded> subTicketAddedEvent = new
                ApplicationEventListener<TicketEvent.SubTicketAdded>() {
                    @Override
                    @Subscribe
                    public void handle(TicketEvent.SubTicketAdded event) {
                        ProjectTicketService ticketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
                        ProjectTicket ticket = ticketService.findTicket(event.getTicketType(), event.getTicketId());
                        if (ticket != null) {
                            ticketsLayout.addComponent(generateSubTicketContent(ticket), 0);
                        }
                    }
                };

    private MVerticalLayout ticketsLayout;
    private ProjectTicket parentTicket;

    public SubTicketsComp(ProjectTicket parentTicket) {
        this.parentTicket = parentTicket;
    }

    @Override
    public void attach() {
            EventBusFactory.getInstance().register(subTicketAddedEvent);
        super.attach();
    }

    @Override
    public void detach() {
            EventBusFactory.getInstance().unregister(subTicketAddedEvent);
        super.detach();
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout contentLayout = new MHorizontalLayout().withFullWidth();
        ticketsLayout = new VerticalRemoveInlineComponentMarker().withFullWidth().withMargin(new MarginInfo(false, true, true, false));
        contentLayout.with(ticketsLayout).expand(ticketsLayout);

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            MButton addNewTaskBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                MWindow newTicketWindow = AppContextUtil.getSpringBean(TicketComponentFactory.class)
                        .createNewTicketWindow(null, parentTicket.getProjectId(), parentTicket.getMilestoneId(), true, null);
                UI.getCurrent().addWindow(newTicketWindow);
            }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.PLUS);

            SplitButton splitButton = new SplitButton(addNewTaskBtn);
            splitButton.setWidthUndefined();
            splitButton.addStyleName(WebThemes.BUTTON_ACTION);

            OptionPopupContent popupButtonsControl = new OptionPopupContent();
            Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                splitButton.setPopupVisible(false);
                UI.getCurrent().addWindow(new SelectChildTicketWindow(parentTicket));
            });
            popupButtonsControl.addOption(selectBtn);
            splitButton.setContent(popupButtonsControl);
            contentLayout.addComponent(splitButton);
        }

        ProjectTicketService projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        List<ProjectTicket> subTickets = projectTicketService.findSubTickets(ProjectTypeConstants.TASK, parentTicket.getTypeId());
        if (CollectionUtils.isNotEmpty(subTickets)) {
            for (ProjectTicket subTicket : subTickets) {
                ticketsLayout.addComponent(generateSubTicketContent(subTicket));
            }
        }
        return contentLayout;
    }

    @Override
    protected void doSetValue(Object o) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    private HorizontalLayout generateSubTicketContent(ProjectTicket subTicket) {
        MHorizontalLayout layout = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX).withMargin(new MarginInfo(false, false, false, true));
        layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        layout.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(subTicket.getType())));

        Span priorityLink = new Span().appendText(ProjectAssetsManager.getPriorityHtml(subTicket.getPriority()))
                .setTitle(subTicket.getPriority());
        layout.with(ELabel.html(priorityLink.write()).withUndefinedWidth());

        String taskStatus = UserUIContext.getMessage(StatusI18nEnum.class, subTicket.getStatus());
        ELabel statusLbl = new ELabel(taskStatus).withStyleName(WebThemes.FIELD_NOTE).withUndefinedWidth();
        layout.with(statusLbl);

        String avatarLink = StorageUtils.getAvatarPath(subTicket.getAssignUserAvatarId(), 16);
        Img avatarImg = new Img(subTicket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                .setTitle(subTicket.getAssignUserFullName());
        layout.with(ELabel.html(avatarImg.write()).withUndefinedWidth());

        ToggleTicketSummaryWithParentRelationshipField toggleTaskSummaryField = new ToggleTicketSummaryWithParentRelationshipField(parentTicket, subTicket);
        layout.with(toggleTaskSummaryField).expand(toggleTaskSummaryField);
        return layout;
    }
}
