package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class V20151219_1__Tweak_Theme  implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        jdbcTemplate.update("UPDATE s_account_theme SET topMenuBg=?, actionBtn=?, vTabsheetBg=?, " +
                "vTabsheetBgSelected=?, vTabsheetText=?, vTabsheetTextSelected=? WHERE isDefault=? ",
                "1a8fcb", "24a2e3", "F9F9F9", "B5B5B5", "4C4C4C", "F1F1F1", 1);
    }
}
