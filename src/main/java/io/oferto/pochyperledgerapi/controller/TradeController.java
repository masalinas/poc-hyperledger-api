package io.oferto.pochyperledgerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.oferto.pochyperledgerapi.domain.Trade;
import io.oferto.pochyperledgerapi.repository.TradeRepository;

@RestController
//@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Api(value = "Set of endpoints for CRUD Trade operations")
public class TradeController {
	@Autowired
	TradeRepository tradeRepository;

	@PostMapping("/trades/initialize")
	@ApiOperation(value = "Initialize with some trades", nickname = "initialize")	
	public ResponseEntity<List<Trade>> initialize() {
		try {
			List<Trade> trades = tradeRepository.initialize();
					
		    return new ResponseEntity<>(trades, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@GetMapping("/trades")
	@ApiOperation(value = "Get all trades", nickname = "getAll")	
	public ResponseEntity<List<Trade>> getAll() {
		try {
			List<Trade> trades = tradeRepository.findAll();
					
		    return new ResponseEntity<>(trades, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@GetMapping("/trades/{id}")
	@ApiOperation(value = "Get trade by id", nickname = "getById")	
	public ResponseEntity<Trade> getById(@PathVariable("id") String id) {
		try {
			Trade trade = tradeRepository.findById(id);
					
		    return new ResponseEntity<>(trade, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@PostMapping("/trades")
	@ApiOperation(value = "Create trade", nickname = "create")	
	public ResponseEntity<Trade> create(@RequestBody Trade trade) {
		try {
			Trade result = tradeRepository.create(trade);
					
		    return new ResponseEntity<>(result, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@PutMapping("/trades/{id}")
	@ApiOperation(value = "Update trade", nickname = "update")	
	public ResponseEntity<Trade> update(@PathVariable("id") String id, @RequestBody Trade trade) {
		try {
			Trade result = tradeRepository.update(id, trade);
					
		    return new ResponseEntity<>(result, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@DeleteMapping("/trades/{id}")
	@ApiOperation(value = "Delete trade", nickname = "delete")	
	public ResponseEntity<String> delete(@PathVariable("id") String id) {
		try {
			tradeRepository.delete(id);
					
		    return new ResponseEntity<>(id, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
	
	@PostMapping("/trades/{id}/{owner}/{value}({price}")
	@ApiOperation(value = "Transfer trade", nickname = "transfer")	
	public ResponseEntity<String> transfer(@PathVariable("id") String id, @RequestBody Trade trade) {
		try {
			tradeRepository.transfer(id, trade.getOwner(), trade.getValue(), trade.getPrice());
					
		    return new ResponseEntity<>(id, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
}
