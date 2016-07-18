/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.service.mybatis;

import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.service.DefaultCrudService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.billing.UserStatusConstants;
import com.mycollab.module.billing.esb.AccountCreatedEvent;
import com.mycollab.module.user.dao.BillingAccountMapper;
import com.mycollab.module.user.dao.BillingAccountMapperExt;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.dao.UserMapper;
import com.mycollab.module.user.domain.*;
import com.mycollab.module.user.esb.SendUserEmailVerifyRequestEvent;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.security.PermissionMap;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class BillingAccountServiceImpl extends DefaultCrudService<Integer, BillingAccount> implements BillingAccountService {

    @Autowired
    private BillingAccountMapper billingAccountMapper;

    @Autowired
    private BillingAccountMapperExt billingAccountMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Override
    public ICrudGenericDAO<Integer, BillingAccount> getCrudMapper() {
        return billingAccountMapper;
    }

    @Override
    public SimpleBillingAccount getBillingAccountById(Integer accountId) {
        return billingAccountMapperExt.getBillingAccountById(accountId);
    }

    @Override
    public Integer updateSelectiveWithSession(BillingAccount record, String username) {
        try {
            return super.updateSelectiveWithSession(record, username);
        } catch (DuplicateKeyException e) {
            throw new UserInvalidInputException("The domain " + record.getSubdomain() + " is already used");
        }
    }

    @Override
    public BillingAccount getAccountByDomain(String domain) {
        BillingAccountExample ex = new BillingAccountExample();

        if (deploymentMode.isDemandEdition()) {
            ex.createCriteria().andSubdomainEqualTo(domain);
        }

        List<BillingAccount> accounts = billingAccountMapper.selectByExample(ex);
        if (accounts == null || accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    @Override
    public BillingAccount getAccountById(Integer accountId) {
        BillingAccountExample ex = new BillingAccountExample();

        if (deploymentMode.isDemandEdition()) {
            ex.createCriteria().andIdEqualTo(accountId);
        }

        List<BillingAccount> accounts = billingAccountMapper.selectByExample(ex);
        if (accounts == null || accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    @Override
    public void createDefaultAccountData(String username, String password, String timezoneId, String language, Boolean
                                         isEmailVerified, Boolean isCreatedDefaultData, Integer sAccountId) {
        // Check whether user has registered to the system before
        String encryptedPassword = EnDecryptHelper.encryptSaltPassword(password);
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(ex);

        Date now = new GregorianCalendar().getTime();

        if (CollectionUtils.isNotEmpty(users)) {
            for (User tmpUser : users) {
                if (!encryptedPassword.equals(tmpUser.getPassword())) {
                    throw new UserInvalidInputException("There is already user " + username
                            + " in the MyCollab database. If it is yours, you must enter the same password you registered to MyCollab. Otherwise " +
                            "you must use the different email.");
                }
            }
        } else {
            // Register the new user to this account
            User user = new User();
            user.setEmail(username);
            user.setPassword(encryptedPassword);
            user.setTimezone(timezoneId);
            user.setUsername(username);
            user.setRegisteredtime(now);
            user.setLastaccessedtime(now);
            user.setLanguage(language);

            if (isEmailVerified) {
                user.setStatus(UserStatusConstants.EMAIL_VERIFIED);
            } else {
                user.setStatus(UserStatusConstants.EMAIL_NOT_VERIFIED);
            }

            if (user.getFirstname() == null) {
                user.setFirstname("");
            }

            if (StringUtils.isBlank(user.getLastname())) {
                user.setLastname(StringUtils.extractNameFromEmail(username));
            }
            userMapper.insert(user);
            if (!isEmailVerified) {
                asyncEventBus.post(new SendUserEmailVerifyRequestEvent(user));
            }
        }

        // save default roles
        saveEmployeeRole(sAccountId);
        int adminRoleId = saveAdminRole(sAccountId);
        saveGuestRole(sAccountId);

        // save user account
        UserAccount userAccount = new UserAccount();
        userAccount.setAccountid(sAccountId);
        userAccount.setIsaccountowner(true);
        userAccount.setRegisteredtime(now);
        userAccount.setRegisterstatus(RegisterStatusConstants.ACTIVE);
        userAccount.setRegistrationsource("Web");
        userAccount.setRoleid(adminRoleId);
        userAccount.setUsername(username);

        userAccountMapper.insert(userAccount);
        asyncEventBus.post(new AccountCreatedEvent(sAccountId, username, true));
    }

    private int saveEmployeeRole(int accountId) {
        // Register default role for account
        Role role = new Role();
        role.setRolename(SimpleRole.EMPLOYEE);
        role.setDescription("");
        role.setSaccountid(accountId);
        role.setIssystemrole(true);
        Integer roleId = roleService.saveWithSession(role, "");
        roleService.savePermission(roleId, PermissionMap.buildEmployeePermissionCollection(), accountId);
        return roleId;
    }

    private int saveAdminRole(int accountId) {
        // Register default role for account
        Role role = new Role();
        role.setRolename(SimpleRole.ADMIN);
        role.setDescription("");
        role.setSaccountid(accountId);
        role.setIssystemrole(true);
        Integer roleId = roleService.saveWithSession(role, "");
        roleService.savePermission(roleId, PermissionMap.buildAdminPermissionCollection(), accountId);
        return roleId;
    }

    private int saveGuestRole(int accountId) {
        // Register default role for account
        final Role role = new Role();
        role.setRolename(SimpleRole.GUEST);
        role.setDescription("");
        role.setSaccountid(accountId);
        role.setIssystemrole(true);
        final int roleId = roleService.saveWithSession(role, "");
        roleService.savePermission(roleId, PermissionMap.buildGuestPermissionCollection(), accountId);
        return roleId;
    }
}
