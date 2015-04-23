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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.domain.Tag;
import com.esofthead.mycollab.common.service.TagService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
@ViewComponent
public class TagListViewImpl extends AbstractPageView implements TagListView {

    public TagListViewImpl() {
        withMargin(new MarginInfo(true, false, true, true));
    }

    @Override
    public void displayTags(Tag tag) {
        removeAllComponents();

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("hdr-view").withWidth("100%");

        Label headerLbl = new Label(FontAwesome.TAGS.getHtml() + " Tags", ContentMode.HTML);
        headerLbl.setSizeUndefined();
        headerLbl.setStyleName("hdr-text");
        header.with(headerLbl);

        MHorizontalLayout contentWrapper = new MHorizontalLayout();

        MVerticalLayout rightSideBar = new MVerticalLayout().withSpacing(false).withWidth("450px");
        MHorizontalLayout panelHeader = new MHorizontalLayout().withMargin(new MarginInfo(false, true,
                false, true)).withHeight("34px").withWidth("100%");
        panelHeader.addStyleName("panel-header");
        Label lbl = new Label("Tag Cloud");
        panelHeader.with(lbl);

        TagCloudComp cloudComp = new TagCloudComp();
        cloudComp.displayTagItems();
        rightSideBar.with(panelHeader, cloudComp);

        contentWrapper.with(rightSideBar);
        with(header, contentWrapper);
    }

    private class TagCloudComp extends CssLayout {
        TagCloudComp() {
            this.setStyleName("tagcloud");
        }

        void displayTagItems() {
            TagService tagService = ApplicationContextUtil.getSpringBean(TagService.class);
            List<Tag> tags = tagService.findTagsInProject(CurrentProjectVariables.getProjectId(), AppContext
                    .getAccountId());
            if (CollectionUtils.isEmpty(tags)) {
                this.addComponent(new Label("No tag is existed"));
            } else {
                for (Tag tag : tags) {
                    TagButton btn = new TagButton(tag);
                    this.addComponent(btn);
                }
            }
        }
    }

    private class TagButton extends Button {
        private boolean isSelected = false;

        public TagButton(Tag tag) {
            super(tag.getName());
            this.setStyleName("tagbutton");
            this.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    isSelected = !isSelected;
                    if (isSelected) {
                        TagButton.this.removeStyleName("nonselected");
                        TagButton.this.addStyleName("selected");
                    } else {
                        TagButton.this.removeStyleName("selected");
                        TagButton.this.addStyleName("nonselected");
                    }
                }
            });
        }
    }
}
