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
