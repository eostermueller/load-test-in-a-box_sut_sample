package com.github.eostermueller.tjp2.dataaccess_5;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.BaseManager;
import com.github.eostermueller.tjp2.model.Account;
import com.github.eostermueller.tjp2.model.Accounts;
import org.springframework.stereotype.Component;

@Component
public class AccountMgr5 implements BaseManager {
	protected PkInquiry pkInquiry;
	protected ListInquiry listInquiry;
	public ListInquiry getListInquiry() {
		return listInquiry;
	}
	public void setListInquiry(ListInquiry listInquiry) {
		this.listInquiry = listInquiry;
	}
	public static SqlTextMgr5 sqlTextMgr5 = new SqlTextMgr5();
	
	public void setAppContext(AppContext val) {
		this.init(val);
	}
	public AppContext getAppContext() {
		return this.appContext;
	}
	public AccountMgr5() {
		this.setAppContext(AppContext.SINGLETON_HIKARI_JDBC_CON_POOL);
	}
	public void init(AppContext val) {
		this.appContext = val;
		this.sqlTextMgr5.setLogger(val);
		setPkInquiry(appContext.getPkInquiry() );
		this.setListInquiry(appContext.getListInquiry());
	}
	public void setPkInquiry(PkInquiry val) {
		this.pkInquiry = val;
	}
	public PkInquiry getPkInquiry() {
		return this.pkInquiry;
	}
	protected AppContext appContext = null;
	protected AppContext getPerfSandboxSingleton() {
		return this.appContext;
	}

	@Load(
			useCase = "01_Persistence_chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr5")}
			)
	public Accounts getAccounts_pkInqForHistory() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getPerfSandboxSingleton().getRandomAccountIds(
						this.getPerfSandboxSingleton().NUM_ACCOUNTS);
		return this.getAccounts(accountIdsCriteria);
	}
	public Accounts getAccounts(List<Long> randomAccountIds) {
		Accounts accounts = new Accounts();
		for( long accountId : randomAccountIds) {
			try {
				Account a = getPkInquiry().getAccount(accountId);
				getAccountHistory(a);
				accounts.addAccount( a );
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (PerfSandboxException e) {
				e.printStackTrace();
			}
		}
		return accounts;
	}
	public void getAccountHistory(Account val) throws SQLException, PerfSandboxException {
		List<Long> historyIds = listInquiry.getTransactions(val.accountId);
		for( long historyId : historyIds) {
			val.transactions.add(getPkInquiry().getTransaction(historyId));
		}
	}

}
