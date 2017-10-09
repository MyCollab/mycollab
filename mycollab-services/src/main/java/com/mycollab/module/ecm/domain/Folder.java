package com.mycollab.module.ecm.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class Folder extends Resource {
    private List<Folder> childs = new ArrayList<>();

    public Folder() {
        super();
    }

    public Folder(String path) {
        this.setPath(path);
    }

    public List<Folder> getChilds() {
        return childs;
    }

    public void setChilds(List<Folder> childs) {
        this.childs = childs;
    }

    public void addChild(Folder child) {
        childs.add(child);
    }

    public boolean isHiddenFolder() {
        return getName().startsWith(".");
    }
}
