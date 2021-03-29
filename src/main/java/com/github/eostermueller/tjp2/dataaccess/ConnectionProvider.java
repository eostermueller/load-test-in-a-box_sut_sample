package com.github.eostermueller.tjp2.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.eostermueller.tjp2.PerfSandboxException;

public interface ConnectionProvider {
	Connection getConnection() throws SQLException, PerfSandboxException;
}
