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
