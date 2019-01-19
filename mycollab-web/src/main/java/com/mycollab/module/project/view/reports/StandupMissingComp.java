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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class StandupMissingComp extends MVerticalLayout {
    private static final long serialVersionUID = 5332956503787026253L;

    private VerticalLayout bodyWrap;

    StandupMissingComp() {
        this.withSpacing(false).withMargin(false);
        Label headerLbl = new Label(UserUIContext.getMessage(StandupI18nEnum.STANDUP_MEMBER_NOT_REPORT));
        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true)).
                withHeight("34px").withFullWidth().with(headerLbl).
                withAlign(headerLbl, Alignment.MIDDLE_LEFT).withStyleName(WebThemes.PANEL_HEADER);

        bodyWrap = new MVerticalLayout().withStyleName("panel-body");
        this.with(header, bodyWrap).withFullWidth();
    }

    public void search(Integer projectId, LocalDate date) {
        bodyWrap.removeAllComponents();
        StandupReportService standupReportService = AppContextUtil.getSpringBean(StandupReportService.class);
        List<SimpleUser> someGuys = standupReportService.findUsersNotDoReportYet(projectId, date, AppUI.getAccountId());
        if (someGuys.size() == 0) {
            bodyWrap.addComponent(new Label(UserUIContext.getMessage(GenericI18Enum.EXT_NO_ITEM)));
        } else {
            for (SimpleUser user : someGuys) {
                bodyWrap.addComponent(ELabel.html(buildMemberLink(projectId, user)));
            }
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
