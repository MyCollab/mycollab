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
package com.mycollab.module.project.ui.components;

import com.google.common.collect.Ordering;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.AuditChangeItem;
import com.mycollab.common.domain.SimpleAuditLog;
import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.AuditLogService;
import com.mycollab.common.service.CommentService;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.ReloadableComponent;
import com.mycollab.vaadin.ui.SafeHtmlLabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.formatter.DefaultFieldDisplayHandler;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.AttachmentDisplayComponent;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
// TODO
public class ProjectActivityComponent extends MVerticalLayout implements ReloadableComponent {
    private static Logger LOG = LoggerFactory.getLogger(ProjectActivityComponent.class);
    private String type;
    private String typeId;
    private ELabel headerLbl;
    private ProjectCommentInput commentBox;
    private MVerticalLayout activityBox;

    private CommentService commentService;
    private AuditLogService auditLogService;
    private FieldGroupFormatter groupFormatter;
    private boolean isAscending = true;
    private Ordering dateComparator = new Ordering() {
        @Override
        public int compare(Object o1, Object o2) {
            try {
                Date createTime1 = (Date) PropertyUtils.getProperty(o1, "createdtime");
                Date createTime2 = (Date) PropertyUtils.getProperty(o2, "createdtime");
                return createTime1.compareTo(createTime2);
            } catch (Exception e) {
                return 0;
            }
        }
    };

    public ProjectActivityComponent(String type, Integer extraTypeId) {
        withMargin(false).withStyleName("activity-comp").withFullWidth();
        this.type = type;
        this.groupFormatter = AuditLogRegistry.getFieldGroupFormatterOfType(type);
        headerLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.OPT_CHANGE_HISTORY, 0));

//        final OptionGroup sortDirection = new OptionGroup();
//        sortDirection.addStyleName("sortDirection");
//        String oldestFirstDirection = UserUIContext.getMessage(GenericI18Enum.OPT_OLDEST_FIRST);
//        final String newestFirstDirection = UserUIContext.getMessage(GenericI18Enum.OPT_NEWEST_FIRST);
//        sortDirection.addItems(newestFirstDirection, oldestFirstDirection);
//        sortDirection.setValue(newestFirstDirection);
//        sortDirection.addValueChangeListener(valueChangeEvent -> {
//            Object value = sortDirection.getValue();
//            isAscending = newestFirstDirection.equals(value);
//            displayActivities();
//        });

//        MHorizontalLayout headerPanel = new MHorizontalLayout().withMargin(true).withStyleName(WebThemes.FORM_SECTION)
//                .withFullWidth().with(headerLbl, sortDirection).withAlign(headerLbl, Alignment.MIDDLE_LEFT)
//                .withAlign(sortDirection, Alignment.MIDDLE_RIGHT);

        commentBox = new ProjectCommentInput(this, type, extraTypeId);
        activityBox = new MVerticalLayout().withMargin(new MMarginInfo(true, true, true, false));
