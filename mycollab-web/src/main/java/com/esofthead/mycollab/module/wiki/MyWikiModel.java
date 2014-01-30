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

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.namespaces.INamespace;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MyWikiModel extends WikiModel {

	public MyWikiModel(Configuration configuration, Locale locale,
			String imageBaseURL, String linkBaseURL) {
		super(configuration, locale, imageBaseURL, linkBaseURL);
	}

	public MyWikiModel(Configuration configuration,
			ResourceBundle resourceBundle, INamespace namespace,
			String imageBaseURL, String linkBaseURL) {
		super(configuration, resourceBundle, namespace, imageBaseURL,
				linkBaseURL);
	}

	public MyWikiModel(Configuration configuration, String imageBaseURL,
			String linkBaseURL) {
		super(configuration, imageBaseURL, linkBaseURL);
	}

	public MyWikiModel(String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
	}

	@Override
	public String getRawWikiContent(String namespace, String articleName,
			Map<String, String> templateParameters) {
		String rawContent = super.getRawWikiContent(namespace, articleName,
				templateParameters);

		if (rawContent == null) {
			return "";
		} else {
			return rawContent;
		}
	}
}
