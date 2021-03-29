package com.github.eostermueller.tjp2.dataaccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess_3.AccountMgr3;
import com.github.eostermueller.tjp2.model.Accounts;

@Component
public class AccountMgr9 implements BaseManager {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	AccountMgr3 accountManager3 = null;
	
	public AccountMgr3 getAccountManager3() {
		return accountManager3;
	}
	@Load(
			useCase = "01_Persistence_chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr3")}
			)
	public Accounts getAccounts_bulkAccountInq_oneInqPerAccountForTransHistWithoutPool() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getAppContext().getRandomAccountIds(
						this.getAppContext().NUM_ACCOUNTS);
		return accountManager3.getAccounts(accountIdsCriteria);
	}
	
	public AccountMgr9() {
		this.accountManager3 = new AccountMgr3();
		this.accountManager3.setAppContext( AppContext.SINGLETON_NO_JDBC_CON_POOL);
		LOGGER.info( String.format("@Autowired Found amg5 [%d] and context [%d]", this.getAccountManager3().hashCode(), this.getAccountManager3().getAppContext().hashCode() )  );
		
	}

	@Override
	public AppContext getAppContext() {
		return this.getAccountManager3().getAppContext();
	}
}
