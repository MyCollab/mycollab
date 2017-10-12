/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.bug;

import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugAddView extends IFormAddView<SimpleBug> {

    HasEditFormHandlers<SimpleBug> getEditFormHandlers();

    AttachmentUploadField getAttachUploadField();

    List<Component> getComponents();

    List<Version> getAffectedVersions();

    List<Version> getFixedVersion();

    List<String> getFollowers();
}
