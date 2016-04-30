package ch.qos.cal10n.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public abstract class AbstractCAL10NBundleFinderExt implements
        CAL10NBundleFinder {

    public CAL10NBundleExt getBundle(String baseName, Locale locale, String charset) {
        // same as the JDK convention
        //
        // It generates a path name from the candidate bundle name by replacing
        // all
        // "."
        // characters with "/" and appending the string ".properties".
        // / see also http: // tinyurl.com/ldgej8
        baseName = baseName.replace('.', '/');

        String languageAndCountryCandidate = computeLanguageAndCountryCandidate(
                baseName, locale);
        String languageOnlyCandidate = computeLanguageOnlyCandidate(baseName,
                locale);

        CAL10NBundleExt cprbLanguageOnly = makePropertyResourceBundle(
                languageOnlyCandidate, charset);
        CAL10NBundleExt cprbLanguageAndCountry = null;

        if (languageAndCountryCandidate != null) {
            cprbLanguageAndCountry = makePropertyResourceBundle(
                    languageAndCountryCandidate, charset);
        }

        if (cprbLanguageAndCountry != null) {
            cprbLanguageAndCountry.setParent(cprbLanguageOnly);
            return cprbLanguageAndCountry;
        }
        return cprbLanguageOnly;
    }

    private String computeLanguageAndCountryCandidate(String baseName,
                                                      Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (country != null && country.length() > 0) {
            return String.format("%s_%s-%s.properties", baseName, language, country);
        } else {
            return null;
        }
    }

    abstract protected URL getResource(String resourceCandidate);

    private CAL10NBundleExt makePropertyResourceBundle(String resourceCandidate,
                                                    String charset) {

        CAL10NBundleExt prb = null;
        URL url = getResource(resourceCandidate);
        if (url != null) {
            try {
                InputStream in = openConnectionForUrl(url);

                Reader reader = toReader(in, charset);
                prb = new CAL10NBundleExt(reader, urlToFile(url));
                in.close();
            } catch (IOException e) {
            }
        }
        return prb;
    }

    private static File urlToFile(URL url) {
        if (!"file".equals(url.getProtocol())) {
            return null;
        } else {
            String path = url.getPath();
            if (path == null) {
                return null;
            } else {
                File candidate = new File(path);
                return candidate.exists() ? candidate : null;
            }
        }
    }

    private String computeLanguageOnlyCandidate(String baseName, Locale locale) {
        String language = locale.getLanguage();
        return String.format("%s_%s.properties", baseName, language);
    }

    Reader toReader(InputStream in, String charset) {
        if (charset == null || charset.length() == 0)
            return new InputStreamReader(in);
        else {
            try {
                return new InputStreamReader(in, charset);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Failed to open reader", e);
            }
        }
    }

    private InputStream openConnectionForUrl(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDefaultUseCaches(false);
        return urlConnection.getInputStream();
    }
}
