package io.oferto.pochyperledgerapi.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Repository;

import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

@Repository
public class CAConnectorRepository {
	static final String CA_WALLET = "wallet";
	static final String CA_CERTIFICATE = "ca.org1.example.com-cert.pem";
	static final String CA_ALLOW_ALL_HOSTNAMES = "true";
	static final String CA_HOST = "https://localhost:7054";
	static final String CA_MSP_ID = "Org1MSP";
	static final String CA_ADMIN_NAME = "admin";
	static final String CA_ADMIN_SECRET = "adminpw";
	static final String CA_ADMIN_AFFILIATION = "org1.department1";	
	
	private HFCAClient hfCAClient;
	
	CAConnectorRepository() throws Exception {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
		
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

	public String getWallet() {
		return CAConnectorRepository.CA_WALLET;
	}
	
	public String getCertificate() {
		return CAConnectorRepository.CA_CERTIFICATE;
	}
	
	public String getAllowHostanames() {
		return CAConnectorRepository.CA_ALLOW_ALL_HOSTNAMES;
	}
	
	public String getHost() {
		return CAConnectorRepository.CA_HOST;
	}
	
	public String getMSPId() {
		return CAConnectorRepository.CA_MSP_ID;
	}
	
	public String getAdminName() {
		return CAConnectorRepository.CA_ADMIN_NAME;
	}
	
	public String getAdminAffiliation() {
		return CAConnectorRepository.CA_ADMIN_AFFILIATION;
	}
}
