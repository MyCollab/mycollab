package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class V20151205_1__Custom_Option_Colors   implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `m_options` ADD COLUMN `color` VARCHAR(6) NULL;");
        jdbcTemplate.update("UPDATE m_options SET color='fdde86' WHERE id > 0");
        jdbcTemplate.execute("ALTER TABLE `m_options` CHANGE COLUMN `color` `color` VARCHAR(6) NOT NULL ;");

        jdbcTemplate.update("UPDATE s_account_theme SET vTabsheetBg=?, vTabsheetBgSelected=?, " +
                "topMenuBgSelected=?, vTabsheetText=?, actionBtnText=?, optionBtnText=?, dangerBtnText=? WHERE " +
                "isDefault=? ", new Object[]{"24A3E3", "f7f7f7", "f7f7f7", "f7f7f7", "f7f7f7", "f7f7f7", "f7f7f7", 1});
    }
}
