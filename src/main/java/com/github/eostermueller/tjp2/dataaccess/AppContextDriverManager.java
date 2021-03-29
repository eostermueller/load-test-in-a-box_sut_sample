package com.github.eostermueller.tjp2.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;

public class AppContextDriverManager extends AppContext {
	public AppContextDriverManager() {
		super();
	}
	public Connection getConnection() throws SQLException, PerfSandboxException {
		return this.createDriverManagerConnection();
	}

}
