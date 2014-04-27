package com.esofthead.mycollab.schedule.email.format.html;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.hp.gagawa.java.elements.Text;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TagBuilder {
	public static A newA(String href, String text) {
		A link = new A(href, new Text(text));
		link.setStyle("text-decoration: none; color: rgb(36, 127, 211);");
		return link;
	}

	public static Img newImg(String alt, String src) {
		Img img = new Img(alt, src);
		img.setStyle("vertical-align: middle; margin-right: 3px;");
		return img;
	}

	public static Span newLink(Img img, A link) {
		Span span = new Span();
		span.appendChild(img, link);
		return span;
	}
}
