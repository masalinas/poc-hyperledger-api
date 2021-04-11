package io.oferto.pochyperledgerapi.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import io.oferto.pochyperledgerapi.domain.Actor;

@Repository
public class ActorRepository {
	static final String ENROLLMENT_REQUEST_HOST = "host";
	static final String ENROLLMENT_REQUEST_TLS = "tls";
	static final String ENROLLMENT_REQUEST_ADMIN_NAME = "admin";
	static final String ENROLLMENT_REQUEST_ADMIN_SECRET = "adminpw";
	
	@Autowired
	CAConnectorRepository caConnectorRepository;
	
	public Actor register(Actor actor) throws Exception {
		// load backend wallet from configuration
		Path walletPath = Paths.get("src", "main", "resources", caConnectorRepository.getWallet());
				 
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
	
		// Check to see if we've already enrolled the user.
		if (wallet.get(actor.getName()) != null) {
			System.out.println("An identity for the user \"" + actor.getName() + "\" already exists in the wallet");
			
			throw new Exception("An identity for the user \"appUser\" already exists in the wallet");
		}
				
		X509Identity adminIdentity = (X509Identity)wallet.get(ENROLLMENT_REQUEST_ADMIN_NAME);
		
		if (adminIdentity == null) {
			System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
			
			throw new Exception("\"admin\" needs to be enrolled and added to the wallet first");
		}
			
		// create admin user to send request register user 
		User admin = new User() {
			@Override
			public String getName() {
				return caConnectorRepository.getAdminName();
			}

			@Override
			public Set<String> getRoles() {
				return null;
			}

			@Override
			public String getAccount() {
				return null;
			}

			@Override
			public String getAffiliation() {
				return caConnectorRepository.getAdminAffiliation();
			}

			@Override
			public Enrollment getEnrollment() {
				return new Enrollment() {

					@Override
					public PrivateKey getKey() {
						return adminIdentity.getPrivateKey();
					}

					@Override
					public String getCert() {
						return Identities.toPemString(adminIdentity.getCertificate());
					}
				};
			}

			@Override
			public String getMspId() {
				return caConnectorRepository.getMSPId();
			}
		};
		
		// Register the user, enroll the user, and import the new identity into the wallet.
		RegistrationRequest registrationRequest = new RegistrationRequest(actor.getName());
		registrationRequest.setAffiliation(actor.getAffiliation());
		registrationRequest.setEnrollmentID(actor.getName());
		
		String enrollmentSecret = caConnectorRepository.getHFCAClient().register(registrationRequest, admin);
		
		actor.setSecret(enrollmentSecret);
		
		return actor;
	}
	
	public Actor enroll(Actor actor) throws Exception {	
		// enrollment the actor in CA Server
		Enrollment enrollment = caConnectorRepository.getHFCAClient().enroll(actor.getName(), actor.getSecret());
		
		// create the wallet identity from actor enrollmented
		Identity user = Identities.newX509Identity(caConnectorRepository.getMSPId(), enrollment);
		
		// pèrsist the new identity in the wallet
		// load backend wallet from configuration
		Path walletPath = Paths.get("src", "main", "resources", caConnectorRepository.getWallet());
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
				
		wallet.put(actor.getName(), user);
		
		return actor;
	}	
}
