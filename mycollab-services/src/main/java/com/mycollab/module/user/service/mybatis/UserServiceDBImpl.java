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

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.billing.service.BillingPlanCheckerService;
import com.mycollab.module.file.service.UserAvatarService;
import com.mycollab.module.user.dao.RolePermissionMapper;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.dao.UserMapper;
import com.mycollab.module.user.dao.UserMapperExt;
import com.mycollab.module.user.domain.*;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.esb.DeleteUserEvent;
import com.mycollab.module.user.esb.NewUserJoinEvent;
import com.mycollab.module.user.esb.RequestToResetPasswordEvent;
import com.mycollab.module.user.esb.SendUserInvitationEvent;
import com.mycollab.module.user.service.UserService;
import com.mycollab.security.PermissionMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public ICrudGenericDAO getCrudMapper() {
        return userMapper;
    }

    @Override
    public ISearchableDAO<UserSearchCriteria> getSearchMapper() {
        return userMapperExt;
    }

    @Override
    public void saveUserAccount(User record, Integer roleId, String subDomain, Integer sAccountId, String inviteUser, boolean isSendInvitationEmail) {
        billingPlanCheckerService.validateAccountCanCreateNewUser(sAccountId);

        // check if user email has already in this account yet
        UserAccountExample userAccountEx = new UserAccountExample();

        if (deploymentMode.isDemandEdition()) {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail()).andAccountidEqualTo(sAccountId)
                    .andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE);
        } else {
            userAccountEx.createCriteria().andUsernameEqualTo(record.getEmail()).andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE);
        }

        if (userAccountMapper.countByExample(userAccountEx) > 0) {
            throw new UserInvalidInputException(String.format("There is already user has email %s in your account", record.getEmail()));
        }

        String password = record.getPassword();
        if (password != null) {
            record.setPassword(EnDecryptHelper.encryptSaltPassword(password));
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
            userMapper.insert(record);
            userAvatarService.uploadDefaultAvatar(record.getUsername());
        }

        // save record in s_user_account table
        UserAccount userAccount = new UserAccount();
        userAccount.setAccountid(sAccountId);

        if (roleId != null && roleId > 0) {
            userAccount.setRoleid(roleId);
            userAccount.setIsaccountowner(false);
        } else {
            userAccount.setRoleid(null);
            userAccount.setIsaccountowner(true);
        }

        userAccount.setUsername(record.getUsername());
        userAccount.setRegisteredtime(new GregorianCalendar().getTime());
        userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
        userAccount.setRegisterstatus(RegisterStatusConstants.NOT_LOG_IN_YET);
        userAccount.setInviteuser(inviteUser);

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

        if (isSendInvitationEmail) {
            SendUserInvitationEvent invitationEvent = new SendUserInvitationEvent(record.getUsername(), password,
                    inviteUser, subDomain, sAccountId);
            asyncEventBus.post(invitationEvent);
        }
    }

    @Override
    public Integer updateWithSession(User record, String username) {
        LOG.debug("Check whether there is exist email in system before");
        if ((record.getEmail()) != null && !record.getUsername().equals(record.getEmail())) {
            UserExample ex = new UserExample();
            ex.createCriteria().andUsernameEqualTo(record.getEmail());
            Long numUsers = userMapper.countByExample(ex);
            if (numUsers > 0) {
                throw new UserInvalidInputException(String.format("Email %s is already existed in system. Please choose another email.",
                        record.getEmail()));
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
        SimpleUser oldUser = findUserByUserNameInAccount(record.getUsername(), sAccountId);
        if (oldUser != null) {
            if (Boolean.TRUE.equals(oldUser.getIsAccountOwner()) && Boolean.FALSE.equals(record.getIsAccountOwner())) {
                UserAccountExample userAccountEx = new UserAccountExample();
                userAccountEx.createCriteria().andAccountidEqualTo(sAccountId).andIsaccountownerEqualTo(Boolean.TRUE)
                        .andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE);
                if (userAccountMapper.countByExample(userAccountEx) <= 1) {
                    throw new UserInvalidInputException(String.format("Can not change role of user %s. The reason is " +
                            "%s is the unique account owner of the current account.", record.getUsername(), record.getUsername()));
                }
            }
        }

        if (!record.getUsername().equals(record.getEmail())) {
            UserExample ex = new UserExample();
            ex.createCriteria().andUsernameEqualTo(record.getEmail());
            Long numUsers = userMapper.countByExample(ex);
            if (numUsers > 0) {
                throw new UserInvalidInputException(String.format("Email %s is already existed in system. Please choose another email.",
                        record.getEmail()));
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

    @Override
    public SimpleUser authentication(String username, String password, String subDomain, boolean isPasswordEncrypt) {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUsername(StringSearchField.and(username));
        criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET));
        criteria.setSaccountid(null);

        if (deploymentMode.isDemandEdition()) {
            criteria.setSubdomain(StringSearchField.and(subDomain));
        }

        List<SimpleUser> users = findPageableListByCriteria(new BasicSearchRequest<>(criteria, 0, Integer.MAX_VALUE));
        if (CollectionUtils.isEmpty(users)) {
            throw new UserInvalidInputException(String.format("User %s is not existed in this domain %s", username, subDomain));
        } else {
            SimpleUser user = null;
            if (deploymentMode.isDemandEdition()) {
                for (SimpleUser testUser : users) {
                    if (subDomain.equals(testUser.getSubdomain())) {
                        user = testUser;
                        break;
                    }
                }
                if (user == null) {
                    throw new UserInvalidInputException("Invalid username or password");
                }
            } else {
                user = users.get(0);
            }

            if (user.getPassword() == null || !EnDecryptHelper.checkPassword(password, user.getPassword(), isPasswordEncrypt)) {
                throw new UserInvalidInputException("Invalid username or password");
            }

            if (RegisterStatusConstants.NOT_LOG_IN_YET.equals(user.getRegisterstatus())) {
                updateUserAccountStatus(user.getUsername(), user.getAccountId(), RegisterStatusConstants.ACTIVE);
                asyncEventBus.post(new NewUserJoinEvent(user.getUsername(), user.getAccountId()));
            }
            LOG.debug(String.format("User %s login to system successfully!", username));

            if (user.getIsAccountOwner() == null || (user.getIsAccountOwner() != null && !user.getIsAccountOwner())) {
                if (user.getRoleid() != null) {
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
        return findUserInAccount(username, accountId);
    }

    @Override
    public SimpleUser findUserInAccount(String username, Integer accountId) {
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setUsername(StringSearchField.and(username));
        criteria.setSaccountid(new NumberSearchField(accountId));

        List<SimpleUser> users = userMapperExt.findPageableListByCriteria(criteria, new RowBounds(0, 1));
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public void pendingUserAccount(String username, Integer accountId) {
        pendingUserAccounts(Collections.singletonList(username), accountId);
    }

    @Override
    public void pendingUserAccounts(List<String> usernames, Integer accountId) {
        // check if current user is the unique account owner, then reject deletion
        UserAccountExample userAccountEx = new UserAccountExample();
        userAccountEx.createCriteria().andUsernameNotIn(usernames).andAccountidEqualTo(accountId)
                .andIsaccountownerEqualTo(true).andRegisterstatusEqualTo(RegisterStatusConstants.ACTIVE);
        if (userAccountMapper.countByExample(userAccountEx) == 0) {
            throw new UserInvalidInputException("Can not delete users. The reason is there is no account owner in the rest users");
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
