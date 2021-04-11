package io.oferto.pochyperledgerapi.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Service;

import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

@Service
public class CAConnectorService {
	private static final String CA_CERTIFICATE = "ca.org1.example.com-cert.pem";
	private static final String CA_ALLOW_ALL_HOSTNAMES = "true";
	private static final String CA_HOST = "https://localhost:7054";
	private static final String CA_MSP_ID = "Org1MSP";
	private static final String CA_ADMIN_NAME = "admin";
	private static final String CA_ADMIN_SECRET = "adminpw";
	private static final String CA_ADMIN_AFFILIATION = "org1.department1";
	private static final String CA_APPUSER_NAME = "appUser";
	private static final String CA_ENROLLMENT_REQUEST_HOST = "host";
	private static final String CA_ENROLLMENT_REQUEST_TLS = "tls";
	
	private HFCAClient hfCAClient;
	
	CAConnectorService() throws Exception {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
		
		// connect to CA Service
		this.hfCAClient = connect();
	}	
	
	// helper function for getting connected to the gateway
	private static HFCAClient connect() throws Exception {
		// load a org default certification
		Path networkCertPath = Paths.get("src", "main", "resources", CA_CERTIFICATE);
		
		// Create a CA client for interacting with the CA.
		Properties props = new Properties();
		props.put("pemFile", networkCertPath.toFile().toString());
		props.put("allowAllHostNames", CA_ALLOW_ALL_HOSTNAMES);
		
		CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
		
		HFCAClient caClient = HFCAClient.createNewInstance(CA_HOST, props);				
		caClient.setCryptoSuite(cryptoSuite);
		
		return caClient;
	}

	public HFCAClient getHFCAClient() {
		return this.hfCAClient;
	}

	public String getMSPId() {
		return CAConnectorService.CA_MSP_ID;
	}
	
	public String getAdminName() {
		return CAConnectorService.CA_ADMIN_NAME;
	}
	
	public String getAdminSecret() {
		return CAConnectorService.CA_ADMIN_SECRET;
	}
	
	public String getAdminAffiliation() {
		return CAConnectorService.CA_ADMIN_AFFILIATION;
	}
	
	public String getAppUserName() {
		return CAConnectorService.CA_APPUSER_NAME;
	}
	
	public String getEnrollmentRequestHost() {
		return CAConnectorService.CA_ENROLLMENT_REQUEST_HOST;
	}
	
	public String getEnrollmentRequestTls() {
		return CAConnectorService.CA_ENROLLMENT_REQUEST_TLS;
	}
}
