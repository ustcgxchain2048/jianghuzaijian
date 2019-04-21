package com.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.classname.PersonData;
import com.classname.PersonInfor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gxchain.client.GXChainClient;
import com.gxchain.client.domian.TransactionResult;
import com.gxchain.client.domian.params.GetTableRowsParams;
import com.servlet.FindPeopleServe;

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
	/*
	 * 单人作战
	 * */
	/*将个人地址放入地址*/
	public static boolean setAddress(GXChainClient client,String accountId) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		System.out.println(id);
		System.out.println(client);
		TransactionResult ts = client.callContract("player2","login",param,null,true);
		System.out.println(ts);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	/*放入单人作战的成绩*/
	public static boolean setInforGrad(GXChainClient client,String accountId,int score) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		param.addProperty("score", score);
		TransactionResult ts = client.callContract("player2","bonus",param,null,true);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	/*删除单人作战的记录*/
	public static boolean deleteInfor(GXChainClient client,String accountId) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		TransactionResult ts = client.callContract("player2","quit",param,null,true);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	/*
	 * 多人作战
	 * */
	/*删除多人作战*/
	public static boolean deleteTwoInfor(GXChainClient client,String accountId) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		TransactionResult ts = client.callContract("twoplayers","quit",param,null,true);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	/*双人报名插入*/
	public static boolean twoPeopleInfor(GXChainClient client,String accountId) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		client.callContract("twoplayers", "checkplayers", param, null, true);
		return flag;
	}
	/*查找相关数据，判断是否可以双人作战*/
	public static PersonData peoPleSucc(GXChainClient client,String accountId) {
		GetTableRowsParams params = GetTableRowsParams.builder().lowerBound(0).upperBound(-1).limit(10).build();
		JsonObject result = (JsonObject) client.getTableRowsEx("twoplayers", "fightpair", params);
		JsonArray array = result.get("rows").getAsJsonArray();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		PersonData personData = new PersonData();
		personData.setFlag(false);
		for(int i=0;i<array.size();i++) {
			int ans1 = Integer.parseInt(array.get(i).getAsJsonObject().get("id1").toString());
			int ans2 = Integer.parseInt(array.get(i).getAsJsonObject().get("id2").toString());
			int state = Integer.parseInt(array.get(i).getAsJsonObject().get("gamestate").toString());
			if(state == 1 && ans1 == id) {
				personData.setPrivateKey(Integer.parseInt(array.get(i).getAsJsonObject().get("primarykey").toString()));
				personData.setId(ans1);
				personData.setIdflag(1);
				personData.setScore(0);
				personData.setFlag(true);
				break;
			}else if(state == 1 && ans2 == id) {
				personData.setPrivateKey(Integer.parseInt(array.get(i).getAsJsonObject().get("primarykey").toString()));
				personData.setId(ans2);
				personData.setIdflag(2);
				personData.setScore(0);
				personData.setFlag(true);
				break;
			}
		}
		return personData;
	}
	/*将双人成绩传给上链*/
	public static boolean setTwoSigleGrad(HttpServletRequest request,GXChainClient client,int score) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		HttpSession session = request.getSession();
		String accountId = (String) session.getAttribute("account");
		PersonData personData = FindPeopleServe.dataMap.get(accountId);
		param.addProperty("primarykey",personData.getPrivateKey());
		param.addProperty("id",personData.getId());
		param.addProperty("score", score);
		param.addProperty("idflag",personData.getIdflag());
		TransactionResult ts = client.callContract("twoplayers","end2players",param,null,true);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	/*
	 * 多人作战
	 * */
	
	/*将个人地址放入地址中*/
	public static boolean setMoreAddress(GXChainClient client,String accountId) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		param.addProperty("id",id);
		TransactionResult ts = client.callContract("multiplayer","multilogin",param,null,true);
		System.out.println(ts);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	/*修改状态，判断可以运行*/
	public static boolean changestate(GXChainClient client) {
		boolean flag = true;
		JsonObject param = new JsonObject();
		param.addProperty("state",1);
		TransactionResult ts = client.callContract("multiplayer","setstaterun",param,null,true);
		System.out.println(ts);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	/*查询状态是否可以进行作战*/
	public static boolean isFlag(GXChainClient client,String accountId) {
		boolean flag = false;
		GetTableRowsParams params = GetTableRowsParams.builder().lowerBound(0).upperBound(-1).limit(10000).build();
		JsonObject result = (JsonObject) client.getTableRowsEx("multiplayer", "multiplayer", params);
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		System.out.println(result);
		JsonArray array = result.get("rows").getAsJsonArray();
		System.out.println(array);
		for(int i=0;i<array.size();i++) {
			System.out.println();
			int id1 = Integer.parseInt(array.get(i).getAsJsonObject().get("id").toString());
			int point = Integer.parseInt(array.get(i).getAsJsonObject().get("state").toString());
			if(id1 == id && point == 1) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	/*插入成绩*/
	public static boolean inserMoreGrad(GXChainClient client,String accountId,int score) {
		boolean flag = true;
		int id = Integer.parseInt(accountId.substring(accountId.length()-4, accountId.length()));
		JsonObject param = new JsonObject();
		param.addProperty("id",id);
		param.addProperty("score", score);
		TransactionResult ts = client.callContract("multiplayer","setmulsocre",param,null,true);
		if(ts == null) {
			flag = false;
		}
		return flag;
	}
	
	
	
}
