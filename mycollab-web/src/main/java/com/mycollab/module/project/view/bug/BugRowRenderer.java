package com.mycollab.module.project.view.bug;

import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class BugRowRenderer implements IBeanList.RowDisplayHandler<SimpleBug> {
    @Override
    public Component generateRow(IBeanList<SimpleBug> host, SimpleBug bug, int rowIndex) {
        ToggleBugSummaryField toggleBugSummaryField = new ToggleBugSummaryField(bug);

        MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName(WebThemes.HOVER_EFFECT_NOT_BOX);
        rowComp.addStyleName("margin-bottom");
        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        String bugPriority = bug.getPriority();
        Span priorityLink = new Span().appendText(ProjectAssetsManager.getPriorityHtml(bugPriority)).setTitle(bugPriority);

        Span statusSpan = new Span().appendText(UserUIContext.getMessage(OptionI18nEnum.BugStatus.class,
                bug.getStatus())).setCSSClass(UIConstants.BLOCK);

        String avatarLink = StorageUtils.getAvatarPath(bug.getAssignUserAvatarId(), 16);
        Img img = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName())
                .setCSSClass(UIConstants.CIRCLE_BOX);

        rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG)).withWidthUndefined(),
                ELabel.html(priorityLink.write()).withWidthUndefined(),
                ELabel.html(statusSpan.write()).withWidthUndefined(),
                ELabel.html(img.write()).withWidthUndefined(),
                toggleBugSummaryField).expand(toggleBugSummaryField);
        return rowComp;
    }
}
