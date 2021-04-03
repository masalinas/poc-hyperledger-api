package io.oferto.pochyperledgerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.oferto.pochyperledgerapi.domain.Trade;
import io.oferto.pochyperledgerapi.repository.TradeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Api(value = "Set of endpoints for CRUD Trade operations")
public class TradeController {
	@Autowired
	TradeRepository tradeRepository;
	
	@GetMapping("/trades")
	@ApiOperation(value = "Get all products", nickname = "getAllTrades")	
	public ResponseEntity<List<Trade>> getAllTrades() {
		try {
			List<Trade> trades = tradeRepository.findAll();
					
		    return new ResponseEntity<>(trades, HttpStatus.OK);
		  } catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	}
}
