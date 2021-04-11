package io.oferto.pochyperledgerapi.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Repository;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

@Repository
public class CAConnectorRepository {
	private static final String CA_WALLET = "wallet";
	private static final String CA_CERTIFICATE = "ca.org1.example.com-cert.pem";
	private static final String CA_ALLOW_ALL_HOSTNAMES = "true";
	private static final String CA_HOST = "https://localhost:7054";
	private static final String CA_MSP_ID = "Org1MSP";
	private static final String CA_ADMIN_NAME = "admin";
	private static final String CA_ADMIN_SECRET = "adminpw";
	private static final String CA_ADMIN_AFFILIATION = "org1.department1";	
	private static final String CA_ENROLLMENT_REQUEST_HOST = "host";
	private static final String CA_ENROLLMENT_REQUEST_TLS = "tls";
	
	private HFCAClient hfCAClient;
	
	CAConnectorRepository() throws Exception {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
		
		// connect to CA Service
		this.hfCAClient = connect();
		
		// enroll the admin user if not exist identities in the wallet
		this.enrollAdmin();
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
	
	private void enrollAdmin() throws Exception {
		// load backend wallet
		Path walletPath = Paths.get("src", "main", "resources", CA_WALLET);
		
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);

		// Check to see if we've already enrolled the admin user.
		if (wallet.get(CA_ADMIN_NAME) != null) {
			System.out.println("An identity for the admin user \"" + CA_ADMIN_NAME + "\" already exists in the wallet");
			return;
		}

		// Enroll the admin user, and import the new identity into the wallet.
		final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
		enrollmentRequestTLS.addHost(CA_ENROLLMENT_REQUEST_HOST);	
		enrollmentRequestTLS.setProfile(CA_ENROLLMENT_REQUEST_TLS);
		Enrollment enrollment = getHFCAClient().enroll(CA_ADMIN_NAME, CA_ADMIN_SECRET, enrollmentRequestTLS);
		
		Identity user = Identities.newX509Identity(CA_MSP_ID, enrollment);
		wallet.put(CA_ADMIN_NAME, user);
		
		System.out.println("Successfully enrolled user \"" + CA_ADMIN_NAME + "\" and imported it into the wallet");		
	}

	public HFCAClient getHFCAClient() {
		return this.hfCAClient;
	}

	public String getWallet() {
		return CAConnectorRepository.CA_WALLET;
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
