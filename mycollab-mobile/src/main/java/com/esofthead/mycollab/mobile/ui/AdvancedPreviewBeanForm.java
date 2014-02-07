package com.esofthead.mycollab.mobile.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.events.PreviewFormHandler;

/**
 * 
 * @param <B>
 * @author MyCollab Ltd
 * @since 1.0
 */
public class AdvancedPreviewBeanForm<B> extends GenericBeanForm<B> implements
		HasPreviewFormHandlers<B> {

	private static final long serialVersionUID = 1L;
	private Set<PreviewFormHandler<B>> handlers;

	public AdvancedPreviewBeanForm() {
		super();
	}

	@Override
	public void addFormHandler(PreviewFormHandler<B> handler) {
		if (handlers == null) {
			handlers = new HashSet<PreviewFormHandler<B>>();
		}

		handlers.add(handler);
	}

	public void fireAssignForm(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onAssign(bean);
			}
		}
	}

	public void fireEditForm(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onEdit(bean);
			}
		}
	}

	public void showHistory() {
		throw new MyCollabException(
				"This method must be override by sub classes");
	}

	public void fireCancelForm(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onCancel();
			}
		}
	}

	public void fireDeleteForm(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onDelete(bean);
			}
		}
	}

	public void fireCloneForm(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onClone(bean);
			}
		}
	}

	public void fireGotoNextItem(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.gotoNext(bean);
			}
		}
	}

	public void fireGotoPrevious(B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.gotoPrevious(bean);
			}
		}
	}

	public void fireExtraAction(String action, B bean) {
		if (handlers != null) {
			for (PreviewFormHandler<B> handler : handlers) {
				handler.onExtraAction(action, bean);
			}
		}
	}
}
