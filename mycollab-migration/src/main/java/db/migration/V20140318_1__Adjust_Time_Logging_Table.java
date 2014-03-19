package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;

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
