package com.github.eostermueller.tjp2.dataaccess_4;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.tjp2.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.tjp2.dataaccess.TableNames;

public class SqlTextMgr4 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getHistoryByAccountSql() {
		this.m_stats.m_historyByAccountSql.incrementAndGet();
		String sql ="SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	public String getAccountPkInquirySql() {
		this.m_stats.m_accountPkInquirySql.incrementAndGet();
		String sql = "SELECT aid, bid, abalance, filler, filler01, filler02, filler03, filler04, filler05, filler06, filler07, filler08, filler09, filler10, filler11, filler12, filler13, filler14, filler15, filler16, filler17, filler18, filler19, filler20 from " + m_tableNames.getAccountTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	
	public static class Stats {
		public AtomicLong m_accountPkInquirySql = new AtomicLong();
		public AtomicLong m_historyByAccountSql = new AtomicLong();
		public String getXmlStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("<historyByAccountSql>" + m_historyByAccountSql  + "</historyByAccountSql>");
			sb.append("<accountPkInqSql>" + m_accountPkInquirySql  + "</accountPkInqSql>");
			return sb.toString();
		}
	
	}
	
}
