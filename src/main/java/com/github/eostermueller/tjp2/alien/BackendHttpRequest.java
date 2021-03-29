package com.github.eostermueller.tjp2.alien;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.github.eostermueller.snail4j.workload.annotations.Load;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;
import com.github.eostermueller.tjp2.AppContext;
import com.github.eostermueller.tjp2.PerfSandboxException;

public class BackendHttpRequest {

	private static final String SUCCESS = "success";
	private AppContext appContext;

	private void setAppContext(AppContext val) {
		this.appContext = val;
	}
	
	private AppContext getAppContext() {
		return this.appContext;
	}
	public BackendHttpRequest() {
		this.setAppContext(AppContext.SINGLETON_HIKARI_JDBC_CON_POOL);
	}
	
	@Load(
			useCase = "02_AlienSystems", 
			value = {@UserInterfaceDescription("http-response_noWait")}
			)
	public String getBackendData_noWait() throws IOException, PerfSandboxException {
		return this.getHttpGetRequest(
				this.getAppContext().getWiremockBaseUrl() + "/noDelay", SUCCESS
				);	
	}

	@Load(
			useCase = "02_AlienSystems", 
			value = {@UserInterfaceDescription("http-response_delay_25ms")}
			)
	public String getBackendData_delay_25ms() throws IOException, PerfSandboxException {
		return this.getHttpGetRequest(
				this.getAppContext().getWiremockBaseUrl() + "/delay_25ms", SUCCESS
				);	
	}
	@Load(
			useCase = "02_AlienSystems", 
			value = {@UserInterfaceDescription("http-response_delay_1s")}
			)
	public String getBackendData_delay_1s() throws IOException, PerfSandboxException {
		return this.getHttpGetRequest(
				this.getAppContext().getWiremockBaseUrl() + "/delay_1s", SUCCESS
				);	
	}
	
	@Load(
			useCase = "02_AlienSystems", 
			value = {@UserInterfaceDescription("http-response_delay_10s")}
			)
	public String getBackendData_delay_10s() throws IOException, PerfSandboxException {
		return this.getHttpGetRequest(
				this.getAppContext().getWiremockBaseUrl() + "/delay_10s", "success"
				);	
	}
	
	public String getHttpGetRequest(String url, String validation) throws IOException, PerfSandboxException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		
		String rc = response.toString();
		
		if (rc.indexOf(validation) <0) {
			throw new PerfSandboxException("HTTP call failed.  Validation text [" + validation  + "] not found.");
		}
		
		
		return rc;				
	}
}
