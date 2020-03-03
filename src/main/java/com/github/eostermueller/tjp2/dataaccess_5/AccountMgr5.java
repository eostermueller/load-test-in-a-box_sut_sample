package com.github.eostermueller.tjp2.dataaccess_5;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.BaseManager;
import com.github.eostermueller.tjp2.model.Account;
import com.github.eostermueller.tjp2.model.Accounts;

public class AccountMgr5 implements BaseManager {
	public void setAppContext(AppContext val) {
		this.init(val);
	}
	public AppContext getAppContext() {
		return this.appContext;
	}
	public AccountMgr5() {
		this.setAppContext(AppContext.SINGLETON);
	}

	
	
	public void init(AppContext val) {
		this.appContext = val;
		this.m_sqlTextMgr5.setLogger(val);
		m_pkInquiry = appContext.getPkInquiry();
		m_listInquiry = appContext.getListInquiry();
	}
	private AppContext appContext = null;
	private AppContext getPerfSandboxSingleton() {
		return this.appContext;
	}

	@Load(
			useCase = "chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr5")}
			)
	public Accounts getAccounts_pkInqForHistory() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getPerfSandboxSingleton().getRandomAccountIds(
						this.getPerfSandboxSingleton().NUM_ACCOUNTS);
		return this.getAccounts(accountIdsCriteria);
	}
	private PkInquiry m_pkInquiry;
	private ListInquiry m_listInquiry;
	public static SqlTextMgr5 m_sqlTextMgr5 = new SqlTextMgr5();
	public Accounts getAccounts(List<Long> randomAccountIds) {
		Accounts accounts = new Accounts();
		for( long accountId : randomAccountIds) {
			try {
				Account a = m_pkInquiry.getAccount(accountId);
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
		List<Long> historyIds = m_listInquiry.getTransactions(val.accountId);
		for( long historyId : historyIds) {
			val.transactions.add(m_pkInquiry.getTransaction(historyId));
		}
	}

}
