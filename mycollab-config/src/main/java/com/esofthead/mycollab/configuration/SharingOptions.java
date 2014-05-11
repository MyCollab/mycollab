/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

/**
 * MyCollab offers its social links in emails and all sharing options are kept
 * in this bean.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SharingOptions {
	private static SharingOptions instance;

	private String facebookUrl;

	private String linkedinUrl;

	private String googleplusUrl;

	private String twitterUrl;

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getLinkedinUrl() {
		return linkedinUrl;
	}

	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}

	public String getGoogleplusUrl() {
		return googleplusUrl;
	}

	public void setGoogleplusUrl(String googleplusUrl) {
		this.googleplusUrl = googleplusUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	static {
		instance = new SharingOptions();
		instance.setFacebookUrl("https://www.facebook.com/mycollab2");
		instance.setTwitterUrl("https://twitter.com/mycollabdotcom");
		instance.setLinkedinUrl("http://www.linkedin.com/company/mycollab");
		instance.setGoogleplusUrl("https://plus.google.com/u/0/b/112053350736358775306/+Mycollab/about/p/pub");
	}

	public static SharingOptions getDefaultSharingOptions() {
		return instance;
	}
}
