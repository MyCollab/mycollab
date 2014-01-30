/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.ui.chart;

import java.text.AttributedString;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class JFreeChartLabelCustom implements PieSectionLabelGenerator {

	@Override
	public String generateSectionLabel(PieDataset dataset, Comparable key) {
		String result = null;
		if (dataset != null) {
			int value = dataset.getValue(key).intValue();
			if (value == 0) {
				return null;
			}

			result = key.toString() + " (" + dataset.getValue(key).intValue()
					+ ")";
		}
		return result;
	}

	@Override
	public AttributedString generateAttributedSectionLabel(PieDataset dataset,
			Comparable key) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
