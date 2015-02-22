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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class V20131101_3__Insert_Default_Values implements SpringJdbcMigration {
	private static final Logger LOG = LoggerFactory
			.getLogger(V20131101_3__Insert_Default_Values.class);

	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		LOG.info("Set up initial values");

		LOG.debug("Insert default billing plan");
		SimpleJdbcInsert billingJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("s_billing_plan")
				.usingColumns("billingType", "numUsers", "volume",
						"numProjects", "pricing", "hasBugEnable",
						"hasStandupMeetingEnable", "hasTimeTracking")
				.usingGeneratedKeyColumns("id");

		Map<String, Object> billingParameters = new HashMap<>();
		billingParameters.put("billingType", "Community");
		billingParameters.put("numUsers", 99999999);
		billingParameters.put("volume", 999999999999L);
		billingParameters.put("numProjects", 999999);
		billingParameters.put("pricing", 0);
		billingParameters.put("hasBugEnable", Boolean.TRUE);
		billingParameters.put("hasStandupMeetingEnable", Boolean.TRUE);
		billingParameters.put("hasTimeTracking", Boolean.TRUE);

		Number billingPlanId = billingJdbcInsert
				.executeAndReturnKey(billingParameters);

		LOG.debug("Insert default account");
		SimpleJdbcInsert accountJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("s_account")
				.usingColumns("status", "billingPlanId", "paymentMethod",
						"subdomain").usingGeneratedKeyColumns("id");
		Map<String, Object> accountParameters = new HashMap<>();
		accountParameters.put("status", "Active");
		accountParameters.put("billingPlanId", billingPlanId);
		accountParameters.put("paymentMethod", "None");
		accountParameters.put("subdomain", "");
		Number accountId = accountJdbcInsert
				.executeAndReturnKey(accountParameters);

		LOG.debug("Insert default users");
		SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("s_user").usingColumns("username", "firstname",
						"lastname", "email", "status", "registeredTime",
						"password", "timezone");

		Map<String, Object> userParameters = new HashMap<>();
		userParameters.put("username", "admin@mycollab.com");
		userParameters.put("firstname", "");
		userParameters.put("lastname", "admin");
		userParameters.put("email", "admin@mycollab.com");
		userParameters.put("status", "Active");
		userParameters.put("registeredTime", new Date());
		userParameters.put("password",
				PasswordEncryptHelper.encryptSaltPassword("admin123"));
		userParameters.put("timezone",
				TimezoneMapper.getTimezoneDbId(TimeZone.getDefault()));
		userJdbcInsert.execute(userParameters);

		LOG.debug("Insert default user avatar");

		LOG.debug("Create associate between user and billing plan");
		SimpleJdbcInsert userAccountJdbcInsert = new SimpleJdbcInsert(
				jdbcTemplate)
				.withTableName("s_user_account")
				.usingColumns("username", "accountId", "isAccountOwner",
						"registeredTime", "registerStatus")
				.usingGeneratedKeyColumns("id");
		Map<String, Object> userAccountParameters = new HashMap<>();
		userAccountParameters.put("username", "admin@mycollab.com");
		userAccountParameters.put("accountId", accountId);
		userAccountParameters.put("isAccountOwner", Boolean.TRUE);
		userAccountParameters.put("registeredTime", new Date());
		userAccountParameters.put("registerStatus", "Active");

		userAccountJdbcInsert.executeAndReturnKey(userAccountParameters);

		LOG.debug("Insert default roles");
		SimpleJdbcInsert roleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("s_roles")
				.usingColumns("rolename", "description", "sAccountId",
						"isSystemRole").usingGeneratedKeyColumns("id");

		LOG.debug("Create default admin role");
		SqlParameterSource adminRoleParameters = new MapSqlParameterSource()
				.addValue("rolename", "Administrator")
				.addValue("description", "Admin Role")
				.addValue("sAccountId", accountId)
				.addValue("isSystemRole", Boolean.TRUE);
		Number adminRoleId = roleJdbcInsert
				.executeAndReturnKey(adminRoleParameters);

		LOG.debug("Create default employee role");
		SqlParameterSource employeeRoleParameters = new MapSqlParameterSource()
				.addValue("rolename", "Employee")
				.addValue("description", "Employee Role")
				.addValue("sAccountId", accountId)
				.addValue("isSystemRole", Boolean.TRUE);
		Number employeeRoleId = roleJdbcInsert
				.executeAndReturnKey(employeeRoleParameters);

		LOG.debug("Create default guest role");
		SqlParameterSource guestRoleParameters = new MapSqlParameterSource()
				.addValue("rolename", "Guest")
				.addValue("description", "Guest Role")
				.addValue("sAccountId", accountId)
				.addValue("isSystemRole", Boolean.TRUE);
		Number guestRoleId = roleJdbcInsert
				.executeAndReturnKey(guestRoleParameters);

		LOG.debug("Associate permission with admin role");
		SimpleJdbcInsert rolePermissionJdbcInsert = new SimpleJdbcInsert(
				jdbcTemplate).withTableName("s_role_permission")
				.usingColumns("roleid", "roleVal")
				.usingGeneratedKeyColumns("id");

		SqlParameterSource adminRolePermissionParameters = new MapSqlParameterSource()
				.addValue("roleid", adminRoleId).addValue(
						"roleVal",
						PermissionMap.buildAdminPermissionCollection()
								.toJsonString());
		rolePermissionJdbcInsert.execute(adminRolePermissionParameters);

		LOG.debug("Associate permission with employee role");
		SqlParameterSource employeeRolePermissionParameters = new MapSqlParameterSource()
				.addValue("roleid", employeeRoleId).addValue(
						"roleVal",
						PermissionMap.buildEmployeePermissionCollection()
								.toJsonString());
		rolePermissionJdbcInsert.execute(employeeRolePermissionParameters);

		LOG.debug("Associate permission with guest role");
		SqlParameterSource guestRolePermissionParameters = new MapSqlParameterSource()
				.addValue("roleid", guestRoleId).addValue(
						"roleVal",
						PermissionMap.buildGuestPermissionCollection()
								.toJsonString());
		rolePermissionJdbcInsert.execute(guestRolePermissionParameters);
	}
}
