package io.oferto.pochyperledgerapi.repository;

import org.springframework.stereotype.Repository;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class BlockchainConnectorRepository {
	static final String IDENTITY_USERNAME = "appUser";
	
	private Gateway gateway;
	
	BlockchainConnectorRepository() throws Exception {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
		
		this.gateway = connect();
	}
	
	// helper function for getting connected to the gateway
	private static Gateway connect() throws Exception {
		// Load a file system based wallet for managing identities.		
		Path walletPath = Paths.get("src", "main", "resources", "wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		
		// load a CCP
		Path networkConfigPath = Paths.get("src", "main", "resources", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, IDENTITY_USERNAME).networkConfig(networkConfigPath).discovery(true);
				
		return builder.connect();
	}
	
	public Gateway getGateway() {
		return gateway;
	}
}
