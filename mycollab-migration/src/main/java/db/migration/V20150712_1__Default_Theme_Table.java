package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @version 5.1.0
 */
public class V20150712_1__Default_Theme_Table implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("DELETE  FROM s_account_theme WHERE s_account_theme.id > 0");
//        jdbcTemplate.execute("ALTER TABLE `s_account_theme` " +
//                "DROP COLUMN `toggleBtnTextSelected`," +
//                "DROP COLUMN `toggleBtnText`," +
//                "DROP COLUMN `toggleBtnSelected`," +
//                "DROP COLUMN `toggleBtn`," +
//                "DROP COLUMN `controlBtnText`," +
//                "DROP COLUMN `controlBtn`," +
//                "DROP COLUMN `clearBtnText`," +
//                "DROP COLUMN `clearBtn`;");

        jdbcTemplate.execute("INSERT INTO `s_account_theme`(topMenuBg, topMenuBgSelected, topMenuText, " +
                "topMenuTextSelected, tabsheetBg, tabsheetBgSelected, tabsheetText, tabsheetTextSelected, " +
                "vTabsheetBg, vTabsheetBgSelected, vTabsheetText, vTabsheetTextSelected, hTopMenuBg, " +
                "hTopMenuBgSelected, hTopMenuText, hTopMenuTextSelected, actionBtn, actionBtnText, optionBtn, " +
                "optionBtnText, dangerBtn, dangerBtnText, " +
                "isDefault, sAccountId) VALUES " +
                "('0E4F71', 'FFFFFF', 'FFFFFF', '575757', 'FFFFFF', '12658F', '525252', 'FFFFFF', '167AAD', 'FFFFFF'," +
                " 'FFFFFF', '000000', '47749D', 'FFFFFF', 'FFFFFF', '535353', '3A97FF', 'FFFFFF', '8B8B8B', 'FFFFFF'," +
                " 'F64A46', 'FFFFFF'," +
                " 1, NULL)");
    }
}
