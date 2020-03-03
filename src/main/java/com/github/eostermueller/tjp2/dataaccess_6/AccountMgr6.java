package com.github.eostermueller.tjp2.dataaccess_6;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.BaseManager;
import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.model.Account;
import com.github.eostermueller.tjp2.model.Accounts;

public class AccountMgr6 implements BaseManager {
	public AccountMgr6() {
		this.setAppContext(AppContext.SINGLETON);
	}

	private AppContext appContext = null;

	@Override
	public AppContext getAppContext() {
		return this.appContext;
	}
	public void setAppContext(AppContext val) {
		this.init(val);
	}
	
	public void init(AppContext val) {
		this.appContext = val;
	}

	@Load(
			useCase = "chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr6")}
			)
	public Accounts getAccounts_zeroDbAccess() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getAppContext().getRandomAccountIds(
						this.getAppContext().NUM_ACCOUNTS);
		return this.getAccounts(accountIdsCriteria);
	}

	public Accounts getAccounts(List<Long> accountIds_criteria) {
		
		Accounts accounts = new Accounts();
		for(Long acctId : accountIds_criteria) {
			accounts.addAccount( Account.createFake(acctId) );
		}
		return accounts;
	}
}
