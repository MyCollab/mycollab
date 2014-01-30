package com.esofthead.mycollab.mobile.utils;

import java.io.File;

import com.esofthead.mycollab.configuration.FileStorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

public class UserAvatarControlFactory {
	public static Image createUserAvatarEmbeddedComponent(String avatarId,
			int size) {
		Image image = new Image(null, createAvatarResource(avatarId, size));
		return image;

	}

	public static String getAvatarLink(String userAvatarId, int size) {
		if (userAvatarId == null) {
			return "";
		}

		String link = SiteConfiguration.getStorageConfiguration()
				.generateAvatarPath(userAvatarId, size);

		return link;
	}

	public static Resource createAvatarResource(String avatarId, int size) {
		Resource avatarRes = null;

		if (avatarId == null) {
			return MyCollabResource.newResource("icons/default_user_avatar_"
					+ size + ".png");
		}

		if (SiteConfiguration.isSupportFileStorage()) {
			FileStorageConfiguration fileStorageConfiguration = (FileStorageConfiguration) SiteConfiguration
					.getStorageConfiguration();
			File avatarFile = fileStorageConfiguration.getAvatarFile(avatarId,
					size);
			if (avatarFile != null) {
				avatarRes = new FileResource(avatarFile);
			} else {
				avatarRes = MyCollabResource
						.newResource("icons/default_user_avatar_" + size
								+ ".png");
			}

		} else if (SiteConfiguration.isSupportS3Storage()) {
			avatarRes = new ExternalResource(SiteConfiguration
					.getStorageConfiguration().generateAvatarPath(avatarId,
							size));
		}

		return avatarRes;
	}
}
