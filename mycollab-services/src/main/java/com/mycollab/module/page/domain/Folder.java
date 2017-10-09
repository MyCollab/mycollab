package com.mycollab.module.page.domain;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class Folder extends PageResource {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
