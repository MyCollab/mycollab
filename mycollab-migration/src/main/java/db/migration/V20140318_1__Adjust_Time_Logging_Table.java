/**
 * This file is part of mycollab-migration.
 *
 * mycollab-migration is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-migration is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-migration.  If not, see <http://www.gnu.org/licenses/>.
 */
package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class V20140318_1__Adjust_Time_Logging_Table implements
		SpringJdbcMigration {

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate
				.execute("DELETE  FROM m_prj_time_logging WHERE m_prj_time_logging.id > 0");
		jdbcTemplate
				.execute("ALTER TABLE `m_prj_time_logging` ADD COLUMN `logForDay` DATETIME NOT NULL, ADD COLUMN `isBillable` BIT(1) NOT NULL");
	}

}
