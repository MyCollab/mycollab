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

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    class FormLayoutFactory extends WrappedFormLayoutFactory {

        @Override
        public AbstractComponent getLayout() {
            MVerticalLayout layout = new MVerticalLayout().withMargin(false);
            wrappedLayoutFactory = new DefaultDynaFormLayout(ProjectTypeConstants.BUG, BugDefaultFormLayoutFactory.getAddForm());
            AbstractComponent gridLayout = wrappedLayoutFactory.getLayout();
            gridLayout.addStyleName(WebThemes.SCROLLABLE_CONTAINER);
            gridLayout.addStyleName("window-max-height");

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (bean.getStatus() == null) {
                    bean.setStatus(StatusI18nEnum.Open.name());
                }
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
                    asyncEventBus.post(new CleanCacheEvent(AppUI.getAccountId(), new Class[]{BugService.class}));

                    AttachmentUploadField uploadField = bugEditFormFieldFactory.getAttachmentUploadField();
                    String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(), bean.getProjectid(),
                            ProjectTypeConstants.BUG, "" + bugId);
                    uploadField.saveContentsToRepo(attachPath);
                    EventBusFactory.getInstance().post(new TicketEvent.NewTicketAdded(BugEditForm.this,
                            ProjectTypeConstants.BUG, bugId));
                    ProjectSubscribersComp subcribersComp = bugEditFormFieldFactory.getSubscribersComp();
                    List<String> followers = subcribersComp.getFollowers();
                    if (followers.size() > 0) {
                        List<MonitorItem> monitorItems = new ArrayList<>();
                        for (String follower : followers) {
                            MonitorItem monitorItem = new MonitorItem();
                            monitorItem.setSaccountid(AppUI.getAccountId());
                            monitorItem.setType(ProjectTypeConstants.BUG);
                            monitorItem.setTypeid(bugId + "");
                            monitorItem.setUsername(follower);
                            monitorItem.setExtratypeid(bean.getProjectid());
                            monitorItems.add(monitorItem);
                            monitorItem.setCreatedtime(LocalDateTime.now());
                        }
                        MonitorItemService monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
                        monitorItemService.saveMonitorItems(monitorItems);
                    }
                    postExecution();
                }
            }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> postExecution())
                    .withStyleName(WebThemes.BUTTON_OPTION);

            MCssLayout buttonControls = new MCssLayout(new MHorizontalLayout(cancelBtn, saveBtn).withStyleName(WebThemes.ALIGN_RIGHT)
                    .withMargin(new MarginInfo(true, false, false, false))).withFullWidth().withStyleName(WebThemes.BORDER_TOP);

            layout.with(gridLayout, buttonControls).expand(gridLayout);
            return layout;
        }
    }
}
