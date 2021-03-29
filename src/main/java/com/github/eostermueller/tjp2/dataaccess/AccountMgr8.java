package com.github.eostermueller.tjp2.dataaccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess_2.AccountMgr2;
import com.github.eostermueller.tjp2.model.Accounts;
@Component
public class AccountMgr8 implements BaseManager {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	AccountMgr2 accountManager2 = null;
	
	public AccountMgr2 getAccountManager2() {
		return accountManager2;
	}
	@Load(
			useCase = "01_Persistence_chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr2")}
			)
	public Accounts getAccounts_bulkAccountBulkTransInqWithoutPool() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getAppContext().getRandomAccountIds(
						this.getAppContext().NUM_ACCOUNTS);
		return this.getAccountManager2().getAccounts(accountIdsCriteria);
	}
	
	public AccountMgr8() {
		this.accountManager2 = new AccountMgr2();
		this.accountManager2.setAppContext( AppContext.SINGLETON_NO_JDBC_CON_POOL);
		LOGGER.info( String.format("@Autowired Found amg5 [%d] and context [%d]", this.getAccountManager2().hashCode(), this.getAccountManager2().getAppContext().hashCode() )  );
		
	}

	@Override
	public AppContext getAppContext() {
		return this.getAccountManager2().getAppContext();
	}
}
