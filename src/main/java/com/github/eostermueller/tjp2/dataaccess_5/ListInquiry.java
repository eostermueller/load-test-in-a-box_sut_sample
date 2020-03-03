package com.github.eostermueller.tjp2.dataaccess_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;
import com.github.eostermueller.tjp2.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.tjp2.model.Transaction;

public class ListInquiry {

	public ListInquiry(AppContext val) {
		this.pgBench = val;
	}
	AppContext pgBench = null;
	public List<Long> getTransactions(long accountId) throws SQLException, PerfSandboxException  {
		
		Connection con = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null;
		List<Long> list = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr5.m_sqlTextMgr5.getHistoryListSql() );
			ps.setLong(1, accountId);
			rs = ps.executeQuery();
			list = new ArrayList<Long>();
			while(rs.next()) {
				list.add ( rs.getLong(1));
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return list;
	}
		
}
