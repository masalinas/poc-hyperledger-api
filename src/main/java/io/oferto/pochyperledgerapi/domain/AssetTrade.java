package io.oferto.pochyperledgerapi.domain;

public class AssetTrade {
	private String Key;
	private Trade Record;
	
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public Trade getRecord() {
		return Record;
	}
	public void setRecord(Trade record) {
		Record = record;
	}
}
