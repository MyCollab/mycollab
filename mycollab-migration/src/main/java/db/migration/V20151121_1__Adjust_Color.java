package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class V20151121_1__Adjust_Color  implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.update("UPDATE s_account_theme SET vTabsheetBg=?, vTabsheetBgSelected=?, " +
                "vTabsheetTextSelected=? WHERE isDefault=? ", "1A90CB", "f7f7f7", "000000", 1);
    }
}
