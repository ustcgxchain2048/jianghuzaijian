package com.classname;

public class PersonInfor {
	private String priveteKeyString;
	private String accountId;
	public PersonInfor() {
		// TODO Auto-generated constructor stub
	}
	public PersonInfor(String priveteKeyString,String accountId) {
		// TODO Auto-generated constructor stub
		this.priveteKeyString = priveteKeyString;
		this.accountId = accountId;
	}
	public String getPriveteKeyString() {
		return priveteKeyString;
	}
	public void setPriveteKeyString(String priveteKeyString) {
		this.priveteKeyString = priveteKeyString;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
