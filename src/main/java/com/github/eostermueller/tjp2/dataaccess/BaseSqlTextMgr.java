package com.github.eostermueller.tjp2.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.tjp2.Logger;
import com.github.eostermueller.tjp2.AppContext;

public class BaseSqlTextMgr {
	private Logger m_logger;
	public void setLogger(Logger logger) {
		this.m_logger = logger;
	}
	public TableNames getTableNames() {
		return this.m_tableNames;
	}
	protected TableNames m_tableNames = new TableNames();
	@Autowired
	private AppContext pgBench = null;

       public String getMaxAccountId() {
                return "select max(aid) from " + m_tableNames.getAccountTable();
        }
       public String getMaxBranchId() {
           return "select max(bid) from " + m_tableNames.getBranchTable();
   }


	public String getBranchCount() {
		return "select count(*) from " + m_tableNames.getBranchTable();
	}
	public void logSql(String sql) {
		if (this.m_logger!=null)
			this.m_logger.log(sql);
	}
}
