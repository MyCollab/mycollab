package com.mycollab.module.project.view.reports;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.i18n.StandupI18nEnum;
import com.mycollab.module.project.service.StandupReportService;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class StandupMissingComp extends MVerticalLayout {
    private static final long serialVersionUID = 5332956503787026253L;

    StandupMissingComp(Integer projectId, LocalDate date) {
        this.withSpacing(false).withMargin(false);

        StandupReportService standupReportService = AppContextUtil.getSpringBean(StandupReportService.class);
        List<SimpleUser> someGuys = standupReportService.findUsersNotDoReportYet(projectId, date, AppUI.getAccountId());
        if (someGuys.size() == 0) {
            //this.addComponent(new Label(UserUIContext.getMessage(GenericI18Enum.EXT_NO_ITEM)));
        } else {
            with(new ELabel(UserUIContext.getMessage(StandupI18nEnum.STANDUP_MEMBER_NOT_REPORT)).withStyleName(ValoTheme.LABEL_H3));
            someGuys.forEach(user -> this.with(ELabel.html(buildMemberLink(projectId, user))));
        }
    }

    private String buildMemberLink(Integer projectId, SimpleUser user) {
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", StorageUtils.getAvatarPath(user.getAvatarid(), 16)).setCSSClass(UIConstants.CIRCLE_BOX);
        A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).
                setHref(ProjectLinkGenerator.generateProjectMemberLink(projectId, user.getUsername()));

        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(user.getUsername()));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        userLink.appendText(StringUtils.trim(user.getDisplayName(), 30, true));

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink);
        return div.write();
    }
}
