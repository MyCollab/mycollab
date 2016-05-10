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
package com.esofthead.mycollab.iexporter.csv;

import com.esofthead.mycollab.core.MyCollabException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CSVDateFormatter implements CSVFormatter<Date> {

    @Override
    public Date format(String value) {
        if (value.length() == 0 || value.trim().length() == 0)
            return null;
        else {
            try {
                DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
                formatter.setLenient(false);
                return formatter.parse(value);
            } catch (Exception e) {
                throw new MyCollabException("Can't parse value = \'" + value
                        + "\' to DateType, please input follow mm/dd/yyyy.");
            }
        }
    }

}
