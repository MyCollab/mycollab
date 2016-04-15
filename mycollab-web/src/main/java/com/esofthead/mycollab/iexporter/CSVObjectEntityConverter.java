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
package com.esofthead.mycollab.iexporter;

import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.CSVItemMapperDef;
import com.esofthead.mycollab.iexporter.csv.CSVFormatter;
import com.esofthead.mycollab.vaadin.AppContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <E>
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public class CSVObjectEntityConverter<E> implements ObjectEntityConverter<CSVItemMapperDef, E> {
    private static final Logger LOG = LoggerFactory.getLogger(CSVObjectEntityConverter.class);

    @Override
    public E convert(Class<E> cls, CSVItemMapperDef unit) {
        try {
            E bean = cls.newInstance();
            String[] csvLine = unit.getCsvLine();

            PropertyUtils.setProperty(bean, "saccountid", AppContext.getAccountId());
            for (ImportFieldDef importFieldDef : unit.getFieldsDef()) {
                try {
                    String csvFieldItem = csvLine[importFieldDef.getColumnIndex()];
                    if (importFieldDef.getFieldFormatter() != null) {
                        PropertyUtils.setProperty(bean, importFieldDef.getFieldname(),
                                importFieldDef.getFieldFormatter().format(csvFieldItem));
                    } else
                        PropertyUtils.setProperty(bean, importFieldDef.getFieldname(), csvFieldItem);

                } catch (Exception e) {
                    LOG.error("Error", e);
                }
            }
            return bean;
        } catch (Exception e) {
            LOG.error("Error", e);
            return null;
        }
    }

    public static class FieldMapperDef {
        private String fieldname;
        private String description;

        private CSVFormatter fieldFormatter;

        public FieldMapperDef(String fieldname, String description) {
            this(fieldname, description, null);
        }

        public FieldMapperDef(String fieldname, String description, CSVFormatter formatter) {
            this.fieldname = fieldname;
            this.description = description;
            this.fieldFormatter = formatter;
        }

        public String getFieldname() {
            return fieldname;
        }

        public String getDescription() {
            return description;
        }

        public CSVFormatter getFieldFormatter() {
            return fieldFormatter;
        }
    }

    public static class ImportFieldDef {
        private int columnIndex;
        private FieldMapperDef fieldMapperDef;

        public ImportFieldDef(int columnIndex, FieldMapperDef fieldMapperDef) {
            this.columnIndex = columnIndex;
            this.fieldMapperDef = fieldMapperDef;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public String getFieldname() {
            return fieldMapperDef.getFieldname();
        }

        public String getDescription() {
            return fieldMapperDef.getDescription();
        }

        public CSVFormatter getFieldFormatter() {
            return fieldMapperDef.getFieldFormatter();
        }
    }

    public static class CSVItemMapperDef {
        private String[] csvLine;
        private ImportFieldDef[] fieldsDef;

        public CSVItemMapperDef(String[] csvLine, ImportFieldDef[] fieldDefs) {
            this.csvLine = csvLine;
            this.fieldsDef = fieldDefs;
        }

        public String[] getCsvLine() {
            return csvLine;
        }

        public void setCsvLine(String[] csvLine) {
            this.csvLine = csvLine;
        }

        public ImportFieldDef[] getFieldsDef() {
            return fieldsDef;
        }

        public void setFieldsDef(ImportFieldDef[] fieldsDef) {
            this.fieldsDef = fieldsDef;
        }
    }
}
