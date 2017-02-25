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
package com.mycollab.web.optional;

import com.mycollab.core.Version;
import com.vaadin.sass.SassCompiler;

import java.nio.file.*;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class ThemeCompiler {
    public static void main(String args[]) throws Exception {
        SassCompiler.main(new String[]{"src/main/resources/VAADIN/themes/" + Version.THEME_VERSION + "/styles.scss",
                "src/main/resources/VAADIN/themes/" + Version.THEME_VERSION +"/styles.css"});
        Path source = FileSystems.getDefault().getPath("src/main/resources/VAADIN/themes/" + Version.THEME_VERSION + "/", "styles.css");
        Path dest = FileSystems.getDefault().getPath("target/classes/VAADIN/themes/" + Version.THEME_VERSION + "/", "styles.css");
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }
}
