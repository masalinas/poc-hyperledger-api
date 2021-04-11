package io.oferto.pochyperledgerapi.domain;

import java.security.PrivateKey;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class Actor {	
	@NotBlank(message = "Actor firstname is mandatory")
    @ApiModelProperty(notes = "Actor firstname", example = " Miguel Angel", required = true, position = 0)
	private String firstName;
    
    @NotBlank(message = "Actor lastname is mandatory")
    @ApiModelProperty(notes = "Actor lastname", example = "Salinas Gancedo", required = true, position = 1)
	private String lastName;

    @NotBlank(message = "Actor CA username is mandatory")
    @ApiModelProperty(notes = "Actor CA username", example = "appUser", required = true, position = 2)
	private String name;
    
    @ApiModelProperty(notes = "Actor CA secret", example = "appUserPwd", position = 3)
	private String secret;
    
    @ApiModelProperty(notes = "Actor CA roles", example = "['admin', 'user']", position = 4)
	private Set<String> roles;
    
    @ApiModelProperty(notes = "Actor CA account", example = "appUser", position = 5)
	private String account;

    @ApiModelProperty(notes = "Actor CA affiliation", example = "org1.department1", position = 6)
	private String affiliation;
    
    @ApiModelProperty(notes = "Actor CA key", example = "", position = 7)
	private PrivateKey key;
    
    @ApiModelProperty(notes = "Actor CA key", example = "", position = 7)
	private String cert;
        
    @ApiModelProperty(notes = "Actor CA MSP Id", example = "Org1MSP", position = 8)
	private String mspId;
    
    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public PrivateKey getKey() {
		return key;
	}

	public void setKey(PrivateKey key) {
		this.key = key;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

}
