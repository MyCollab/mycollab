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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class V20150401_2__Insert_Default_Values implements SpringJdbcMigration {
    private static final Logger LOG = LoggerFactory.getLogger(V20150401_2__Insert_Default_Values.class);

    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Set up initial values");

        LOG.debug("Insert default billing plan");
        SimpleJdbcInsert billingJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("s_billing_plan")
                .usingColumns("billingType", "numUsers", "volume", "numProjects", "pricing").usingGeneratedKeyColumns("id");

        Map<String, Object> billingParameters = new HashMap<>();
        billingParameters.put("billingType", "Community");
        billingParameters.put("numUsers", 99999999);
        billingParameters.put("volume", 999999999999L);
        billingParameters.put("numProjects", 999999);
        billingParameters.put("pricing", 0);

        Number billingPlanId = billingJdbcInsert.executeAndReturnKey(billingParameters);

        LOG.debug("Insert default account");
        SimpleJdbcInsert accountJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("s_account")
                .usingColumns("status", "billingPlanId", "paymentMethod", "subdomain").usingGeneratedKeyColumns("id");
        Map<String, Object> accountParameters = new HashMap<>();
        accountParameters.put("status", "Active");
        accountParameters.put("billingPlanId", billingPlanId);
        accountParameters.put("paymentMethod", "None");
        accountParameters.put("subdomain", "");
        accountJdbcInsert.executeAndReturnKey(accountParameters);
    }
}
