
package com.sample;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.MFPJAXRSApplication;
import org.lightcouch.CouchDbException;

import javax.ws.rs.core.Context;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudantJavaApplication extends MFPJAXRSApplication{

	static Logger logger = Logger.getLogger(CloudantJavaApplication.class.getName());

	@Context
	ConfigurationAPI configurationAPI;

	public Database db = null;

	protected void init() throws Exception {
		logger.warning(this.getClass()+ " adapter initialized!");
	}

	public void initConnection() {
		String cloudantDBName = configurationAPI.getPropertyValue("DBName");
		String cloudantAccount = configurationAPI.getPropertyValue("account");
		String cloudantKey = configurationAPI.getPropertyValue("key");
		String cloudantPassword = configurationAPI.getPropertyValue("password");

		if (!cloudantDBName.isEmpty() && !cloudantAccount.isEmpty() && !cloudantKey.isEmpty() && !cloudantPassword.isEmpty()){
			try {
				CloudantClient cloudantClient = new CloudantClient(cloudantAccount,cloudantKey,cloudantPassword);
				db = cloudantClient.database(cloudantDBName, false);
			} catch (CouchDbException e) {
				System.out.println(e.getMessage());
				
			}
		}
	}

	protected void destroy() throws Exception {
		logger.info("Adapter destroyed!");
	}

	protected String getPackageToScan() {
		// The package of this class will be scanned (recursively) to find JAX-RS
		// resources.
		// It is also possible to override "getPackagesToScan" method in order to return
		// more than one package for scanning
		return getClass().getPackage().getName();
	}
}
