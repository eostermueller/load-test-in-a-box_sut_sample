package com.github.eostermueller.tjp2.dataaccess_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.BaseManager;
import com.github.eostermueller.tjp2.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.tjp2.model.Account;
import com.github.eostermueller.tjp2.model.Accounts;
import com.github.eostermueller.tjp2.model.Transaction;


public class AccountMgr4  implements BaseManager {
	private AppContext appContext = null;
	private PkInquiry4 m_pkInquiry4 = null;
	private AppContext getPerfSandboxSingleton() {
		return this.appContext;
	}

	public static SqlTextMgr4 m_sqlTextMgr4 = new SqlTextMgr4();
	public AccountMgr4() {
		this.setAppContext(AppContext.SINGLETON);
	}
	
	public void setAppContext(AppContext val) {
		this.init(val);
	}
	public AppContext getAppContext() {
		return this.appContext;
	}	
	public void init(AppContext val) {
		this.appContext = val;
		this.m_sqlTextMgr4.setLogger(val);
		
		this.m_pkInquiry4 = new PkInquiry4(val);
		
	}
	@Load(
			useCase = "chunkyVersusChattySql", 
			value = {@UserInterfaceDescription("AccountMgr4")}
			)
	public Accounts getAccounts4_oneInqPerAccount() throws PerfSandboxException {
		List<Long> accountIdsCriteria = 
				this.getPerfSandboxSingleton().getRandomAccountIds(
						this.getPerfSandboxSingleton().NUM_ACCOUNTS);
		return this.getAccounts(accountIdsCriteria);
	}
		
	public Accounts getAccounts(List<Long> randomAccountIds) {
		Accounts accounts = new Accounts();
		for( long accountId : randomAccountIds) {
			try {
				Account a = m_pkInquiry4.getAccount(accountId);
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
		val.transactions = getTransactions(val.accountId);
	}
	private List<Transaction> getTransactions(long accountId) throws SQLException, PerfSandboxException  {
		
		Connection con = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null;
		List<Transaction> list = null;
		try {
			con = appContext.getConnection();
			ps = con.prepareStatement( m_sqlTextMgr4.getHistoryByAccountSql() );
			ps.setLong(1, accountId);
			rs = ps.executeQuery();
			list = new ArrayList<Transaction>();
			while(rs.next()) {
				Transaction t = new Transaction();
				t.tellerId = rs.getInt(1);
				t.historyId = rs.getLong(2);
				t.branchId = rs.getInt(3);
				t.accountId = rs.getInt(4);
				t.delta = rs.getLong(5);
				t.mtime = PerfSandboxUtil.getDate(rs, 6); 
				t.filler = rs.getString(7);
				list.add(t);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return list;
	}

}