//        this.with(headerPanel, commentBox, activityBox);

        commentService = AppContextUtil.getSpringBean(CommentService.class);
        auditLogService = AppContextUtil.getSpringBean(AuditLogService.class);
    }

    public void loadActivities(String typeId) {
        this.typeId = typeId;
        if (commentBox != null) {
            commentBox.setTypeAndId(typeId);
        }
        displayActivities();
    }

    private void displayActivities() {
        activityBox.removeAllComponents();
        if (type == null || typeId == null) {
            return;
        }

        final CommentSearchCriteria commentCriteria = new CommentSearchCriteria();
        commentCriteria.setType(StringSearchField.and(type));
        commentCriteria.setTypeId(StringSearchField.and(typeId));
        final int commentCount = commentService.getTotalCount(commentCriteria);

        final AuditLogSearchCriteria logCriteria = new AuditLogSearchCriteria();
        logCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        logCriteria.setModule(StringSearchField.and(ModuleNameConstants.PRJ));
        logCriteria.setType(StringSearchField.and(type));
        logCriteria.setTypeId(StringSearchField.and(typeId));
        final int logCount = auditLogService.getTotalCount(logCriteria);
        setTotalNums(commentCount + logCount);

        List<SimpleComment> comments = (List<SimpleComment>) commentService.findPageableListByCriteria(new BasicSearchRequest<>(commentCriteria));
        List<SimpleAuditLog> auditLogs = (List<SimpleAuditLog>) auditLogService.findPageableListByCriteria(new BasicSearchRequest<>(logCriteria));
        List activities = new ArrayList(commentCount + logCount);
        activities.addAll(comments);
        activities.addAll(auditLogs);
        if (isAscending) {
            Collections.sort(activities, dateComparator.reverse());
        } else {
            Collections.sort(activities, dateComparator);
        }

        for (Object activity : activities) {
            if (activity instanceof SimpleComment) {
                activityBox.addComponent(buildCommentBlock((SimpleComment) activity));
            } else if (activity instanceof SimpleAuditLog) {
                Component auditBlock = buildAuditBlock((SimpleAuditLog) activity);
                if (auditBlock != null) {
                    activityBox.addComponent(auditBlock);
                }
            } else {
                LOG.error("Do not support activity " + activity);
            }
        }
    }

    private Component buildCommentBlock(final SimpleComment comment) {
        final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withFullWidth();

        ProjectMemberBlock memberBlock = new ProjectMemberBlock(comment.getCreateduser(), comment.getOwnerAvatarId(),
                comment.getOwnerFullName());
        layout.addComponent(memberBlock);

        MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);

        MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
        messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        ELabel timePostLbl = ELabel.html(UserUIContext.getMessage(GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
                UserUIContext.formatPrettyTime(comment.getCreatedtime())))
                .withDescription(UserUIContext.formatDateTime(comment.getCreatedtime()))
                .withStyleName(UIConstants.META_INFO);

        if (hasDeletePermission(comment)) {
            MButton msgDeleteBtn = new MButton(VaadinIcons.TRASH).withListener(clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                                commentService.removeWithSession(comment, UserUIContext.getUsername(), AppUI.getAccountId());
                                activityBox.removeComponent(layout);
                            }
                        });
            }).withStyleName(WebThemes.BUTTON_ICON_ONLY);
            messageHeader.with(timePostLbl, msgDeleteBtn).expand(timePostLbl);
        } else {
            messageHeader.with(timePostLbl).expand(timePostLbl);
        }

        rowLayout.addComponent(messageHeader);

        Label messageContent = new SafeHtmlLabel(comment.getComment());
        rowLayout.addComponent(messageContent);

        List<Content> attachments = comment.getAttachments();
        if (!CollectionUtils.isEmpty(attachments)) {
            MVerticalLayout messageFooter = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            AttachmentDisplayComponent attachmentDisplay = new AttachmentDisplayComponent(attachments);
            attachmentDisplay.setWidth("100%");
            messageFooter.with(attachmentDisplay);
            rowLayout.addComponent(messageFooter);
        }

        layout.with(rowLayout).expand(rowLayout);
        return layout;
    }

    private boolean hasDeletePermission(SimpleComment comment) {
        return (UserUIContext.getUsername().equals(comment.getCreateduser()) || UserUIContext.isAdmin());
    }

    private Component buildAuditBlock(SimpleAuditLog auditLog) {
        List<AuditChangeItem> changeItems = auditLog.getChangeItems();
        if (CollectionUtils.isNotEmpty(changeItems)) {
            final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                    .withFullWidth();

            ProjectMemberBlock memberBlock = new ProjectMemberBlock(auditLog.getPosteduser(), auditLog.getPostedUserAvatarId(),
                    auditLog.getPostedUserFullName());
            layout.addComponent(memberBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);

            MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            ELabel timePostLbl = ELabel.html(UserUIContext.getMessage(GenericI18Enum.EXT_MODIFIED_ITEM, auditLog.getPostedUserFullName(),
                    UserUIContext.formatPrettyTime(auditLog.getPosteddate())))
                    .withDescription(UserUIContext.formatDateTime(auditLog.getPosteddate()));
            timePostLbl.setStyleName(UIConstants.META_INFO);
            messageHeader.with(timePostLbl).expand(timePostLbl);

            rowLayout.addComponent(messageHeader);

            for (AuditChangeItem item : changeItems) {
                String fieldName = item.getField();

                DefaultFieldDisplayHandler fieldDisplayHandler = groupFormatter.getFieldDisplayHandler(fieldName);
                if (fieldDisplayHandler != null) {
                    Span fieldBlock = new Span().appendText(UserUIContext.getMessage(fieldDisplayHandler.getDisplayName()))
                            .setCSSClass(UIConstants.BLOCK);
                    Div historyDiv = new Div().appendChild(fieldBlock).appendText(fieldDisplayHandler.getFormat()
                            .toString(item.getOldvalue())).appendText(" " + FontAwesome.LONG_ARROW_RIGHT.getHtml() +
                            " ").appendText(fieldDisplayHandler.getFormat().toString(item.getNewvalue()));
                    rowLayout.addComponent(ELabel.html(historyDiv.write()));
                }
            }

            layout.with(rowLayout).expand(rowLayout);
            return layout;
        } else {
            return null;
        }
    }

    private void setTotalNums(Integer nums) {
        headerLbl.setValue(UserUIContext.getMessage(GenericI18Enum.OPT_CHANGE_HISTORY, nums));
    }

    @Override
    public void reload() {
        displayActivities();
    }
}
