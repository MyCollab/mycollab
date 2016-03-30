package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class V20160310_2__Adjust_Project_Views implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `m_prj_customize_view` ADD COLUMN `displayInvoice` BIT(1) NULL;");
        jdbcTemplate.update("UPDATE m_prj_customize_view SET displayInvoice=?", new Object[]{1});
        jdbcTemplate.execute("ALTER TABLE `m_prj_customize_view` CHANGE COLUMN `displayInvoice` `displayInvoice` BIT(1) NOT NULL ;");
    }
}
