package com.mycollab.module.project.view.bug.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.event.BugVersionEvent;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections4.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class VersionsViewField extends CustomField<Collection<Version>> {

    private MCssLayout containerLayout;

    public VersionsViewField() {
        containerLayout = new MCssLayout();
    }

    @Override
    protected Component initContent() {
        return containerLayout;
    }

    @Override
    protected void doSetValue(Collection<Version> versions) {
        if (CollectionUtils.isNotEmpty(versions)) {
            versions.forEach(version -> containerLayout.addComponent(buildVersionLink(version)));
        }
    }

    private MButton buildVersionLink(Version version) {
        return new MButton(StringUtils.trim(version.getName(), 25, true),
                clickEvent -> EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId())))
                .withDescription(version.getName()).withStyleName(UIConstants.BLOCK, ValoTheme.BUTTON_SMALL);
    }

    @Override
    public Collection<Version> getValue() {
        return null;
    }
}
