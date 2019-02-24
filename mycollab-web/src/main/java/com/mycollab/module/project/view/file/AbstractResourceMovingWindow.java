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
package com.mycollab.module.project.view.file;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
abstract class AbstractResourceMovingWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(AbstractResourceMovingWindow.class);

    private ResourceService resourceService;

    private Folder baseFolder;
    private Folder selectedFolder;
    private Collection<Resource> movedResources;

    AbstractResourceMovingWindow(Folder baseFolder, Resource resource) {
        this(baseFolder, Collections.singletonList(resource));
    }

    AbstractResourceMovingWindow(Folder baseFolder, Collection<Resource> resources) {
        super(UserUIContext.getMessage(FileI18nEnum.ACTION_MOVE_ASSETS));
        withModal(true).withResizable(false).withWidth("600px").withCenter();
        this.baseFolder = baseFolder;
        this.movedResources = resources;
        this.resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        constructBody();
    }

    private void constructBody() {
        MVerticalLayout contentLayout = new MVerticalLayout();
        this.setContent(contentLayout);

        Tree<Resource> folderTree = new Tree<>();
        folderTree.setSizeFull();

        AbstractBackEndHierarchicalDataProvider<Resource, SerializablePredicate<Resource>> dataProvider = new AbstractBackEndHierarchicalDataProvider<Resource, SerializablePredicate<Resource>>() {
            @Override
            protected Stream<Resource> fetchChildrenFromBackEnd(HierarchicalQuery<Resource, SerializablePredicate<Resource>> query) {
                Optional<Resource> parentRes = query.getParentOptional();
                if (parentRes.isPresent()) {
                    List<Resource> resources = resourceService.getResources(parentRes.get().getPath());
                    return (resources != null) ? resources.stream().filter(resource -> !resource.getName().startsWith(".") && !(resource instanceof Content)) : Stream.empty();
                } else {
                    return Stream.of(baseFolder);
                }
            }

            @Override
            public int getChildCount(HierarchicalQuery<Resource, SerializablePredicate<Resource>> query) {
                Stream<Resource> stream = fetchChildrenFromBackEnd(query);
                return (int) stream.count();
            }

            @Override
            public boolean hasChildren(Resource item) {
                if (item instanceof Folder) {
                    List<Resource> resources = resourceService.getResources(item.getPath());
                    return (resources != null) && !resources.isEmpty();
                }
                return false;
            }
        };

        folderTree.setDataProvider(dataProvider);
        folderTree.setItemCaptionGenerator((ItemCaptionGenerator<Resource>) item -> {
            if (item == baseFolder) {
                return UserUIContext.getMessage(FileI18nEnum.OPT_MY_DOCUMENTS);
            } else {
                return item.getName();
            }
        });

        folderTree.addItemClickListener(itemClickEvent -> selectedFolder = (Folder) itemClickEvent.getItem());

        contentLayout.addComponent(folderTree);

        MButton moveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_MOVE), clickEvent -> {
            if (!CollectionUtils.isEmpty(movedResources)) {
                boolean checkingFail = false;
                for (Resource res : movedResources) {
                    try {
                        resourceService.moveResource(res, selectedFolder, UserUIContext.getUsername(), AppUI.getAccountId());
                    } catch (Exception e) {
                        checkingFail = true;
                        LOG.error("Error", e);
                    }
                }
                close();
                displayAfterMoveSuccess(selectedFolder, checkingFail);
            }
        }).withIcon(VaadinIcons.ARROWS).withStyleName(WebThemes.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MHorizontalLayout controlGroupBtnLayout = new MHorizontalLayout(cancelBtn, moveBtn);
        contentLayout.with(controlGroupBtnLayout).withAlign(controlGroupBtnLayout, Alignment.MIDDLE_RIGHT);
    }

    public abstract void displayAfterMoveSuccess(Folder folder, boolean checking);

}
