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

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.esb.CamelProxyBuilderUtil;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.dao.RolePermissionMapper;
import com.esofthead.mycollab.module.user.dao.UserAccountInvitationMapper;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.dao.UserMapperExt;
import com.esofthead.mycollab.module.user.domain.RolePermission;
import com.esofthead.mycollab.module.user.domain.RolePermissionExample;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.domain.UserAccountInvitation;
import com.esofthead.mycollab.module.user.domain.UserExample;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.esb.UserEndpoints;
import com.esofthead.mycollab.module.user.esb.UserRemovedCommand;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Transactional
public class UserServiceDBImpl extends
		DefaultService<String, User, UserSearchCriteria> implements UserService {

	private static Logger log = LoggerFactory
			.getLogger(UserServiceDBImpl.class);

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
	private UserAccountInvitationMapper userAccountInvitationMapper;

	@Autowired
	private BillingPlanCheckerService billingPlanCheckerService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ICrudGenericDAO getCrudMapper() {
		return userMapper;
	}

	@Override
	public ISearchableDAO<UserSearchCriteria> getSearchMapper() {
		return userMapperExt;
	}

	@Override
	public void saveUserAccount(SimpleUser record, Integer sAccountId,
			String inviteUser) {
		billingPlanCheckerService.validateAccountCanCreateNewUser(sAccountId);

		// check if user email has already in this account yet
		UserAccountExample userAccountEx = new UserAccountExample();

		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
			userAccountEx
					.createCriteria()
					.andUsernameEqualTo(record.getEmail())
					.andAccountidEqualTo(sAccountId)
					.andRegisterstatusIn(
							Arrays.asList(
									RegisterStatusConstants.ACTIVE,
									RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
									RegisterStatusConstants.VERIFICATING));
		} else {
			userAccountEx
					.createCriteria()
					.andUsernameEqualTo(record.getEmail())
					.andRegisterstatusIn(
							Arrays.asList(
									RegisterStatusConstants.ACTIVE,
									RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
									RegisterStatusConstants.VERIFICATING));
		}

		if (userAccountMapper.countByExample(userAccountEx) > 0) {
			throw new UserInvalidInputException(
					"There is already user has email " + record.getEmail()
							+ " in your account");
		}

		if (record.getPassword() != null) {
			record.setPassword(PasswordEncryptHelper.encryptSaltPassword(record
					.getPassword()));
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

		// Check if user has already account in system, if not we will create
		// new user

		UserExample userEx = new UserExample();
		userEx.createCriteria().andUsernameEqualTo(record.getUsername());
		if (userMapper.countByExample(userEx) == 0) {
			record.setRegisterstatus(RegisterStatusConstants.VERIFICATING);
			userMapper.insert(record);

			userAvatarService.uploadDefaultAvatar(record.getUsername());
		}

		// save record in s_user_account table
		UserAccount userAccount = new UserAccount();
		userAccount.setAccountid(record.getAccountId());
		userAccount
				.setIsaccountowner((record.getIsAccountOwner() == null) ? Boolean.FALSE
						: record.getIsAccountOwner());

		userAccount.setRoleid(record.getRoleid());
		userAccount.setUsername(record.getUsername());
		userAccount.setRegisteredtime(new GregorianCalendar().getTime());
		userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
		userAccount
				.setRegisterstatus((record.getRegisterstatus() == null) ? RegisterStatusConstants.VERIFICATING
						: record.getRegisterstatus());

		log.debug("Check whether user is already in this account with status different than ACTIVE, then change status of him");
		userAccountEx = new UserAccountExample();
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
			userAccountEx.createCriteria()
					.andUsernameEqualTo(record.getEmail())
					.andAccountidEqualTo(sAccountId);
		} else {
			userAccountEx.createCriteria()
					.andUsernameEqualTo(record.getEmail());
		}

		if (userAccountMapper.countByExample(userAccountEx) > 0) {
			userAccountMapper.updateByExampleSelective(userAccount,
					userAccountEx);
		} else {
			userAccountMapper.insert(userAccount);
		}

		if (!RegisterStatusConstants.ACTIVE.equals(record.getRegisterstatus())) {
			// save to invitation user
			UserAccountInvitation invitation = new UserAccountInvitation();
			invitation.setAccountid(record.getAccountId());
			invitation.setCreatedtime(new GregorianCalendar().getTime());
			invitation.setUsername(record.getUsername());
			invitation.setInviteuser(inviteUser);
			invitation
					.setInvitationstatus((record.getRegisterstatus() == null) ? RegisterStatusConstants.VERIFICATING
							: record.getRegisterstatus());
			userAccountInvitationMapper.insert(invitation);
		}
	}

	@Override
	public int updateWithSession(User record, String username) {
		log.debug("Check whether there is exist email in system before");
		if ((record.getEmail()) != null
				&& !record.getUsername().equals(record.getEmail())) {
			UserExample ex = new UserExample();
			ex.createCriteria().andUsernameEqualTo(record.getEmail());
			int numUsers = userMapper.countByExample(ex);
			if (numUsers > 0) {
				throw new UserInvalidInputException(
						"Email "
								+ record.getEmail()
								+ " is already existed in system. Please choose another email.");
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
		log.debug("Check whether there is exist email in system before");
		if (!record.getUsername().equals(record.getEmail())) {
			UserExample ex = new UserExample();
			ex.createCriteria().andUsernameEqualTo(record.getEmail());
			int numUsers = userMapper.countByExample(ex);
			if (numUsers > 0) {
				throw new UserInvalidInputException(
						"Email "
								+ record.getEmail()
								+ " is already existed in system. Please choose another email.");
			}
		}

		// now we keep username similar than email
		UserExample ex = new UserExample();
		ex.createCriteria().andUsernameEqualTo(record.getUsername());
		record.setUsername(record.getEmail());
		userMapper.updateByExampleSelective(record, ex);

		UserAccountExample userAccountEx = new UserAccountExample();
		userAccountEx.createCriteria().andUsernameEqualTo(record.getUsername())
				.andAccountidEqualTo(sAccountId);
		List<UserAccount> userAccounts = userAccountMapper
				.selectByExample(userAccountEx);
		if (userAccounts.size() > 0) {
			UserAccount userAccount = userAccounts.get(0);
			if (record.getRoleid() == -1) {
				userAccount.setRoleid(null);
				userAccount.setIsaccountowner(true);
			} else {
				userAccount.setRoleid(record.getRoleid());
			}

			userAccount.setRegisterstatus(record.getRegisterstatus());
			userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
			userAccountMapper.updateByPrimaryKey(userAccount);
		}
	}

	@Override
	public void massRemoveWithSession(List<String> primaryKeys,
			String username, int accountId) {
		userMapperExt.removeKeysWithSession(primaryKeys);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SimpleUser authentication(String username, String password,
			String subdomain, boolean isPasswordEncrypt) {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setUsername(new StringSearchField(username));
		criteria.setSaccountid(null);

		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
			criteria.setSubdomain(new StringSearchField(subdomain));
		}

		List<SimpleUser> users = findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
				criteria, 0, Integer.MAX_VALUE));
		if (users == null || users.isEmpty()) {
			throw new UserInvalidInputException("User " + username
					+ " is not existed in this domain");
		} else {
			SimpleUser user = users.get(0);
			if (user.getPassword() == null
					|| !PasswordEncryptHelper.checkPassword(password,
							user.getPassword(), isPasswordEncrypt)) {
				log.debug("PASS: " + password + "   " + user.getPassword());
				throw new UserInvalidInputException(
						"Invalid username or password");
			}

			log.debug("User " + username + " login to system successfully!");

			if (user.getIsAccountOwner() == null
					|| (user.getIsAccountOwner() != null && !user
							.getIsAccountOwner())) {
				if (user.getRoleid() != null) {
					log.debug("User " + username
							+ " is not admin. Getting his role");
					RolePermissionExample ex = new RolePermissionExample();
					ex.createCriteria().andRoleidEqualTo(user.getRoleid());
					List roles = rolePermissionMapper
							.selectByExampleWithBLOBs(ex);
					if (CollectionUtils.isNotEmpty(roles)) {
						RolePermission rolePer = (RolePermission) roles.get(0);
						PermissionMap permissionMap = PermissionMap
								.fromJsonString(rolePer.getRoleval());
						user.setPermissionMaps(permissionMap);
						log.debug("Find role match to user " + username);
					} else {
						log.debug("We can not find any role associate to user "
								+ username);
					}
				} else {
					log.debug("User " + username + " has no any role");
				}
			}
			user.setPassword(null);
			return user;
		}
	}

	@Override
	public SimpleUser findUserByUserNameInAccount(String username,
			Integer accountId) {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setUsername(new StringSearchField(username));
		criteria.setSaccountid(new NumberSearchField(accountId));

		List<SimpleUser> users = userMapperExt.findPagableListByCriteria(
				criteria, new RowBounds(0, Integer.MAX_VALUE));
		if (CollectionUtils.isEmpty(users)) {
			return null;
		} else {
			return users.get(0);
		}
	}

	@Override
	public void pendingUserAccount(String username, Integer accountId) {
		pendingUserAccounts(Arrays.asList(username), accountId);

		// clean cache of related items
		CacheUtils.cleanCaches(accountId, UserService.class);
	}

	private void internalPendingUserAccount(String username, Integer accountId) {
		// check if current user is the unique account owner, then reject
		// deletion
		UserAccountExample userAccountEx = new UserAccountExample();
		userAccountEx.createCriteria().andUsernameEqualTo(username)
				.andAccountidEqualTo(accountId);
		List<UserAccount> accounts = userAccountMapper
				.selectByExample(userAccountEx);
		if (accounts.size() > 0) {
			UserAccount account = accounts.get(0);
			if (account.getIsaccountowner() != null
					&& account.getIsaccountowner() == Boolean.TRUE) {
				userAccountEx = new UserAccountExample();
				userAccountEx.createCriteria().andAccountidEqualTo(accountId)
						.andIsaccountownerEqualTo(Boolean.TRUE);
				if (userAccountMapper.countByExample(userAccountEx) == 1) {
					throw new UserInvalidInputException(
							"Can not delete user "
									+ username
									+ ". The reason is "
									+ username
									+ " is the unique account owner of current account.");
				}
			}
		}

		userAccountEx = new UserAccountExample();
		userAccountEx.createCriteria().andUsernameEqualTo(username)
				.andAccountidEqualTo(accountId);
		UserAccount userAccount = new UserAccount();
		userAccount.setRegisterstatus(RegisterStatusConstants.DELETE);
		userAccountMapper.updateByExampleSelective(userAccount, userAccountEx);

		// notify users are "deleted"
		UserRemovedCommand userRemovedCommand = CamelProxyBuilderUtil.build(
				UserEndpoints.USER_REMOVE_ENDPOINT, UserRemovedCommand.class);
		userRemovedCommand.userRemoved(username, accountId);
	}

	@Override
	public void pendingUserAccounts(List<String> usernames, Integer accountId) {
		for (String username : usernames) {
			internalPendingUserAccount(username, accountId);
		}

		// clean cache of related items
		CacheUtils.cleanCaches(accountId, UserService.class);
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
	public void updateUserAccountStatus(String username, Integer sAccountId,
			String registerStatus) {
		// Update status of user account
		UserAccount userAccount = new UserAccount();
		userAccount.setAccountid(sAccountId);
		userAccount.setUsername(username);
		userAccount.setRegisterstatus(registerStatus);

		UserAccountExample ex = new UserAccountExample();
		ex.createCriteria().andAccountidEqualTo(sAccountId)
				.andUsernameEqualTo(username);
		userAccountMapper.updateByExampleSelective(userAccount, ex);
	}

	@Override
	public void updateUserAccountsStatus(List<String> usernames,
			@CacheKey Integer sAccountId, String registerStatus) {
		for (String username : usernames) {
			updateUserAccountStatus(username, sAccountId, registerStatus);
		}

	}

	@Override
	public int getTotalActiveUsersInAccount(@CacheKey Integer accountId) {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setRegisterStatuses(new SetSearchField<String>(
				new String[] { RegisterStatusConstants.ACTIVE }));
		criteria.setSaccountid(new NumberSearchField(accountId));
		return userMapperExt.getTotalCount(criteria);
	}
}
