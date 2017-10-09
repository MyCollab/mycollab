package com.mycollab.mobile.optional;

import com.mycollab.core.Version;
import com.vaadin.sass.SassCompiler;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ThemeCompiler {
    public static void main(String args[]) throws Exception {
        SassCompiler.main(new String[]{"src/main/resources/VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/styles.scss",
                "src/main/resources/VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/styles.css"});
        Path source = FileSystems.getDefault().getPath("src/main/resources/VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/", "styles.css");
        Path dest = FileSystems.getDefault().getPath("target/classes/VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/", "styles.css");
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }
}
