package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.Img;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class TicketRowRenderer implements IBeanList.RowDisplayHandler<ProjectTicket> {
    @Override
    public Component generateRow(IBeanList<ProjectTicket> host, ProjectTicket genericTask, int rowIndex) {
        MHorizontalLayout rowComp = new MHorizontalLayout().withMargin(new MarginInfo(false, false, false, true))
                .withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX, WebThemes.MARGIN_BOTTOM);
        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(genericTask.getType())).withUndefinedWidth());
        String status;
        if (genericTask.isMilestone()) {
            status = UserUIContext.getMessage(OptionI18nEnum.MilestoneStatus.class, genericTask.getStatus());
        } else {
            status = UserUIContext.getMessage(com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class, genericTask.getStatus());
        }
        rowComp.with(new ELabel(status).withStyleName(WebThemes.BLOCK).withUndefinedWidth());
        String avatarLink = StorageUtils.getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
        Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                .setTitle(genericTask.getAssignUserFullName());

        ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(genericTask);
        rowComp.with(ELabel.html(img.write()).withUndefinedWidth(), toggleTicketSummaryField).expand(toggleTicketSummaryField);
        return rowComp;
    }
}
