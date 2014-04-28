/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
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
