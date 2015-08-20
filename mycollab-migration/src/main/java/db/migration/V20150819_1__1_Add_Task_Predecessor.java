package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class V20150819_1__1_Add_Task_Predecessor implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("CREATE TABLE `m_prj_predecessor` (" +
                "  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT," +
                "  `type` VARCHAR(45) NOT NULL," +
                "  `typeId` INT(11) UNSIGNED NOT NULL," +
                "  `predestype` VARCHAR(45) NOT NULL," +
                "  `createdTime` DATETIME NOT NULL," +
                "  `createdUser` VARCHAR(45) COLLATE 'utf8mb4_unicode_ci' NULL," +
                "  PRIMARY KEY (`id`)," +
                "  INDEX `FK_m_prj_predecessor_1_idx` (`createdUser` ASC)," +
                "  CONSTRAINT `FK_m_prj_predecessor_1`" +
                "    FOREIGN KEY (`createdUser`)" +
                "    REFERENCES `s_user` (`username`)" +
                "    ON DELETE SET NULL" +
                "    ON UPDATE CASCADE);");

        jdbcTemplate.execute("DELETE  FROM s_account_theme WHERE s_account_theme.sAccountId IS NULL");
        jdbcTemplate.execute("INSERT INTO `s_account_theme`(topMenuBg, topMenuBgSelected, topMenuText, " +
                "topMenuTextSelected, tabsheetBg, tabsheetBgSelected, tabsheetText, tabsheetTextSelected, " +
                "vTabsheetBg, vTabsheetBgSelected, vTabsheetText, vTabsheetTextSelected, " +
                "actionBtn, actionBtnText, optionBtn, " +
                "optionBtnText, dangerBtn, dangerBtnText, " +
                "isDefault, sAccountId) VALUES " +
                "('0E4F71', 'FFFFFF', 'FFFFFF', '575757', 'FFFFFF', '12658F', '525252', 'FFFFFF', 'E4F1FF', 'C2DFFF'," +
                " '000000', '000000', '3A97FF', 'FFFFFF', '8B8B8B', 'FFFFFF', 'F64A46', 'FFFFFF', 1, NULL)");
    }
}
