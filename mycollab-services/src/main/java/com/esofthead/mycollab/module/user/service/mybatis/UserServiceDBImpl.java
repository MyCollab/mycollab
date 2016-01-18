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
package com.esofthead.mycollab.module.user.service.mybatis;

import com.esofthead.mycollab.configuration.IDeploymentMode;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.dao.RolePermissionMapper;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.dao.UserMapperExt;
import com.esofthead.mycollab.module.user.domain.*;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.esb.DeleteUserEvent;
import com.esofthead.mycollab.module.user.esb.RequestToResetPasswordEvent;
import com.esofthead.mycollab.module.user.esb.SendUserInvitationEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.PermissionMap;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
public class UserServiceDBImpl extends DefaultService<String, User, UserSearchCriteria> implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceDBImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMapperExt userMapperExt;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserAvatarService userAvatarService;

    @Autowired
    private BillingPlanCheckerService billingPlanCheckerService;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private IDeploymentMode deploymentMode;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ICrudGenericDAO getCrudMapper() {
        return userMapper;
    }

    @Override
    public ISearchableDAO<UserSearchCriteria> getSearchMapper() {
        return userMapperExt;
    }

    @Override
    public void saveUserAccount(SimpleUser record, Integer sAccountId, String inviteUser) {
        billingPlanCheckerService.validateAccountCanCreateNewUser(sAccountId);

        // check if user email has already in this account yet
        UserAccountExample userAccountEx = new UserAccountExample();

        if (deploymentMode.isDemandEdition()) {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail()).andAccountidEqualTo(sAccountId)
                    .andRegisterstatusIn(Arrays.asList(RegisterStatusConstants.ACTIVE,
                            RegisterStatusConstants.SENT_VERIFICATION_EMAIL, RegisterStatusConstants.VERIFICATING));
        } else {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail())
                    .andRegisterstatusIn(Arrays.asList(RegisterStatusConstants.ACTIVE,
                            RegisterStatusConstants.SENT_VERIFICATION_EMAIL, RegisterStatusConstants.VERIFICATING));
        }

        if (userAccountMapper.countByExample(userAccountEx) > 0) {
            throw new UserInvalidInputException(String.format("There is already user has email %s in your account", record.getEmail()));
        }

        if (record.getPassword() != null) {
            record.setPassword(PasswordEncryptHelper.encryptSaltPassword(record.getPassword()));
        }

        if (record.getUsername() == null) {
            record.setUsername(record.getEmail());
        }

        if (record.getLastname() == null) {
            String userEmail = record.getEmail();
            int index = userEmail.lastIndexOf("@");
            if (index > 0) {
                record.setLastname(userEmail.substring(0, index));
            } else {
                record.setLastname(userEmail);
            }
        }

        if (record.getFirstname() == null) {
            record.setFirstname("");
        }

        // Check if user has already account in system, if not we will create new user
        UserExample userEx = new UserExample();
        userEx.createCriteria().andUsernameEqualTo(record.getUsername());
        if (userMapper.countByExample(userEx) == 0) {
            record.setRegisterstatus(UserStatusConstants.EMAIL_NOT_VERIFIED);
            userMapper.insert(record);
            userAvatarService.uploadDefaultAvatar(record.getUsername());
        }

        // save record in s_user_account table
        UserAccount userAccount = new UserAccount();
        userAccount.setAccountid(record.getAccountId());
        userAccount.setIsaccountowner((record.getIsAccountOwner() == null) ? Boolean.FALSE : record.getIsAccountOwner());

        userAccount.setRoleid(record.getRoleid());
        userAccount.setUsername(record.getUsername());
        userAccount.setRegisteredtime(new GregorianCalendar().getTime());
        userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
        userAccount.setRegisterstatus(RegisterStatusConstants.SENT_VERIFICATION_EMAIL);
        userAccount.setInviteuser(inviteUser);

        LOG.debug("Check whether user is already in this account with status different than ACTIVE, then change his status");
        userAccountEx = new UserAccountExample();
        if (deploymentMode.isDemandEdition()) {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail()).andAccountidEqualTo(sAccountId);
        } else {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail());
        }

        if (userAccountMapper.countByExample(userAccountEx) > 0) {
            userAccountMapper.updateByExampleSelective(userAccount, userAccountEx);
        } else {
            userAccountMapper.insert(userAccount);
        }

        SendUserInvitationEvent invitationEvent = new SendUserInvitationEvent(record.getUsername(), record
                .getDisplayName(), record.getSubdomain(), sAccountId);
        asyncEventBus.post(invitationEvent);
    }

    @Override
    public Integer updateWithSession(User record, String username) {
        LOG.debug("Check whether there is exist email in system before");
        if ((record.getEmail()) != null && !record.getUsername().equals(record.getEmail())) {
            UserExample ex = new UserExample();
            ex.createCriteria().andUsernameEqualTo(record.getEmail());
            int numUsers = userMapper.countByExample(ex);
            if (numUsers > 0) {
                throw new UserInvalidInputException(
                        String.format("Email %s is already existed in system. Please choose another email.", record.getEmail()));
            }
        }

        // now we keep username similar than email
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(record.getUsername());
        record.setUsername(record.getEmail());
        return userMapper.updateByExampleSelective(record, ex);
    }

    @Override
    public void updateUserAccount(SimpleUser record, Integer sAccountId) {
        LOG.debug("Check whether there is exist email in system before");
        if (!record.getUsername().equals(record.getEmail())) {
            UserExample ex = new UserExample();
            ex.createCriteria().andUsernameEqualTo(record.getEmail());
            int numUsers = userMapper.countByExample(ex);
            if (numUsers > 0) {
                throw new UserInvalidInputException(
                        String.format("Email %s is already existed in system. Please choose another email.", record.getEmail()));
            }
        }

        if (Boolean.TRUE.equals(record.getIsAccountOwner())) {
            if (record.getRoleid() != null && record.getRoleid() != -1) {
                UserAccountExample userAccountEx = new UserAccountExample();
                userAccountEx.createCriteria().andAccountidEqualTo(sAccountId).andIsaccountownerEqualTo(Boolean.TRUE);
                if (userAccountMapper.countByExample(userAccountEx) == 1) {
                    throw new UserInvalidInputException(String.format("Can not change role of user %s. The reason is " +
                            "%s is the unique account owner of the current account.", record.getUsername(), record.getUsername()));
                } else {
                    record.setIsAccountOwner(false);
                }
            }
        }

        // now we keep username similar than email
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(record.getUsername());
        record.setUsername(record.getEmail());
        userMapper.updateByExampleSelective(record, ex);

        UserAccountExample userAccountEx = new UserAccountExample();
        userAccountEx.createCriteria().andUsernameEqualTo(record.getUsername()).andAccountidEqualTo(sAccountId);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountEx);
        if (userAccounts.size() > 0) {
            UserAccount userAccount = userAccounts.get(0);
            if (record.getRoleid() == -1) {
                userAccount.setRoleid(null);
                userAccount.setIsaccountowner(true);
            } else {
                userAccount.setRoleid(record.getRoleid());
                userAccount.setIsaccountowner(false);
            }

            userAccount.setRegisterstatus(record.getRegisterstatus());
            userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
            userAccountMapper.updateByPrimaryKey(userAccount);
        }
    }

    @Override
    public void massRemoveWithSession(List<User> users, String username, Integer accountId) {
        List<String> keys = new ArrayList<>();
        for (User user : users) {
            keys.add(user.getUsername());
        }
        userMapperExt.removeKeysWithSession(keys);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public SimpleUser authentication(String username, String password, String subDomain, boolean isPasswordEncrypt) {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUsername(StringSearchField.and(username));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));
        criteria.setSaccountid(null);

        if (deploymentMode.isDemandEdition()) {
            criteria.setSubdomain(StringSearchField.and(subDomain));
        }

        List<SimpleUser> users = findPagableListByCriteria(new SearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        if (CollectionUtils.isEmpty(users)) {
            throw new UserInvalidInputException(String.format("User %s is not existed in this domain %s", username, subDomain));
        } else {
            SimpleUser user = users.get(0);
            if (user.getPassword() == null || !PasswordEncryptHelper.checkPassword(password, user.getPassword(), isPasswordEncrypt)) {
                LOG.debug(String.format("PASS: %s   %s", password, user.getPassword()));
                throw new UserInvalidInputException("Invalid username or password");
            }

            LOG.debug(String.format("User %s login to system successfully!", username));

            if (user.getIsAccountOwner() == null || (user.getIsAccountOwner() != null && !user.getIsAccountOwner())) {
                if (user.getRoleid() != null) {
                    LOG.debug(String.format("User %s is not admin. Getting his role", username));
                    RolePermissionExample ex = new RolePermissionExample();
                    ex.createCriteria().andRoleidEqualTo(user.getRoleid());
                    List roles = rolePermissionMapper.selectByExampleWithBLOBs(ex);
                    if (CollectionUtils.isNotEmpty(roles)) {
                        RolePermission rolePer = (RolePermission) roles.get(0);
                        PermissionMap permissionMap = PermissionMap.fromJsonString(rolePer.getRoleval());
                        user.setPermissionMaps(permissionMap);
                        LOG.debug(String.format("Find role match to user %s", username));
                    } else {
                        LOG.debug(String.format("We can not find any role associate to user %s", username));
                    }
                } else {
                    LOG.debug(String.format("User %s has no any role", username));
                }
            }
            user.setPassword(null);
            return user;
        }
    }

    @Override
    public SimpleUser findUserByUserNameInAccount(String username, Integer accountId) {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUsername(StringSearchField.and(username));
        criteria.setSaccountid(new NumberSearchField(accountId));

        List<SimpleUser> users = userMapperExt.findPagableListByCriteria(criteria, new RowBounds(0, Integer.MAX_VALUE));
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void pendingUserAccount(String username, Integer accountId) {
        pendingUserAccounts(Arrays.asList(username), accountId);
    }

    @Override
    public void pendingUserAccounts(List<String> usernames, Integer accountId) {
        // check if current user is the unique account owner, then reject deletion
        UserAccountExample userAccountEx = new UserAccountExample();
        userAccountEx.createCriteria().andUsernameNotIn(usernames).andAccountidEqualTo(accountId)
                .andIsaccountownerEqualTo(true).andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE);
        if (userAccountMapper.countByExample(userAccountEx) == 0) {
            throw new UserInvalidInputException("Can not delete users. The reason is there is no " +
                    "account owner in the rest users");
        }

        userAccountEx = new UserAccountExample();
        userAccountEx.createCriteria().andUsernameIn(usernames).andAccountidEqualTo(accountId);
        UserAccount userAccount = new UserAccount();
        userAccount.setRegisterstatus(RegisterStatusConstants.DELETE);
        userAccountMapper.updateByExampleSelective(userAccount, userAccountEx);

        // notify users are "deleted"
        for (String username : usernames) {
            DeleteUserEvent event = new DeleteUserEvent(username, accountId);
            asyncEventBus.post(event);
        }
    }

    @Override
    public User findUserByUserName(String username) {
        UserExample ex = new UserExample();
        ex.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(ex);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void updateUserAccountStatus(String username, Integer sAccountId, String registerStatus) {
        // Update status of user account
        UserAccount userAccount = new UserAccount();
        userAccount.setAccountid(sAccountId);
        userAccount.setUsername(username);
        userAccount.setRegisterstatus(registerStatus);

        UserAccountExample ex = new UserAccountExample();
        ex.createCriteria().andAccountidEqualTo(sAccountId).andUsernameEqualTo(username);
        userAccountMapper.updateByExampleSelective(userAccount, ex);
    }

    @Override
    public int getTotalActiveUsersInAccount(Integer accountId) {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE));
        criteria.setSaccountid(new NumberSearchField(accountId));
        return userMapperExt.getTotalCount(criteria);
    }

    @Override
    public void requestToResetPassword(String username) {
        asyncEventBus.post(new RequestToResetPasswordEvent(username));
    }
}
