package com.game;

import com.google.gson.JsonObject;
import com.gxchain.client.GXChainClient;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*1.登录 
		 * 链码：公钥哈希地址
		 * */
		String priveteKeyString = "5J9AUPA9YUBqfKyyG3Jhdos7TdsfAXnzyzEJacm84a5c1G4j73r";
		String accountId = "1.2.2617";
		String node = "wss://testnet.gxchain.org";
		
		try {
			GXChainClient client = new GXChainClient(priveteKeyString, accountId,node);
			JsonObject param = new JsonObject();
			int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
			

			param.addProperty("id",id);
			param.addProperty("score", 3000);
			client.callContract("player2","bonus",param,null,true);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("124");
		}
	}

}
