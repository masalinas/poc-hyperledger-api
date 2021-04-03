package io.oferto.pochyperledgerapi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import io.oferto.pochyperledgerapi.domain.AssetTrade;
import io.oferto.pochyperledgerapi.domain.Trade;

@Repository
public class TradeRepository {
	
	@Autowired
	BlockchainConnectorRepository blockchainConnectorRepository;
	
	public List<Trade> findAll() throws Exception {
		List<Trade> trades;
		
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork("mychannel");
			Contract contract = network.getContract("basic");

			// execute the smart contract
			byte[] result;

			System.out.println("\n");
			result = contract.evaluateTransaction("GetAllAssets");
			System.out.println("Evaluate Transaction: GetAllTrades, result: " + new String(result));
			
			// parse result and return
			Gson gson = new Gson();
			AssetTrade[] assets = gson.fromJson(new String(result), AssetTrade[].class);   
					
			trades = new ArrayList<Trade>();
			
			for (AssetTrade asset: assets) {
				trades.add(asset.getRecord());
			}

			return trades;
		}
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}		
	}
	
	public Trade create(Trade trade) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork("mychannel");
			Contract contract = network.getContract("basic");
			
			UUID uuid = UUID.randomUUID();
			
			System.out.println("\n");
			System.out.println("Submit Transaction: CreateAsset " + trade.toString());
			//CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5 and appraisedValue of 1300
			contract.submitTransaction("CreateAsset", "asset13", "yellow", "5", "Tom", "1300");				

			trade.setId(uuid);
			
			return trade;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
}
