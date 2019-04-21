package com.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gxchain.client.GXChainClient;
import com.gxchain.client.domian.params.GetTableRowsParams;


public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*1.登录 
		 * 链码：公钥哈希地址
		 * */
		String priveteKeyString = "5J9AUPA9YUBqfKyyG3Jhdos7TdsfAXnzyzEJacm84a5c1G4j73r";
		String accountId = "1.2.2617";
		String node = "wss://testnet.gxchain.org";
		
		String priString = "5JoxBPVR2dYtq64U1BoGXZ9NFbZG66FRgpj9fTWEZcY37odc2UE";
		String accIdString ="1.2.2612";
		
		try {
			GXChainClient client1 = new GXChainClient(priveteKeyString, accountId,node);
//			Operation.changestate(client1);
			//Operation.isFlag(client1, accountId);
			Operation.inserMoreGrad(client1,accountId,100);
//			Operation.peoPleSucc(client1, accountId);
			
			
//			JsonObject param1 = new JsonObject();
//			int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
//			param1.addProperty("id",id);
//			client1.callContract("twoplayers", "checkplayers", param1, null, true);
//			
//			GXChainClient client2 = new GXChainClient(priString, accIdString,node);
//			JsonObject param2 = new JsonObject();
//			int id2 = Integer.parseInt(accIdString.substring(accIdString.length()-4, accIdString.length()));
//			param1.addProperty("id",id2);
//			client1.callContract("twoplayers", "checkplayers", param1, null, true);
			
//			GetTableRowsParams params = GetTableRowsParams.builder().lowerBound(0).upperBound(-1).limit(10).build();
//			JsonObject result = (JsonObject) client.getTableRowsEx("player2", "player", params);
//			JsonArray array = result.get("rows").getAsJsonArray();
//			for(int i=0;i<array.size();i++) {
//				System.out.println(array.get(i).getAsJsonObject().get("id"));
//			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("124");
		}
	}

}
