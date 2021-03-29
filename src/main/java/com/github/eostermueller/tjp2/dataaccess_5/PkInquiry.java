package com.github.eostermueller.tjp2.dataaccess_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.tjp2.model.Account;
import com.github.eostermueller.tjp2.model.Accounts;
import com.github.eostermueller.tjp2.model.Branch;
import com.github.eostermueller.tjp2.model.Transaction;

public class PkInquiry {
	public Connection getConnection() throws SQLException, PerfSandboxException {
		return this.ctx.getConnection();
	}

	public PkInquiry(AppContext val) {
		this.ctx = val;
	}
	private AppContext ctx;
	public Account getAccount(long accountId) throws SQLException, PerfSandboxException {
		Account account = new Account();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement( AccountMgr5.sqlTextMgr5.getAccountPkInquirySql() );
			ps.setLong(1, accountId);
			
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + accountId + "]");
				account.accountId = rs.getLong(1);
				account.branchId = rs.getInt(2);
				account.balance = rs.getLong(3);
				account.filler = rs.getString(4);
				account.filler01 = rs.getString(5);
				account.filler02 = rs.getString(6);
				account.filler03 = rs.getString(7);
				account.filler04 = rs.getString(8);
				account.filler05 = rs.getString(9);
				account.filler06 = rs.getString(10);
				account.filler07 = rs.getString(11);
				account.filler08 = rs.getString(12);
				account.filler09 = rs.getString(13);
				account.filler10 = rs.getString(14);
				account.filler11 = rs.getString(15);
				account.filler12 = rs.getString(16);
				account.filler13 = rs.getString(17);
				account.filler14 = rs.getString(18);
				account.filler15 = rs.getString(19);
				account.filler16 = rs.getString(20);
				account.filler17 = rs.getString(21);
				account.filler18 = rs.getString(22);
				account.filler19 = rs.getString(23);
				account.filler20 = rs.getString(24);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return account;
	}
	public Branch getBranch(int branchId) throws SQLException, PerfSandboxException {
		Branch branch = new Branch();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement( AccountMgr5.sqlTextMgr5.getBranchPkInquirySql() );
			ps.setInt(1, branchId);
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + branchId + "]");
				branch.bid = rs.getInt(1);
				branch.bbalance = rs.getLong(2);
				branch.filler = rs.getString(3);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return branch;
	}
	public Transaction getTransaction(long transactionId) throws SQLException, PerfSandboxException {
		Transaction transaction = new Transaction();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			ps = con.prepareStatement( AccountMgr5.sqlTextMgr5.getHistoryPkInquirySql() );
			ps.setLong(1, transactionId);
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + transactionId + "]");
				//		return "SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE tid = ?";
				transaction.tellerId = rs.getInt(1);
				transaction.historyId = rs.getLong(2);
				transaction.branchId = rs.getInt(3);
				transaction.accountId = rs.getLong(4);
				transaction.delta = rs.getLong(5);
				transaction.mtime = PerfSandboxUtil.getDate(rs, 6);
				transaction.filler = rs.getString(7);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
			return transaction;
		}
}
