/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.wiki;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;

public class MyHtmlConverter extends ExtendedHtmlConverter {

	public MyHtmlConverter() {
		super();
	}

	public MyHtmlConverter(boolean noLinks) {
		super(noLinks);
	}

	public MyHtmlConverter(boolean noLinks, boolean noImages) {
		super(noLinks, noImages);
	}

	protected void renderContentToken(Appendable resultBuffer,
			ContentToken contentToken, IWikiModel model) throws IOException {
		String content = contentToken.getContent();
		content = content.replaceAll("\\(,", "(").replaceAll("\\(\\)", "()");
		content = Utils.escapeXml(content, true, true, true);
		resultBuffer.append(content);
	}

	protected void renderHtmlTag(Appendable resultBuffer, HTMLTag htmlTag,
			IWikiModel model) throws IOException {
		String tagName = htmlTag.getName();
		if ((!tagName.equals("ref"))) {
			super.renderHtmlTag(resultBuffer, htmlTag, model);
		}
	}
}
