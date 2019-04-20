package com.game;

import org.apache.commons.collections.bag.TreeBag;

import com.classname.PersonInfor;
import com.google.gson.JsonObject;
import com.gxchain.client.GXChainClient;

public class Operation {
	/*判断登录是否成功*/
	public static GXChainClient isLogin(PersonInfor personInfor) {
		String node = "wss://testnet.gxchain.org";
		GXChainClient client = null;
		try {
			client = new GXChainClient(personInfor.getPriveteKeyString(), personInfor.getAccountId(),node);
			return client;
		} catch (Exception e) {
			// TODO: handle exception
			return client;
			
		}
	}
	
	/*将个人地址放入*/
}
