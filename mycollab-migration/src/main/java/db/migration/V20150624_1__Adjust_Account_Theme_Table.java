package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by baohan on 6/24/15.
 */
public class V20150624_1__Adjust_Account_Theme_Table implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate
                .execute("DELETE  FROM s_account_theme WHERE s_account_theme.id > 0");
        jdbcTemplate.execute("INSERT INTO `s_account_theme` (`topMenuBg`, `topMenuBgSelected`, `topMenuText`, " +
                "`topMenuTextSelected`, `tabsheetBg`, `tabsheetBgSelected`, `tabsheetText`, `tabsheetTextSelected`, " +
                "`vTabsheetBg`, `vTabsheetBgSelected`, `vTabsheetText`, `vTabsheetTextSelected`, `hTopMenuBg`, " +
                "`hTopMenuBgSelected`, `hTopMenuText`, `hTopMenuTextSelected`, `actionBtn`, `actionBtnText`, " +
                "`optionBtn`, `optionBtnText`, `clearBtn`, `clearBtnText`, `controlBtn`, `controlBtnText`, `dangerBtn`, " +
                "`dangerBtnText`, `toggleBtn`, `toggleBtnSelected`, `toggleBtnText`, `toggleBtnTextSelected`, `isDefault`, " +
                "`sAccountId`) VALUES " +
                "('575757', 'FFFFFF', 'FFFFFF', '575757', 'FFFFFF', '707070', '525252', 'FFFFFF', '8A8A8A', 'FFFFFF'," +
                " 'FFFFFF', '000000', '47749D', 'FFFFFF', 'FFFFFF', '535353', '82BA4F', 'FFFFFF', '8B8B8B', 'FFFFFF'," +
                " 'FAFAFA', '525252', 'C06B11', 'FFFFFF', 'F64A46', 'FFFFFF', '7B7B7B', '1C7DCE', 'FFFFFF', 'FFFFFF'," +
                " 1, NULL);");
    }
}
