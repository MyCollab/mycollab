package com.mycollab.module.crm.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.ActivityStreamService;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.CrmLocalizationTypeMap;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamPanel extends CssLayout {
    private static final long serialVersionUID = 1L;

    private final CrmActivityStreamPagedList activityStreamList;

    public ActivityStreamPanel() {
        activityStreamList = new CrmActivityStreamPagedList();
        this.addComponent(activityStreamList);
    }

    public void display() {
        final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.CRM));
        searchCriteria.setTypes(getRestrictedItemTypes());
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.addOrderField(new SearchCriteria.OrderField("createdTime", SearchCriteria.DESC));
        this.activityStreamList.setSearchCriteria(searchCriteria);
    }

    private SetSearchField<String> getRestrictedItemTypes() {
        SetSearchField<String> types = new SetSearchField<>();
        if (UserUIContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
            types.addValue(CrmTypeConstants.ACCOUNT);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CONTACT)) {
            types.addValue(CrmTypeConstants.CONTACT);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_LEAD)) {
            types.addValue(CrmTypeConstants.LEAD);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
            types.addValue(CrmTypeConstants.CAMPAIGN);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
            types.addValue(CrmTypeConstants.OPPORTUNITY);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CASE)) {
            types.addValue(CrmTypeConstants.CASE);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_TASK)) {
            types.addValue(CrmTypeConstants.TASK);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_MEETING)) {
            types.addValue(CrmTypeConstants.MEETING);
        }
        if (UserUIContext.canRead(RolePermissionCollections.CRM_CALL)) {
            types.addValue(CrmTypeConstants.CALL);
        }
        return types;
    }

    private static class CrmActivityStreamPagedList extends AbstractBeanPagedList<SimpleActivityStream> {
        private static final long serialVersionUID = 1L;

        private ActivityStreamService activityStreamService;

        CrmActivityStreamPagedList() {
            super(null, 20);
            setStyleName("activity-list");
            activityStreamService = AppContextUtil.getSpringBean(ActivityStreamService.class);
        }

        public int setSearchCriteria(final ActivityStreamSearchCriteria searchCriteria) {
            listContainer.removeAllComponents();
            searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, defaultNumberSearchItems);
            doSearch();
            return totalCount;
        }

        @Override
        protected void doSearch() {
            totalCount = activityStreamService.getTotalCount(((BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest).getSearchCriteria());
            totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
            if (searchRequest.getCurrentPage() > totalPage) {
                searchRequest.setCurrentPage(totalPage);
            }

            if (totalPage > 1) {
                if (controlBarWrapper != null) {
                    removeComponent(controlBarWrapper);
                }
                this.addComponent(createPageControls());
            } else {
                if (getComponentCount() == 2) {
                    removeComponent(getComponent(1));
                }
            }

            List<SimpleActivityStream> currentListData = (List<SimpleActivityStream>) activityStreamService.findPageableListByCriteria(
                    (BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest);
            listContainer.removeAllComponents();
            Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

            CssLayout currentFeedBlock = new CssLayout();
            AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);
            try {
                for (SimpleActivityStream activityStream : currentListData) {
                    Date itemCreatedDate = activityStream.getCreatedtime();

                    if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
                        currentFeedBlock = new CssLayout();
                        currentFeedBlock.setStyleName("feed-block");
                        feedBlocksPut(currentDate, itemCreatedDate, currentFeedBlock);
                        currentDate = itemCreatedDate;
                    }
                    StringBuilder content = new StringBuilder();
                    String itemType = CrmLocalizationTypeMap.getType(activityStream.getType());
                    String assigneeValue = buildAssigneeValue(activityStream);
                    String itemValue = buildItemValue(activityStream);

                    if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream.getAction())) {
                        content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_CREATE_ACTION,
                                assigneeValue, itemType, itemValue));
                    } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activityStream.getAction())) {
                        content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_UPDATE_ACTION,
                                assigneeValue, itemType, itemValue));
                        if (activityStream.getAssoAuditLog() != null) {
                            content.append(auditLogRegistry.generatorDetailChangeOfActivity(activityStream));
                        }
                    } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activityStream.getAction())) {
                        content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_COMMENT_ACTION, assigneeValue, itemType, itemValue));
                        if (activityStream.getAssoAuditLog() != null) {
                            content.append("<p><ul><li>\"").append(activityStream.getAssoAuditLog().getChangeset()).append("\"</li></ul></p>");
                        }
                    } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                        content.append(UserUIContext.getMessage(CrmCommonI18nEnum.WIDGET_ACTIVITY_DELETE_ACTION,
                                assigneeValue, itemType, itemValue));
                    }

                    Label activityLink = new Label(content.toString(), ContentMode.HTML);
                    CssLayout streamWrapper = new CssLayout();
                    streamWrapper.setWidth("100%");
                    streamWrapper.addStyleName("stream-wrapper");
                    streamWrapper.addComponent(activityLink);
                    currentFeedBlock.addComponent(streamWrapper);
                }
            } catch (Exception e) {
                throw new MyCollabException(e);
            }
        }

        @Override
        protected MHorizontalLayout createPageControls() {
            this.controlBarWrapper = new MHorizontalLayout().withFullHeight().withStyleName("page-controls");
            ButtonGroup controlBtns = new ButtonGroup();
            controlBtns.setStyleName(WebThemes.BUTTON_ACTION);
            MButton prevBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_NAV_NEWER), clickEvent -> pageChange(currentPage - 1))
                    .withWidth("64px").withStyleName(WebThemes.BUTTON_ACTION);
            if (currentPage == 1) {
                prevBtn.setEnabled(false);
            }

            MButton nextBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_NAV_OLDER), clickEvent -> pageChange(currentPage + 1))
                    .withWidth("64px").withStyleName(WebThemes.BUTTON_ACTION);
            if (currentPage == totalPage) {
                nextBtn.setEnabled(false);
            }

            controlBtns.addButton(prevBtn);
            controlBtns.addButton(nextBtn);

            controlBarWrapper.addComponent(controlBtns);
            return controlBarWrapper;
        }

        @Override
        protected QueryHandler<SimpleActivityStream> buildQueryHandler() {
            return new QueryHandler<SimpleActivityStream>() {
                @Override
                public int queryTotalCount() {
                    return 0;
                }

                @Override
                public List<SimpleActivityStream> queryCurrentData() {
                    return null;
                }
            };
        }

        private String buildAssigneeValue(SimpleActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Img userAvatar = new Img("", StorageUtils.getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                    .setCSSClass(UIConstants.CIRCLE_BOX);
            A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(AccountLinkGenerator.generateUserLink(
                    activityStream.getCreateduser())).appendText(StringUtils.trim
                    (activityStream.getCreatedUserFullName(), 30, true));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(activityStream.getCreateduser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink);

            return div.write();
        }

        private String buildItemValue(SimpleActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Text itemImg = new Text(CrmAssetsManager.getAsset(activityStream.getType()).getHtml());
            A itemLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(CrmLinkGenerator.generateCrmItemLink(
                    activityStream.getType(), Integer.parseInt(activityStream.getTypeid())));

            itemLink.setAttribute("onmouseover", TooltipHelper.crmHoverJsFunction(activityStream.getType(), activityStream.getTypeid()));
            itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            itemLink.appendText(activityStream.getNamefield());

            div.appendChild(itemImg, DivLessFormatter.EMPTY_SPACE, itemLink);
            return div.write();
        }

        private void feedBlocksPut(Date currentDate, Date nextDate, CssLayout currentBlock) {
            MHorizontalLayout blockWrapper = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName("feed-block-wrap");

            blockWrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentDate);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(nextDate);

            if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
                int currentYear = cal2.get(Calendar.YEAR);
                Label yearLbl = ELabel.html("<div>" + String.valueOf(currentYear) + "</div>").withStyleName
                        ("year-lbl").withWidthUndefined();
                listContainer.addComponent(yearLbl);
            } else {
                blockWrapper.setMargin(new MarginInfo(true, false, false, false));
            }
            Label dateLbl = new Label(DateFormatUtils.format(nextDate, "dd/MM"));
            dateLbl.setSizeUndefined();
            dateLbl.setStyleName("date-lbl");
            blockWrapper.with(dateLbl, currentBlock).expand(currentBlock);

            listContainer.addComponent(blockWrapper);
        }
    }
}
