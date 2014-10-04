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
package com.esofthead.mycollab.vaadin.ui;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.EnumSet;

import com.squareup.javawriter.JavaWriter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class WebResourceGenerator {
	private static String ROOT_PATH = "src/main/resources/assets/icons";

	private static void emitFieldResources(JavaWriter jw, File baseFolder)
			throws IOException {
		File[] files = baseFolder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String filePath = file.getPath();
				filePath = filePath.substring(ROOT_PATH.length() + 1);
				int index = filePath.lastIndexOf('.');
				if (index < 0) {
					throw new RuntimeException("File resource " + filePath
							+ " is not valid");
				}
				String resourceName = "_"
						+ filePath.substring(0, index).replace('/', '_')
								.replace('-', '_');
				jw.emitField("String", resourceName,
						EnumSet.of(PUBLIC, STATIC, FINAL), "\"icons/"
								+ filePath + "\"");
			} else {
				emitFieldResources(jw, file);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		File imagesFolder = new File(ROOT_PATH);

		StringWriter w = new StringWriter();
		JavaWriter jw = new JavaWriter(w);
		jw.emitPackage("com.esofthead.mycollab.vaadin.ui").beginType(
				"com.esofthead.mycollab.vaadin.ui.WebResourceIds", "class",
				EnumSet.of(PUBLIC, FINAL));

		emitFieldResources(jw, imagesFolder);
		jw.endType();

		FileWriter fileWriter = new FileWriter(
				new File(
						"src/main/java/com/esofthead/mycollab/vaadin/ui/WebResourceIds.java"));
		fileWriter.write(w.toString());
		fileWriter.flush();
		fileWriter.close();
	}
}
