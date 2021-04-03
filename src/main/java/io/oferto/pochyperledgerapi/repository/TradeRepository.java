package io.oferto.pochyperledgerapi.repository;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import io.oferto.pochyperledgerapi.domain.AssetTrade;
import io.oferto.pochyperledgerapi.domain.Trade;

@Repository
public class TradeRepository {
	static final String CHANNEL_NAME = "mychannel";
	static final String CONTRACT_NAME = "trade";
	
	@Autowired
	BlockchainConnectorRepository blockchainConnectorRepository;
	
	public List<Trade> findAll() throws Exception {
		List<Trade> trades;
		
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);

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
	
	public Trade findById(String id) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);

			// execute the smart contract
			byte[] result;

			System.out.println("\n");
			result = contract.evaluateTransaction("ReadTrade", id);
			System.out.println("Evaluate Transaction: ReadTrade, result: " + new String(result));
			
			// parse result and return
			Gson gson = new Gson();
			AssetTrade asset = gson.fromJson(new String(result), AssetTrade.class);   
					
			return asset.getRecord();
		}
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}		
	}
	
	public Trade create(Trade trade) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			System.out.println("\n");
			System.out.println("Submit Transaction: CreateAsset " + trade.toString());
			contract.submitTransaction("CreateTrade", trade.getOwner(), trade.getTradeType(), trade.getValue().toString(), trade.getPrice().toString(), trade.getCreationDate().toString());				
			
			return trade;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
	
	public Trade update(String id, Trade trade) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			System.out.println("\n");
			System.out.println("Submit Transaction: UpdateTrade " + trade.toString());
			contract.submitTransaction("UpdateTrade", id, trade.getTradeType(), trade.getValue().toString(), trade.getPrice().toString(), trade.getCreationDate().toString());				
			
			return trade;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
	
	public String delete(String id) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			System.out.println("\n");
			System.out.println("Submit Transaction: DeleteTrade id" + id);
			contract.submitTransaction("DeleteTrade", id);				
			
			return id;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
	
	public String transfer(String id, String owner, Float value, Float price) throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			System.out.println("\n");
			System.out.println("Submit Transaction: TransferTrade id" + id);
			contract.submitTransaction("TransferTrade", id, owner, value.toString(), price.toString());				
			
			return id;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
}
