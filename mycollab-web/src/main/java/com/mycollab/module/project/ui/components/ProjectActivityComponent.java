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
package com.mycollab.module.project.ui.components;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.AuditChangeItem;
import com.mycollab.common.domain.SimpleAuditLog;
import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.AuditLogService;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.SimpleLogging;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.formatter.DefaultFieldDisplayHandler;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.AttachmentDisplayComponent;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.google.common.collect.Ordering;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
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
public class ProjectActivityComponent extends MVerticalLayout implements ReloadableComponent {
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
        withMargin(false).withStyleName("activity-comp");
        this.type = type;
        this.groupFormatter = AuditLogRegistry.getFieldGroupFormatterOfType(type);
        headerLbl = new ELabel(AppContext.getMessage(GenericI18Enum.OPT_CHANGE_HISTORY, 0));

        final OptionGroup sortDirection = new OptionGroup();
        sortDirection.addStyleName("sortDirection");
        String oldestFirstDirection = AppContext.getMessage(GenericI18Enum.OPT_OLDEST_FIRST);
        final String newestFirstDirection = AppContext.getMessage(GenericI18Enum.OPT_NEWEST_FIRST);
        sortDirection.addItems(newestFirstDirection, oldestFirstDirection);
        sortDirection.setValue(newestFirstDirection);
        sortDirection.addValueChangeListener(valueChangeEvent -> {
            Object value = sortDirection.getValue();
            isAscending = newestFirstDirection.equals(value);
            displayActivities();
        });

        MHorizontalLayout headerPanel = new MHorizontalLayout().withMargin(true).withStyleName(WebUIConstants.FORM_SECTION)
                .withFullWidth().with(headerLbl, sortDirection).withAlign(headerLbl, Alignment.MIDDLE_LEFT)
                .withAlign(sortDirection, Alignment.MIDDLE_RIGHT);

        commentBox = new ProjectCommentInput(this, type, extraTypeId);
        activityBox = new MVerticalLayout().withMargin(new MMarginInfo(true, true, true, false));
        this.with(headerPanel, commentBox, activityBox);

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
        logCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        logCriteria.setModule(StringSearchField.and(ModuleNameConstants.PRJ));
        logCriteria.setType(StringSearchField.and(type));
        logCriteria.setTypeId(StringSearchField.and(typeId));
        final int logCount = auditLogService.getTotalCount(logCriteria);
        setTotalNums(commentCount + logCount);

        List<SimpleComment> comments = commentService.findPageableListByCriteria(new BasicSearchRequest<>(commentCriteria, 0, Integer.MAX_VALUE));
        List<SimpleAuditLog> auditLogs = auditLogService.findPageableListByCriteria(new BasicSearchRequest<>(logCriteria, 0, Integer.MAX_VALUE));
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
                SimpleLogging.error("Do not support activity " + activity);
            }
        }
    }

    private Component buildCommentBlock(final SimpleComment comment) {
        final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withFullWidth();

        ProjectMemberBlock memberBlock = new ProjectMemberBlock(comment.getCreateduser(), comment.getOwnerAvatarId(),
                comment.getOwnerFullName());
        layout.addComponent(memberBlock);

        MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebUIConstants.MESSAGE_CONTAINER);

        MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
        messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        ELabel timePostLbl = ELabel.html(AppContext.getMessage(GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
                AppContext.formatPrettyTime(comment.getCreatedtime())))
                .withDescription(AppContext.formatDateTime(comment.getCreatedtime()));
        timePostLbl.setStyleName(UIConstants.META_INFO);

        if (hasDeletePermission(comment)) {
            MButton msgDeleteBtn = new MButton(FontAwesome.TRASH_O).withListener(clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                                commentService.removeWithSession(comment, AppContext.getUsername(), AppContext.getAccountId());
                                activityBox.removeComponent(layout);
                            }
                        });
            }).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);
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
        return (AppContext.getUsername().equals(comment.getCreateduser()) || AppContext.isAdmin());
    }

    private Component buildAuditBlock(SimpleAuditLog auditLog) {
        List<AuditChangeItem> changeItems = auditLog.getChangeItems();
        if (CollectionUtils.isNotEmpty(changeItems)) {
            final MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                    .withFullWidth();

            ProjectMemberBlock memberBlock = new ProjectMemberBlock(auditLog.getPosteduser(), auditLog.getPostedUserAvatarId(),
                    auditLog.getPostedUserFullName());
            layout.addComponent(memberBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebUIConstants.MESSAGE_CONTAINER);

            MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            ELabel timePostLbl = ELabel.html(AppContext.getMessage(GenericI18Enum.EXT_MODIFIED_ITEM, auditLog.getPostedUserFullName(),
                    AppContext.formatPrettyTime(auditLog.getPosteddate())))
                    .withDescription(AppContext.formatDateTime(auditLog.getPosteddate()));
            timePostLbl.setStyleName(UIConstants.META_INFO);
            messageHeader.with(timePostLbl).expand(timePostLbl);

            rowLayout.addComponent(messageHeader);

            for (int i = 0; i < changeItems.size(); i++) {
                AuditChangeItem item = changeItems.get(i);
                String fieldName = item.getField();

                DefaultFieldDisplayHandler fieldDisplayHandler = groupFormatter.getFieldDisplayHandler(fieldName);
                if (fieldDisplayHandler != null) {
                    Span fieldBlock = new Span().appendText(AppContext.getMessage(fieldDisplayHandler.getDisplayName()))
                            .setCSSClass(WebUIConstants.BUTTON_BLOCK);
                    Div historyDiv = new Div().appendChild(fieldBlock).appendText(fieldDisplayHandler.getFormat()
                            .toString(item.getOldvalue())).appendText(" " + FontAwesome.LONG_ARROW_RIGHT.getHtml() +
                            " ").appendText(fieldDisplayHandler.getFormat().toString(item.getNewvalue()));
                    rowLayout.addComponent(new Label(historyDiv.write(), ContentMode.HTML));
                }
            }

            layout.with(rowLayout).expand(rowLayout);
            return layout;
        } else {
            return null;
        }
    }

    private void setTotalNums(int nums) {
        headerLbl.setValue(AppContext.getMessage(GenericI18Enum.OPT_CHANGE_HISTORY, nums));
    }

    @Override
    public void reload() {
        displayActivities();
    }
}
