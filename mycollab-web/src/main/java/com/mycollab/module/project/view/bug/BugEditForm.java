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
package com.mycollab.module.project.view.bug;

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class BugEditForm extends AdvancedEditBeanForm<SimpleBug> {
    @Override
    public void setBean(final SimpleBug item) {
        this.setFormLayoutFactory(new FormLayoutFactory());
        this.setBeanFormFieldFactory(new BugEditFormFieldFactory(this, item.getProjectid()));
        super.setBean(item);
    }

    protected void postExecution() {

    }

    class FormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 9);
            layout.addComponent(informationLayout.getLayout());

            MButton updateAllBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPDATE_OTHER_FIELDS), clickEvent -> {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(BugEditForm.this, bean));
                postExecution();
            }).withStyleName(WebUIConstants.BUTTON_LINK);
            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (validateForm()) {
                    BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                    Integer bugId;
                    if (bean.getId() == null) {
                        bugId = bugService.saveWithSession(bean, UserUIContext.getUsername());
                    } else {
                        bugService.updateWithSession(bean, UserUIContext.getUsername());
                        bugId = bean.getId();
                    }

                    AsyncEventBus asyncEventBus = AppContextUtil.getSpringBean(AsyncEventBus.class);
                    // save component
                    BugEditFormFieldFactory bugEditFormFieldFactory = (BugEditFormFieldFactory) fieldFactory;
                    BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
                    bugRelatedItemService.saveAffectedVersionsOfBug(bugId, bugEditFormFieldFactory.getAffectedVersionSelect().getSelectedItems());
                    bugRelatedItemService.saveComponentsOfBug(bugId, bugEditFormFieldFactory.getComponentSelect().getSelectedItems());
                    asyncEventBus.post(new CleanCacheEvent(MyCollabUI.getAccountId(), new Class[]{BugService.class}));

                    AttachmentUploadField uploadField = bugEditFormFieldFactory.getAttachmentUploadField();
                    String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), bean.getProjectid(),
                            ProjectTypeConstants.BUG, "" + bugId);
                    uploadField.saveContentsToRepo(attachPath);
                    EventBusFactory.getInstance().post(new BugEvent.NewBugAdded(BugEditForm.this, bugId));
                    EventBusFactory.getInstance().post(new TicketEvent.NewTicketAdded(BugEditForm.this,
                            ProjectTypeConstants.BUG, bugId));
                    ProjectSubscribersComp subcribersComp = bugEditFormFieldFactory.getSubscribersComp();
                    List<String> followers = subcribersComp.getFollowers();
                    if (followers.size() > 0) {
                        List<MonitorItem> monitorItems = new ArrayList<>();
                        for (String follower : followers) {
                            MonitorItem monitorItem = new MonitorItem();
                            monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                            monitorItem.setSaccountid(MyCollabUI.getAccountId());
                            monitorItem.setType(ProjectTypeConstants.BUG);
                            monitorItem.setTypeid(bugId);
                            monitorItem.setUser(follower);
                            monitorItem.setExtratypeid(bean.getProjectid());
                            monitorItems.add(monitorItem);
                        }
                        MonitorItemService monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
                        monitorItemService.saveMonitorItems(monitorItems);
                    }
                    postExecution();
                }
            }).withIcon(FontAwesome.SAVE).withStyleName(WebUIConstants.BUTTON_ACTION);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> postExecution())
                    .withStyleName(WebUIConstants.BUTTON_OPTION);

            MHorizontalLayout buttonControls = new MHorizontalLayout(updateAllBtn, cancelBtn, saveBtn).withMargin(new MarginInfo(true, true, true, false));

            layout.addComponent(buttonControls);
            layout.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
            layout.addStyleName(WebUIConstants.SCROLLABLE_CONTAINER);
            new Restrain(layout).setMaxHeight("600px");
            return layout;
        }

        @Override
        protected Component onAttachField(Object propertyId, Field<?> field) {
            if (BugWithBLOBs.Field.name.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0, 2, "100%");
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY),
                        UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY_HELP), 0, 1);
            } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 1);
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 2);
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_COMPONENTS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_COMPONENTS_HELP), 1, 2);
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 3);
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 1, 3);
            } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 4);
            } else if (BugWithBLOBs.Field.milestoneid.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), 1, 4);
            } else if (BugWithBLOBs.Field.name.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 5, 2, "100%");
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 6, 2, "100%");
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS), 0, 7, 2, "100%");
            } else if (SimpleBug.Field.selected.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS),
                        UserUIContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP), 0, 8, 2, "100%");
            }
            return null;
        }
    }
}
