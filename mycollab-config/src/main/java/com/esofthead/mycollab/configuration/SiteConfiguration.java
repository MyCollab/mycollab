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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.DeploymentMode;

/**
 * Utility class read mycollab system properties when system starts
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SiteConfiguration {
	private static Logger log = LoggerFactory
			.getLogger(SiteConfiguration.class);

	private static SiteConfiguration instance;

	private DeploymentMode deploymentMode;
	private StorageConfiguration storageConfiguration;
	private String sentErrorEmail;
	private String siteName;
	private String serverAddress;
	private int serverPort;
	private EmailConfiguration emailConfiguration;
	private EmailConfiguration relayEmailConfiguration;
	private DatabaseConfiguration databaseConfiguration;
	private String cdnUrl;
	private Properties cacheProperties;
	private String endecryptPassword;
	private String dropboxCallbackUrl;
	private String ggDriveCallbackUrl;
	private String appUrl;
	private Locale defaultLocale;
	private Map<String, Locale> supportedLanguages;

	public static void loadInstance(int serverPort) {
		ApplicationProperties.loadProps();
		instance = new SiteConfiguration();

		instance.sentErrorEmail = ApplicationProperties.getString(
				ApplicationProperties.ERROR_SENDTO, "hainguyen@mycollab.com");

		instance.siteName = ApplicationProperties.getString(
				ApplicationProperties.SITE_NAME, "MyCollab");

		instance.serverAddress = ApplicationProperties.getString(
				ApplicationProperties.SERVER_ADDRESS, "localhost");

		instance.defaultLocale = toLocale(ApplicationProperties.getString(
				ApplicationProperties.DEFAULT_LOCALE, "English"));

		instance.supportedLanguages = getSupportedLocales(ApplicationProperties
				.getString(ApplicationProperties.LOCALES, "English, Japanese"));

		instance.serverPort = serverPort;

		// load Deployment Mode
		String runningMode = ApplicationProperties.getString(
				ApplicationProperties.RUNNING_MODE, null);
		if ("site".equals(runningMode)) {
			log.debug("MyCollab is run under site mode");
			instance.deploymentMode = DeploymentMode.SITE;
		} else if ("standalone".equals(runningMode)) {
			log.debug("MyCollab is run under standalone mode");
			instance.deploymentMode = DeploymentMode.STANDALONE;
		} else {
			log.debug("MyCollab is run under development mode");
			instance.deploymentMode = DeploymentMode.DEV;
		}

		instance.cdnUrl = String.format(
				ApplicationProperties.getString(ApplicationProperties.CDN_URL),
				instance.serverAddress, instance.serverPort);
		instance.appUrl = String.format(
				ApplicationProperties.getString(ApplicationProperties.APP_URL),
				instance.serverAddress, instance.serverPort);

		// Load storage configuration
		String storageSystem = ApplicationProperties.getString(
				ApplicationProperties.STORAGE_SYSTEM, "file");
		if (StorageConfiguration.FILE_STORAGE_SYSTEM.equals(storageSystem)) {
			log.debug("MyCollab uses file storage system");
			instance.storageConfiguration = FileStorageConfiguration.build();
		} else {
			log.debug("MyCollab uses amazon s3 system");
			instance.storageConfiguration = S3StorageConfiguration
					.build(ApplicationProperties.getAppProperties());
		}

		instance.endecryptPassword = ApplicationProperties.getString(
				ApplicationProperties.BI_ENDECRYPT_PASSWORD, "esofthead321");

		// load email
		String host = ApplicationProperties
				.getString(ApplicationProperties.MAIL_SMTPHOST);
		String user = ApplicationProperties
				.getString(ApplicationProperties.MAIL_USERNAME);
		String password = ApplicationProperties
				.getString(ApplicationProperties.MAIL_PASSWORD);
		Integer port = Integer.parseInt(ApplicationProperties.getString(
				ApplicationProperties.MAIL_PORT, "-1"));
		Boolean isTls = Boolean.parseBoolean(ApplicationProperties.getString(
				ApplicationProperties.MAIL_IS_TLS, "false"));
		instance.emailConfiguration = new EmailConfiguration(host, user,
				password, port, isTls);

		// load relay email
		String relayHost = ApplicationProperties.getString(
				ApplicationProperties.RELAYMAIL_SMTPHOST, host);
		int relayPort = Integer.parseInt(ApplicationProperties.getString(
				ApplicationProperties.RELAYMAIL_PORT, port + ""));
		String relayUser = ApplicationProperties.getString(
				ApplicationProperties.RELAYMAIL_USERNAME, user);
		String relayPassword = ApplicationProperties.getString(
				ApplicationProperties.RELAYMAIL_PASSWORD, password);
		boolean relayIsTls = Boolean.parseBoolean(ApplicationProperties
				.getString(ApplicationProperties.RELAYMAIL_IS_TLS,
						Boolean.toString(isTls)));
		instance.relayEmailConfiguration = new EmailConfiguration(relayHost,
				relayUser, relayPassword, relayPort, relayIsTls);

		// load database configuration
		String driverClass = ApplicationProperties
				.getString(ApplicationProperties.DB_DRIVER_CLASS);
		String dbUrl = ApplicationProperties
				.getString(ApplicationProperties.DB_URL);
		String dbUser = ApplicationProperties
				.getString(ApplicationProperties.DB_USERNAME);
		String dbPassword = ApplicationProperties
				.getString(ApplicationProperties.DB_PASSWORD);
		instance.databaseConfiguration = new DatabaseConfiguration(driverClass,
				dbUrl, dbUser, dbPassword);

		// load cache properties
		Properties props = new Properties();
		props.put("infinispan.client.hotrod.server_list", ApplicationProperties
				.getString("infinispan.client.hotrod.server_list", ""));
		instance.cacheProperties = props;

		instance.dropboxCallbackUrl = ApplicationProperties
				.getString("dropbox.callbackUrl");

		instance.ggDriveCallbackUrl = ApplicationProperties
				.getString("ggDrive.callbackUrl");
	}

	public static Properties getCacheProperties() {
		return instance.cacheProperties;
	}

	public static String getCdnUrl() {
		return instance.cdnUrl;
	}

	public static String getAppUrl() {
		return instance.appUrl;
	}

	public static DatabaseConfiguration getDatabaseConfiguration() {
		return instance.databaseConfiguration;
	}

	public static EmailConfiguration getRelayEmailConfiguration() {
		return instance.relayEmailConfiguration;
	}

	public static EmailConfiguration getEmailConfiguration() {
		return instance.emailConfiguration;
	}

	public static String getSiteName() {
		return instance.siteName;
	}

	public static DeploymentMode getDeploymentMode() {
		return instance.deploymentMode;
	}

	public static String getSendErrorEmail() {
		return instance.sentErrorEmail;
	}

	public static StorageConfiguration getStorageConfiguration() {
		return instance.storageConfiguration;
	}

	public static Locale getDefaultLocale() {
		return instance.defaultLocale;
	}

	public static Map<String, Locale> getSupportedLanguages() {
		return instance.supportedLanguages;
	}

	public static boolean isSupportFileStorage() {
		return instance.storageConfiguration instanceof FileStorageConfiguration;
	}

	public static boolean isSupportS3Storage() {
		return instance.storageConfiguration instanceof S3StorageConfiguration;
	}

	public static String getSiteUrl(String subdomain) {
		String siteUrl = "";
		if (instance.deploymentMode == DeploymentMode.SITE) {
			siteUrl = String.format(ApplicationProperties
					.getString(ApplicationProperties.APP_URL), subdomain);
		} else {
			siteUrl = String.format(ApplicationProperties
					.getString(ApplicationProperties.APP_URL),
					instance.serverAddress, instance.serverPort);
		}
		return siteUrl;
	}

	public static String getDropboxCallbackUrl() {
		return instance.dropboxCallbackUrl;
	}

	public static String getGGDriveCallbackUrl() {
		return instance.ggDriveCallbackUrl;
	}

	public static String getEnDecryptPassword() {
		return instance.endecryptPassword;
	}

	public static String getServerAddress() {
		return instance.serverAddress;
	}

	public static int getServerPort() {
		return instance.serverPort;
	}

	private static Locale toLocale(String language) {
		if (language == null) {
			return Locale.US;
		}

		Map<String, Locale> nativeLanguages = LocaleHelper.getNativeLanguages();
		Locale locale = nativeLanguages.get(language);
		return (locale != null) ? locale : Locale.US;
	}

	private static Map<String, Locale> getSupportedLocales(String languageVal) {
		Map<String, Locale> locales = new HashMap<String, Locale>();
		Map<String, Locale> nativeLanguages = LocaleHelper.getNativeLanguages();
		String[] languages = languageVal.split(",");
		for (String language : languages) {
			Locale locale = nativeLanguages.get(language.trim());
			if (locale == null) {
				log.error("Do not support native language {}", language);
				continue;
			}

			locales.put(language, locale);
		}
		return locales;
	}
}
