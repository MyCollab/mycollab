package com.esofthead.mycollab.module.user.service.test;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esofthead.mycollab.module.user.domain.AccountTheme;
import com.esofthead.mycollab.module.user.service.AccountThemeService;


public class AccountThemeServiceImpl implements AccountThemeService {

	@Override
	public int saveWithSession(AccountTheme record, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateWithSession(AccountTheme record, String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateWithSessionWithSelective(AccountTheme record,
			String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void massUpdateWithSession(AccountTheme record,
			List<Integer> primaryKeys, int accountId) {
		// TODO Auto-generated method stub

	}

	@Override
	public AccountTheme findByPrimaryKey(Integer primaryKey, int sAccountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeWithSession(Integer primaryKey, String username,
			int sAccountId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void massRemoveWithSession(List<Integer> primaryKeys,
			String username, int sAccountId) {
		// TODO Auto-generated method stub

	}

	@Override
	public AccountTheme getAccountTheme(int saccountid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountTheme getDefaultTheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveAccountTheme(AccountTheme theme, int saccountid) {
		// TODO Auto-generated method stub

	}

}
