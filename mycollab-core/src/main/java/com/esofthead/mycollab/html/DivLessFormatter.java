package com.esofthead.mycollab.html;

import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.Div;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 *
 */
public class DivLessFormatter extends Div {

	@Override
	public String write() {
		StringBuffer b = new StringBuffer();
		if ((this.children != null) && (this.children.size() > 0)) {
			for (Node child : this.children) {
				b.append(child.write());
			}
		}
		return b.toString();
	}

}
