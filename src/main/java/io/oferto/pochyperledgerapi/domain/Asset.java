package io.oferto.pochyperledgerapi.domain;

public class Asset {
	private String Key;
	private Product Record;
	
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public Product getRecord() {
		return Record;
	}
	public void setRecord(Product record) {
		Record = record;
	}
}
