package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.tracker.dao.RelatedBugMapper;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.RelatedBugExample;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ToggleBugSummaryWithDependentField extends CustomField<SimpleBug> {

    private ToggleBugSummaryField toggleBugSummaryField;

    public ToggleBugSummaryWithDependentField(final BugWithBLOBs hostBug, final BugWithBLOBs relatedBug) {
        toggleBugSummaryField = new ToggleBugSummaryField(relatedBug);
        MButton unlinkBtn = new MButton("", clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                    AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_NO), confirmDialog -> {
                        RelatedBugExample ex = new RelatedBugExample();
                        ex.createCriteria().andBugidEqualTo(hostBug.getId()).andRelatedidEqualTo(relatedBug.getId());
                        RelatedBugMapper bugMapper = AppContextUtil.getSpringBean(RelatedBugMapper.class);
                        bugMapper.deleteByExample(ex);
                        UIUtils.removeChildAssociate(toggleBugSummaryField, RemoveInlineComponentMarker.class);
                    });
        }).withIcon(FontAwesome.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP, ValoTheme.BUTTON_ICON_ONLY)
                .withDescription(UserUIContext.getMessage(BugI18nEnum.OPT_REMOVE_RELATIONSHIP));
        toggleBugSummaryField.addControl(unlinkBtn);
    }

    @Override
    protected Component initContent() {
        return toggleBugSummaryField;
    }

    @Override
    public Class<? extends SimpleBug> getType() {
        return SimpleBug.class;
    }
}
