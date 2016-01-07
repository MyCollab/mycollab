/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile;

import com.esofthead.mycollab.core.MyCollabVersion;
import com.vaadin.sass.SassCompiler;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ThemeMobileCompiler {
    public static void main(String args[]) throws Exception {
        SassCompiler.main(new String[]{"src/main/resources/VAADIN/themes/" + MyCollabVersion.THEME_MOBILE_VERSION + "/styles.scss",
                "src/main/resources/VAADIN/themes/" + MyCollabVersion.THEME_MOBILE_VERSION + "/styles.css"});
        Path source = FileSystems.getDefault().getPath("src/main/resources/VAADIN/themes/" + MyCollabVersion.THEME_MOBILE_VERSION + "/", "styles.css");
        Path dest = FileSystems.getDefault().getPath("target/classes/VAADIN/themes/" + MyCollabVersion.THEME_MOBILE_VERSION + "/", "styles.css");
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }
}
