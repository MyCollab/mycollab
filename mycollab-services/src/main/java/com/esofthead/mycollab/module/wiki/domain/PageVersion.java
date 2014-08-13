package com.esofthead.mycollab.module.wiki.domain;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public class PageVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	
	private int index;
	
	private Page frozenPage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Page getFrozenPage() {
		return frozenPage;
	}

	public void setFrozenPage(Page frozenPage) {
		this.frozenPage = frozenPage;
	}
}
