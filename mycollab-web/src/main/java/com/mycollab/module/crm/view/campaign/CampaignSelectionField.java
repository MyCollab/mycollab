/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignSelectionField extends CustomField<Integer> implements FieldSelection<CampaignWithBLOBs> {
    private CampaignWithBLOBs internalValue = new CampaignWithBLOBs();

    private TextField campaignName = new TextField();

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            setCampaignByVal((Integer) value);
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        if (internalValue != null) {
            setInternalValue(internalValue.getId());
        } else {
            setInternalValue(null);
        }
        super.commit();
    }

    private void setCampaignByVal(Integer campaignId) {
        CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
        SimpleCampaign campaign = campaignService.findById(campaignId, AppUI.getAccountId());
        if (campaign != null) {
            this.internalValue = campaign;
            campaignName.setValue(internalValue.getCampaignname());
        }
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        MButton browseBtn = new MButton("", clickEvent -> {
            CampaignSelectionWindow campaignWindow = new CampaignSelectionWindow(CampaignSelectionField.this);
            UI.getCurrent().addWindow(campaignWindow);
            campaignWindow.show();
        }).withIcon(FontAwesome.ELLIPSIS_H).withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);

        MButton clearBtn = new MButton("", clickEvent -> {
            campaignName.setValue("");
            internalValue = null;
        }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);

        layout.with(campaignName, browseBtn, clearBtn).expand(campaignName);
        return layout;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public void fireValueChange(CampaignWithBLOBs data) {
        this.internalValue = data;
        if (internalValue != null) {
            campaignName.setValue(internalValue.getCampaignname());
        }
    }
}
