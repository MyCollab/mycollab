package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.BitSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.module.crm.domain.criteria.CallSearchCriteria;
import com.mycollab.module.crm.i18n.CallI18nEnum;
import com.mycollab.module.crm.service.CallService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CallListDashlet extends Depot {

    private CallTableDisplay tableItem;

    public CallListDashlet() {
        super(UserUIContext.getMessage(CallI18nEnum.MY_ITEMS), new VerticalLayout());

        this.setMargin(new MarginInfo(true, false, false, false));

        tableItem = new CallTableDisplay(
                new TableViewField(null, "isClosed", WebUIConstants.TABLE_CONTROL_WIDTH), Arrays.asList(
                new TableViewField(CallI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_X_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_START_DATE, "startdate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH)));

        tableItem.addTableListener(event -> {
            final SimpleCall call = (SimpleCall) event.getData();
            if ("isClosed".equals(event.getFieldName())) {
                call.setIsclosed(true);
                CallService callService = AppContextUtil.getSpringBean(CallService.class);
                callService.updateWithSession(call, UserUIContext.getUsername());
                display();
            }
        });
        bodyContent.addComponent(tableItem);

        MButton customizeViewBtn = new MButton("", clickEvent -> {
        }).withIcon(FontAwesome.ADJUST).withStyleName(WebThemes.BUTTON_SMALL_PADDING)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final CallSearchCriteria criteria = new CallSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setAssignUsers(new SetSearchField<>(UserUIContext.getUsername()));
        criteria.setIsClosed(BitSearchField.FALSE);
        tableItem.setSearchCriteria(criteria);
    }
}
