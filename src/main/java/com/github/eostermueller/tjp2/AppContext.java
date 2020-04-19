package com.github.eostermueller.tjp2;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.github.eostermueller.tjp2.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.tjp2.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.tjp2.dataaccess_5.ListInquiry;
import com.github.eostermueller.tjp2.dataaccess_5.PkInquiry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

/**
 * The JDBC url (for this class's DataSources) and wiremock URL are specified in -D parameters, ones specified by snail4j
 * @author eoste
 *
 */
public class AppContext implements InitializingBean, ApplicationListener<ContextRefreshedEvent>, Logger {
	public static final long NUM_ACCOUNTS = 10;

	private static final String H2_PORT_JAVA_SYS_PROP_NAME = "snail4j.h2.port"; //
	
	private static final String WIREMOCK_PORT_JAVA_SYS_PROP_NAME = "snail4j.wiremock.port"; //

	private static final String WIREMOCK_HOST_JAVA_SYS_PROP_NAME = "snail4j.wiremock.hostname";

	private static final String H2_HOST_JAVA_SYS_PROP_NAME = "snail4j.h2.hostname";

	public static AppContext SINGLETON = new AppContext();
	
	private int db = 1;
	private int readDataCount = 0;
	public void setFileSystemReadCount(int val) {
		this.readDataCount = val;
	}
	public int getFileSystemReadCount() {
		return this.readDataCount;
	}
	private HttpServer httpServer = new HttpServer();
	public HttpServer getHttpServer() {
		return this.httpServer;
	}
	private BranchInquiry branchInquiry = null;
	public AtomicBoolean backendStarted = new AtomicBoolean(false);
	
