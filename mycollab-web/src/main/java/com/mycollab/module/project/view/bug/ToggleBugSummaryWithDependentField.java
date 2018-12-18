/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
// TODO
public class ToggleBugSummaryWithDependentField extends CustomField<SimpleBug> {

    private ToggleBugSummaryField toggleBugSummaryField;

    public ToggleBugSummaryWithDependentField(final BugWithBLOBs hostBug, final BugWithBLOBs relatedBug) {
        toggleBugSummaryField = new ToggleBugSummaryField(relatedBug);
        MButton unlinkBtn = new MButton("", clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                    AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO), confirmDialog -> {
                        RelatedBugExample ex = new RelatedBugExample();
                        ex.createCriteria().andBugidEqualTo(hostBug.getId()).andRelatedidEqualTo(relatedBug.getId());
                        RelatedBugMapper bugMapper = AppContextUtil.getSpringBean(RelatedBugMapper.class);
                        bugMapper.deleteByExample(ex);
                        UIUtils.removeChildAssociate(toggleBugSummaryField, RemoveInlineComponentMarker.class);
                    });
        }).withIcon(VaadinIcons.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP, ValoTheme.BUTTON_ICON_ONLY)
                .withDescription(UserUIContext.getMessage(BugI18nEnum.OPT_REMOVE_RELATIONSHIP));
        toggleBugSummaryField.addControl(unlinkBtn);
    }

    @Override
    protected Component initContent() {
        return toggleBugSummaryField;
    }

    @Override
    protected void doSetValue(SimpleBug simpleBug) {

    }

    @Override
    public SimpleBug getValue() {
        return null;
    }
}
