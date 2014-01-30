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

import info.bliki.api.Page;
import info.bliki.api.User;

import java.util.List;

public class WikiTest {
	public static void main(String[] args) {
		String[] listOfTitleStrings = { "Web service" };
		User user = new User("", "", "http://en.wikipedia.org/w/api.php");
		user.login();
		List<Page> listOfPages = user.queryContent(listOfTitleStrings);
		for (Page page : listOfPages) {
			MyWikiModel wikiModel = new MyWikiModel("${image}", "${title}");
			String currentContent = page.getCurrentContent();
			String html = wikiModel.render(new MyHtmlConverter(true, true),
					currentContent);
			System.out.println(html);
		}
	}
}
