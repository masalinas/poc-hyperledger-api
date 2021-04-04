package io.oferto.pochyperledgerapi.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;

import io.oferto.pochyperledgerapi.domain.AssetTrade;
import io.oferto.pochyperledgerapi.domain.Trade;

@Repository
public class TradeRepository {
	static final String CHANNEL_NAME = "mychannel";
	static final String CONTRACT_NAME = "trade_v9";
	
	@Autowired
	BlockchainConnectorRepository blockchainConnectorRepository;
	
	public List<Trade> initialize() throws Exception {
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			System.out.println("\n");
			System.out.println("Submit Transaction: initialize");
			contract.submitTransaction("InitLedger");
			
			List<Trade> trades = this.findAll();
			
			return trades;
		}
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
	
	public List<Trade> findAll() throws Exception {
		List<Trade> trades;
		
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);

			// execute the smart contract
			byte[] result;

			System.out.println("\n");
			result = contract.evaluateTransaction("GetAllTrades");
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
			Trade asset = gson.fromJson(new String(result), Trade.class);   
					
			return asset;
		}
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}		
	}
	
	public Trade create(Trade trade) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		trade.setId(UUID.randomUUID().toString());
		trade.setCreationDate(new Date());
		
		try {			
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
						
			// execute the smart contract
			byte[] result;
						
			System.out.println("\n");			
			result = contract.submitTransaction("CreateTrade", trade.getID(), trade.getOwner(), trade.getTradeType(), trade.getValue().toString(), trade.getPrice().toString(), df.format(trade.getCreationDate()));				
			System.out.println("Evaluate Transaction: CreateTrade, result: " + new String(result));
			
			return trade;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
	
	public Trade update(String id, Trade trade) throws Exception {		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		trade.setUpdatedDate(new Date());
		
		try {
			// get the network and contract
			Network network = blockchainConnectorRepository.getGateway().getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CONTRACT_NAME);
			
			// execute the smart contract
			byte[] result;
			
			System.out.println("\n");			
			result = contract.submitTransaction("UpdateTrade", id, trade.getTradeType(), trade.getValue().toString(), trade.getPrice().toString(), sdf.format(trade.getUpdatedDate()));				
			System.out.println("Evaluate Transaction: UpdateTrade, result: " + new String(result));
			
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
			
			// execute the smart contract
			byte[] result;
			
			System.out.println("\n");			
			result = contract.submitTransaction("DeleteTrade", id);				
			System.out.println("Evaluate Transaction: DeleteTrade, result: " + new String(result));
			
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
						
			// execute the smart contract
			byte[] result;
			
			System.out.println("\n");			
			result = contract.submitTransaction("TransferTrade", id, owner, value.toString(), price.toString());				
			System.out.println("Evaluate Transaction: DeleteTrade, result: " + new String(result));
			
			return id;
		}		
		catch(Exception e){
			System.err.println(e);
			
			throw e;
		}
	}
}
