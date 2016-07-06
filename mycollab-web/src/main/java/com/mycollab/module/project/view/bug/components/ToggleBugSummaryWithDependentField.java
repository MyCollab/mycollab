/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.bug.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.tracker.dao.RelatedBugMapper;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.RelatedBugExample;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ToggleBugSummaryWithDependentField extends CustomField<SimpleBug> {

    private ToggleBugSummaryField toggleBugSummaryField;

    public ToggleBugSummaryWithDependentField(final BugWithBLOBs hostBug, final BugWithBLOBs relatedBug) {
        toggleBugSummaryField = new ToggleBugSummaryField(relatedBug);
        Button unlinkBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                        AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO), new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog confirmDialog) {
                                RelatedBugExample ex = new RelatedBugExample();
                                ex.createCriteria().andBugidEqualTo(hostBug.getId()).andRelatedidEqualTo(relatedBug.getId());
                                RelatedBugMapper bugMapper = AppContextUtil.getSpringBean(RelatedBugMapper.class);
                                bugMapper.deleteByExample(ex);
                                UIUtils.removeChildAssociate(toggleBugSummaryField, RemoveInlineComponentMarker.class);
                            }
                        });
            }
        });
        unlinkBtn.setIcon(FontAwesome.UNLINK);
        unlinkBtn.setDescription("Remove relationship");
        unlinkBtn.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
        unlinkBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
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
