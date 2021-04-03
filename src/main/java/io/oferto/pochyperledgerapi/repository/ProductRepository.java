package io.oferto.pochyperledgerapi.repository;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.oferto.pochyperledgerapi.domain.AssetProduct;
import io.oferto.pochyperledgerapi.domain.Product;

@Repository
public class ProductRepository {
	
	ProductRepository() {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}
	
	// helper function for getting connected to the gateway
	private static Gateway connect() throws Exception {
		// Load a file system based wallet for managing identities.		
		Path walletPath = Paths.get("src", "main", "resources", "wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		
		// load a CCP
		Path networkConfigPath = Paths.get("src", "main", "resources", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
		
		return builder.connect();
	}
	
	public List<Product> findAll() throws Exception {
		List<Product> products;
		
		// connect to the network and invoke the smart contract
		try (Gateway gateway = connect()) {
			// get the network and contract
			Network network = gateway.getNetwork("mychannel");
			Contract contract = network.getContract("basic");

			// execute the smart contract
			byte[] result;

			System.out.println("\n");
			result = contract.evaluateTransaction("GetAllAssets");
			System.out.println("Evaluate Transaction: GetAllAssets, result: " + new String(result));
			
			// parse result and return
			Gson gson = new Gson();
			AssetProduct[] assets = gson.fromJson(new String(result), AssetProduct[].class);   
					
			products = new ArrayList<Product>();
			
			for (AssetProduct asset: assets) {
				products.add(asset.getRecord());
			}

			return products;
		}
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}		
	}
}
