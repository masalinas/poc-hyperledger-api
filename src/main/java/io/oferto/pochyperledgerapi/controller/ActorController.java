package io.oferto.pochyperledgerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.oferto.pochyperledgerapi.domain.Actor;
import io.oferto.pochyperledgerapi.repository.ActorRepository;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Set of endpoints for actor CA service operations")
public class ActorController {
	@Autowired
	ActorRepository actorRepository;
	
	@PostMapping("/actors/register")
	@ApiOperation(value = "Register an actor in CA", nickname = "register")	
	public ResponseEntity<Actor> register(Actor actor) {
		try {
			Actor actorRegistered = actorRepository.register(actor);
					
		    return new ResponseEntity<>(actorRegistered, HttpStatus.OK);
		} catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/actors/enroll")
	@ApiOperation(value = "Enroll an actor in CA", nickname = "enroll")	
	public ResponseEntity<Actor> enroll(Actor actor) {
		try {
			Actor actorEnrolled = actorRepository.enroll(actor);
					
		    return new ResponseEntity<>(actorEnrolled, HttpStatus.OK);
		} catch (Exception e) {
		    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}