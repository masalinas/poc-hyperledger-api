package io.oferto.pochyperledgerapi.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.annotation.PostConstruct;

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
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import io.oferto.pochyperledgerapi.domain.Actor;
import io.oferto.pochyperledgerapi.service.CAConnectorService;

@Repository
public class ActorRepository {	
	@Autowired
	CAConnectorService caConnectorService;
		
	@PostConstruct
	public void init(){
		this.initializeWallet();
	}
		
	private void initializeWallet() {
		try {			
			// enroll the admin user if not exist
			this.enrollAdmin();	
		} catch (Exception e) {
			// do nothing			
		}
		
		try {
			Actor actorApppUser = new Actor();
			actorApppUser.setName(caConnectorService.getAppUserName());
			
			// register appUser
			this.register(actorApppUser);
			
			// enroll appUser
			this.enroll(actorApppUser);		
		} catch (Exception e) {
			// do nothing is appUser is already enrolled			
		}
	}
	
	private void enrollAdmin() throws Exception {
		// load backend wallet
		Path walletPath = Paths.get("src", "main", "resources", caConnectorService.getWallet());
		
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);

		// Check to see if we've already enrolled the admin user.
		if (wallet.get(caConnectorService.getAdminName()) != null) {
			System.out.println("An identity for the user \"" + caConnectorService.getAdminName() + "\" already exists in the wallet");
			return;
		}

		// Enroll the admin user, and import the new identity into the wallet.
		final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
		enrollmentRequestTLS.addHost(caConnectorService.getEnrollmentRequestHost());	
		enrollmentRequestTLS.setProfile(caConnectorService.getEnrollmentRequestTls());
		Enrollment enrollment = caConnectorService.getHFCAClient().enroll(caConnectorService.getAdminName(), caConnectorService.getAdminSecret(), enrollmentRequestTLS);
		
		Identity user = Identities.newX509Identity(caConnectorService.getMSPId(), enrollment);
		wallet.put(caConnectorService.getAdminName(), user);
		
		System.out.println("Successfully enrolled \"" + caConnectorService.getAdminName() + "\" and imported it into the wallet");		
	}
	
	public Actor register(Actor actor) throws Exception {
		// load backend wallet from configuration
		Path walletPath = Paths.get("src", "main", "resources", caConnectorService.getWallet());
				 
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
	
		// Check to see if we've already enrolled the user.
		if (wallet.get(actor.getName()) != null) {
			System.out.println("An identity for the user \"" + actor.getName() + "\" already exists in the wallet");
			
			throw new Exception("An identity for the user \"" + actor.getName() + "\" already exists in the wallet");
		}
				
		X509Identity adminIdentity = (X509Identity)wallet.get(caConnectorService.getAdminName());
		
		if (adminIdentity == null) {
			System.out.println("\"" + caConnectorService.getAdminName() + "\" needs to be enrolled and added to the wallet first");
			
			throw new Exception("\"" + caConnectorService.getAdminName() + "\" needs to be enrolled and added to the wallet first");
		}
			
		// create admin user to send request register user 
		User admin = new User() {
			@Override
			public String getName() {
				return caConnectorService.getAdminName();
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
				return caConnectorService.getAdminAffiliation();
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
				return caConnectorService.getMSPId();
			}
		};
		
		// Register the user, enroll the user, and import the new identity into the wallet.
		RegistrationRequest registrationRequest = new RegistrationRequest(actor.getName());
		registrationRequest.setAffiliation(actor.getAffiliation());
		registrationRequest.setEnrollmentID(actor.getName());
		
		String enrollmentSecret = caConnectorService.getHFCAClient().register(registrationRequest, admin);
		
		actor.setSecret(enrollmentSecret);
		
		return actor;
	}
	
	public Actor enroll(Actor actor) throws Exception {
		// load backend wallet from configuration
		Path walletPath = Paths.get("src", "main", "resources", caConnectorService.getWallet());
				 
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
	
		// Check to see if we've already enrolled the user.
		if (wallet.get(actor.getName()) != null) {
			System.out.println("An identity for the user \"" + actor.getName() + "\" already exists in the wallet");
			
			throw new Exception("An identity for the user \"" + actor.getName() + "\" already exists in the wallet");
		}
		
		// enrollment the actor in CA Server
		Enrollment enrollment = caConnectorService.getHFCAClient().enroll(actor.getName(), actor.getSecret());
		
		// create the wallet identity from actor enrollmented
		Identity user = Identities.newX509Identity(caConnectorService.getMSPId(), enrollment);
				
		// persist the identity in the wallet
		wallet.put(actor.getName(), user);
		
		return actor;
	}	
}
