package com.mycollab.module.project.view.bug.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.event.BugComponentEvent;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
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
public class ComponentsViewField extends CustomField<Collection<Component>> {

    private MCssLayout containerLayout;

    public ComponentsViewField() {
        containerLayout =  new MCssLayout();
    }

    @Override
    protected com.vaadin.ui.Component initContent() {
        return containerLayout;
    }

    @Override
    protected void doSetValue(Collection<Component> components) {
        if (CollectionUtils.isNotEmpty(components)) {
            components.forEach(component -> containerLayout.addComponent(buildComponentLink(component)));
        }
    }

    private MButton buildComponentLink(Component component) {
        return new MButton(StringUtils.trim(component.getName(), 25, true),
                clickEvent -> EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId())))
                .withDescription(component.getName()).withStyleName(WebThemes.BLOCK, ValoTheme.BUTTON_SMALL);
    }

    @Override
    public Collection<Component> getValue() {
        return null;
    }
}
