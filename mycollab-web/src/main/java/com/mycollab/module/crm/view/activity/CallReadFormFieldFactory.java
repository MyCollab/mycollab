package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.CallWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.module.crm.i18n.OptionI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallStatus;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallType;
import com.mycollab.module.crm.ui.components.RelatedReadItemField;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class CallReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCall> {
    private static final long serialVersionUID = 1L;

    CallReadFormFieldFactory(GenericBeanForm<SimpleCall> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleCall call = attachForm.getBean();

        if (propertyId.equals("assignuser")) {
            return new UserLinkViewField(call.getAssignuser(), call.getAssignUserAvatarId(), call.getAssignUserFullName());
        } else if (propertyId.equals("type")) {
            return new RelatedReadItemField(call);
        } else if (CallWithBLOBs.Field.status.equalTo(propertyId)) {
            StringBuilder value = new StringBuilder();
            if (call.getStatus() != null) {
                value.append(UserUIContext.getMessage(CallStatus.class, call.getStatus())).append(" ");
            }

            if (call.getCalltype() != null) {
                value.append(UserUIContext.getMessage(CallType.class, call.getCalltype()));
            }
            return new DefaultViewField(value.toString()).withStyleName(UIConstants.FIELD_NOTE);
        } else if (CallWithBLOBs.Field.durationinseconds.equalTo(propertyId)) {
            try {
                final long duration = Long.parseLong("" + call.getDurationinseconds() * 1000);

                Duration dur = new Duration(duration);
                PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays()
                        .appendSuffix("d").appendHours()
                        .appendSuffix("h").appendMinutes()
                        .appendSuffix("m").appendSeconds()
                        .appendSuffix("s").toFormatter();
                String formatted = formatter.print(dur.toPeriod());
                return new DefaultViewField(formatted);
            } catch (NumberFormatException e) {
                return new DefaultViewField("");
            }

        } else if (CallWithBLOBs.Field.startdate.equalTo(propertyId)) {
            return new DateTimeViewField(call.getStartdate());
        } else if (CallWithBLOBs.Field.description.equalTo(propertyId)) {
            return new RichTextViewField(call.getDescription());
        }

        return null;
    }

}
