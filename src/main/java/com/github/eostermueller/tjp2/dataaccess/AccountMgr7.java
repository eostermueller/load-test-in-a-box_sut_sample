package com.github.eostermueller.tjp2.dataaccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess_5.AccountMgr5;
import com.github.eostermueller.tjp2.model.Accounts;

@Component
public class AccountMgr7 implements BaseManager {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	AccountMgr5 accountManager5 = null;
	
	public AccountMgr5 getAccountManager5() {
		return accountManager5;
	}
	
	@Load(
			useCase = "01_Persistence_chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr7")}
			)
	public Accounts getAccounts_pkInqForHistoryWithoutPool() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this
					.getAppContext()
					.getRandomAccountIds(
						this.getAppContext().NUM_ACCOUNTS
						);
		
		return this
				.getAccountManager5()
				.getAccounts(accountIdsCriteria);
	}
	public AccountMgr7() {
		this.accountManager5 = new AccountMgr5();
		this.accountManager5.setAppContext( AppContext.SINGLETON_NO_JDBC_CON_POOL);
		LOGGER.info( String.format("@Autowired Found amg5 [%d] and context [%d]", this.getAccountManager5().hashCode(), this.getAccountManager5().getAppContext().hashCode() )  );
		
	}

	@Override
	public AppContext getAppContext() {
		return this.getAccountManager5().getAppContext();
	}
}