	private AppContext() {
		
		try {
			init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		m_pkInquiry_3 = new PkInquiry(this);
		m_listInquiry_3 = new ListInquiry(this); 
	}
	private AppContext init() throws SQLException {
		this.initDataSources();
		
		try {
			//Execute 1 time at startup
			long maxAccountId_01 = queryForMaxAccountId(this.dataSource01.getConnection());
			long maxAccountId_02 = queryForMaxAccountId(this.dataSource02.getConnection());
			this.setMaxAccountId_01( maxAccountId_01 );
			this.setMaxAccountId_02( maxAccountId_02 );
			long maxBranchId_01 = queryForMaxBranchId(this.dataSource01.getConnection());
			long maxBranchId_02 = queryForMaxBranchId(this.dataSource02.getConnection());
			this.setMaxBranchId_01( maxBranchId_01 );
			this.setMaxBranchId_02( maxBranchId_02 );
		} catch (PerfSandboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	private long queryForMaxBranchId(Connection con) throws PerfSandboxException {
		BaseSqlTextMgr bstm = new BaseSqlTextMgr();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long countOfBranches = -1L;
		try {
			ps = con.prepareStatement( bstm.getMaxBranchId() );
			rs = ps.executeQuery();
			rs.next();
			countOfBranches = rs.getLong(1);
			
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return countOfBranches;		
	}
	/**
	 * 
	 * @return
	 * @throws PerfSandboxException
	 */
	public long queryForMaxAccountId(Connection con) throws PerfSandboxException {
		BaseSqlTextMgr bstm = new BaseSqlTextMgr();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long countOfAccounts = -1L;
		try {
			ps = con.prepareStatement( bstm.getMaxAccountId() );
			rs = ps.executeQuery();
			rs.next();
			countOfAccounts = rs.getLong(1);
			
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return countOfAccounts;		
	}
	
	
	private AtomicLong m_maxAccountId_01 = new AtomicLong(20);
	private AtomicLong m_maxAccountId_02 = new AtomicLong(20);
	private AtomicLong m_maxBranchId_01 = new AtomicLong(20);
	private AtomicLong m_maxBranchId_02 = new AtomicLong(20);

	private PkInquiry m_pkInquiry_3 = null;

	private ListInquiry m_listInquiry_3 = null;

	private AtomicBoolean m_logSql = new AtomicBoolean(false);

	public String getWiremockBaseUrl() {
		String url = "http://"  
				+ this.getWiremockHostname()
				+ ":"
				+ this.getWiremockPort()
				+ "/";
		return url;
	}
	public int getWiremockPort() {
			String strPortVal = System.getProperty(WIREMOCK_PORT_JAVA_SYS_PROP_NAME);
			return Integer.parseInt(strPortVal);
	}
	
	public int getH2Port() {
		String strPortVal = System.getProperty(H2_PORT_JAVA_SYS_PROP_NAME);
		return Integer.parseInt(strPortVal);
}
	public String getWiremockHostname() {
		return System.getProperty(WIREMOCK_HOST_JAVA_SYS_PROP_NAME);
	}
	public String getH2Hostname() {
		return System.getProperty(H2_HOST_JAVA_SYS_PROP_NAME);
	}
	/**
	 * Build using parameters documented here:
	 * <pre>https://github.com/brettwooldridge/HikariCP</pre>
	 * @param port the tcp port number H2 was launched on
	 * @param schema the SCHEMA of the H2 database. (S01 and S02) are valid values for snail4j, as of april 5, 2020.
	 * @return
	 */
	private HikariConfig getHikariConfig(int port, String h2Hostname, String schema) {
		String jdbcUrl = "jdbc:h2:tcp://" + h2Hostname + ":" + port + "/perfSandboxDb;SCHEMA=" + schema + ";AUTO_SERVER=TRUE";

		HikariConfig config = new HikariConfig();
		config.setIdleTimeout(42000);
		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName("org.h2.Driver");
		return config;
	}
	/**
	 * "S01" and "S02" are the two schemas that get populated using these instructions:
	 *  https://github.com/eostermueller/javaPerformanceTroubleshooting/wiki/Install-and-Run#step-2-run-the-one-time-init-script
	 *  ...and the init.sh script described there must be run as a prerequisite for building the snail4j uber jar.
	 *  
	 *  FYI, the following two jmeter files are used to populate these two schemas:
	 *  
	 * https://github.com/eostermueller/javaPerformanceTroubleshooting/blob/master/src/test/jmeter/loadDb-01.jmx
	 * https://github.com/eostermueller/javaPerformanceTroubleshooting/blob/master/src/test/jmeter/loadDb-02.jmx
	 */
	private void initDataSources() {
	    //HikariConfig config = new HikariConfig("/hikari01.properties");
		
		HikariConfig config = this.getHikariConfig(this.getH2Port(), this.getH2Hostname(), "S01");
	    
 	    this.dataSource01 = new HikariDataSource(config);
	    log("Hikari JdbcUrl 01 [" + config.getJdbcUrl() + "]");

	    config = this.getHikariConfig(this.getH2Port(), this.getH2Hostname(), "S02");
 	    this.dataSource02 = new HikariDataSource(config);
	    log("Hikari JdbcUrl 02 [" + config.getJdbcUrl() + "]");
	}
 	@Bean(destroyMethod = "close")
	public DataSource getDataSource() throws SQLException {

	    DataSource rc = null;
	    if (this.getDb()==1)
	    	rc = this.dataSource01;
	    else if (this.getDb()==2)
	    	rc = this.dataSource02;
	    
	    return rc;
	}
	public int getMaxBranchId() throws SQLException {
		int rc = -1;
	    if (this.getDb()==1)
	    	rc = this.getMaxBranchId_01();
	    else if (this.getDb()==2)
	    	rc = this.getMaxBranchId_02();
	    
	    return rc;
	}
 	DataSource dataSource01 = null;
 	DataSource dataSource02 = null;
	private Integer backendPort = 8674;
	private int eclipseEmfPooledSaxParseCount;
	private int apacheCommonsPooledSaxParseCount;
	private int unpooledSaxParseCount;
	private int accountCloneCount;


	public Connection getConnection() throws SQLException, PerfSandboxException {
		Connection c = null;
		if (this.db==1)
			c = this.dataSource01.getConnection();
		else if (this.db==2)
			c = this.dataSource02.getConnection();
		else throw new PerfSandboxException("A database number [" + this.db + "] is not supported.  Only types 1 or 2.");
		
		return c;	
	}
	public long getMaxAccountId_01() {
		return this.m_maxAccountId_01.longValue();
	}
	public long getMaxAccountId_02() {
		return this.m_maxAccountId_02.longValue();
	}
	public int getMaxBranchId_01() {
		return this.m_maxBranchId_01.intValue();
	}
	public int getMaxBranchId_02() {
		return this.m_maxBranchId_01.intValue();
	}
	/*
	 * @param numAccounts - number of random accounts to create.
	 */
	public List<Long> getRandomAccountIds(long numAccounts) {
		List<Long> accountIds = new ArrayList<Long>();
		
		for(long i = 0; i < numAccounts; i++) {
			long randomAccountId = getRandomAccountId();
			if (this.getLogSql())
				System.out.println("Of total [" + this.getMaxAccountId()  + "] accounts, just generated [" + randomAccountId  + "]");
			accountIds.add( randomAccountId );
		}
			
		return accountIds;
	}
	public long getRandomAccountId() {
		
		/**
		 * The two data bases will likely have different max account ids.
		 * If a table has only 1000 account ids (1-1000 is how the load script is designed)
		 * then querying for account 1001 would produce and error we want to avoid.
		 */
		
		return (long)ThreadLocalRandom.current().nextDouble(1, getMaxAccountId() );
	}
	private long getMaxAccountId() {
		long max = -1;
		if (this.getDb()==1)
			max = this.getMaxAccountId_01() ;
		else if (this.getDb()==2)
			max = this.getMaxAccountId_02();
		return max;
	}
	public PkInquiry getPkInquiry() {
		return m_pkInquiry_3;
	}
	public ListInquiry getListInquiry() {
		return m_listInquiry_3;
	}
	public void log(String msg) {
		if (this.getLogSql())
			System.out.println(msg);
	}
	public void setLogSql(boolean val) {
		this.m_logSql.set(val);
	}
	public boolean getLogSql() {
		return this.m_logSql.get();
	}
	
	public void setMaxAccountId_01(long val) {
		this.m_maxAccountId_01.set(val);
		log("Set maxAccounts to [" + val + "]");
	}
	public void setMaxAccountId_02(long val) {
		this.m_maxAccountId_02.set(val);
		log("Set maxAccounts to [" + val + "]");
	}
	public void setMaxBranchId_01(long val) {
		this.m_maxBranchId_01.set(val);
		log("Set maxBranch to [" + val + "]");
	}
	public void setMaxBranchId_02(long val) {
		this.m_maxBranchId_02.set(val);
		log("Set maxBranch to [" + val + "]");
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("My startup method");
		
	}
	public void setBackendPort(Integer intPort) {
		this.backendPort = intPort;
	}
	public Integer getBackendPort() {
		return this.backendPort;
	}
	public void setDb(int intValue) throws PerfSandboxException {
		if (intValue <1 || intValue >2)
			throw new PerfSandboxException("only 1 and 2 are valid options for the db.");
		this.db = intValue;
	}
	public int getDb() {
		return this.db;
	}
	public BranchInquiry getBranchInquiry() {
		return this.branchInquiry;
	}
	/*
	public void setBranchScenarioNum(int intBranchScenarioNum, int numBranchInquiriesPerRoundTrip) {
		if (branchInquiry!=null)
			this.branchInquiry.shutdown();
		
		switch (intBranchScenarioNum) {
		case 0:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = null;
			break;
		case 1:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = new UncachedBranchInquiryImpl() {
				public DataSource getDataSource() throws SQLException {
					return AppContext.this.getDataSource();
				}
				public int getMaxBranchId() throws SQLException {
					return AppContext.this.getMaxBranchId();
				}
			};
			break;
		case 2:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = new CachedBranchInquiryImpl(){
				public DataSource getDataSource() throws SQLException {
					return AppContext.this.getDataSource();
				}
				public int getMaxBranchId() throws SQLException {
					return AppContext.this.getMaxBranchId();
				}
			};
			break;
		default:
			this.m_numBranchScenario.set(-1);
			throw new RuntimeException("branchScenarios [" + intBranchScenarioNum + "] was received. 0, 1 and 2 are supported");
		}
		
		if (this.branchInquiry !=null) {
			BaseSqlTextMgr bstm = new BaseSqlTextMgr();
			this.branchInquiry.setTableName(bstm.getTableNames().getBranchTable());
			branchInquiry.setInquiryCount(numBranchInquiriesPerRoundTrip);
		}
		
	}
	*/
	/*
	public String formatStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("<PgBenchStats>");
		//sb.append( this.formatStats(sb) );
		 this.formatStats(sb);
		sb.append("</PgBenchStats>");
		return sb.toString();
	}
	public String formatStats(String stats, long duration) {
		StringBuilder sb = new StringBuilder();
		sb.append("<PgBenchStats>");
		sb.append( stats );
		sb.append("<Duration>").append(duration).append("</Duration>");
		//sb.append( this.formatStats(sb));
		this.formatStats(sb);
		sb.append("</PgBenchStats>");
		return sb.toString();
	}
	*/
	/*
	public String formatStats(StringBuilder sb) {
		sb.append("<Scenario>").append(Integer.toString(getNumScenario())).append("</Scenario>");
		sb.append("<LogSql>").append(getLogSql()).append("</LogSql>");
		sb.append("<Db>").append(getDb()).append("</Db>");
		sb.append("<CpuBusyThread id=\"1\">" ).append( this.getBusyCpuProcessor(1).isStarted() ).append("</CpuBusyThread>");
		sb.append("<CpuBusyThread id=\"2\">" ).append( this.getBusyCpuProcessor(2).isStarted() ).append("</CpuBusyThread>");
		sb.append("<FileSystemReadCount>").append(this.getFileSystemReadCount()).append("</FileSystemReadCount>");
		sb.append("<EclipseEmfPooledSaxParseCount>").append(this.getEclipseEmfPooledSaxParseCount()).append("</EclipseEmfPooledSaxParseCount>");
		sb.append("<ApacheCommonsPooledSaxParseCount>").append(this.getApacheCommonsPooledSaxParseCount()).append("</ApacheCommonsPooledSaxParseCount>");
		sb.append("<UnpooledSaxParseCount>").append(this.getUnpooledSaxParseCount()).append("</UnpooledSaxParseCount>");
		int branchScenario = this.getBranchScenarioNum();
		sb.append("<BranchScenario>").append( branchScenario ).append("</BranchScenario>");
		int branchInquiryPerRoundTrip = -1;
		long totalPhysicalBranchInq = -1;
		
		if (branchScenario > 0 && this.branchInquiry !=null) {
			branchInquiryPerRoundTrip = this.branchInquiry.getInquiryCount();
			totalPhysicalBranchInq  = this.branchInquiry.getTotalPhysicalCount();
		}
		
		sb.append("<BranchInquiryPerRoundTrip>").append( branchInquiryPerRoundTrip ).append("</BranchInquiryPerRoundTrip>");
		sb.append("<BranchInquiryPhysicalCount>").append( totalPhysicalBranchInq ).append("</BranchInquiryPhysicalCount>");
		
		return sb.toString();
	}
	*/
	/** Number of times FOR EACH request that the code will reparse some test XML using the Eclipse EMF parser pool of SAXParser objects. */
	public void setEclipseEmfPooledSaxParseCount(int intValue) {
		this.eclipseEmfPooledSaxParseCount = intValue;
	}
	public int getEclipseEmfPooledSaxParseCount() {
		return this.eclipseEmfPooledSaxParseCount;
	}

	/** Number of times FOR EACH request that the code will reparse some test XML using the the Apache Commons Pool2 pool of SAXParser objects. */
	public void setApacheCommonsPooledSaxParseCount(int intValue) {
		this.apacheCommonsPooledSaxParseCount = intValue;
	}
	public int getApacheCommonsPooledSaxParseCount() {
		return this.apacheCommonsPooledSaxParseCount;
	}

	/** Number of times FOR EACH request that the code will reparse some test XML using a new SAXParser object for every parse. */
	public void setUnpooledSaxParseCount(int intValue) {
		this.unpooledSaxParseCount = intValue;
	}
	public int getUnpooledSaxParseCount() {
		return this.unpooledSaxParseCount;
	}
	public void setAccountCloneCount(int intValue) {
		this.accountCloneCount = intValue;
	}
	public int getAccountCloneCount() {
		return this.accountCloneCount;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	
}
