package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.classname.PersonInfor;
import com.game.Operation;
import com.gxchain.client.GXChainClient;



public class LoginServe extends HttpServlet{
	public static GXChainClient axClient;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		PersonInfor personInfor = new PersonInfor();
		personInfor.setPriveteKeyString(request.getParameter("activePrivateKey"));
		personInfor.setAccountId(request.getParameter("accountIdOrName"));
		if(Operation.isLogin(personInfor)!=null) {
			LoginServe.axClient = Operation.isLogin(personInfor);
			HttpSession session = request.getSession();
			session.setAttribute("account", personInfor.getAccountId());
			System.out.println(personInfor.getPriveteKeyString());
			System.out.println(personInfor.getAccountId());
			JSONObject jsonObject= new JSONObject();
			jsonObject.put("flag","1");
			PrintWriter pw = response.getWriter();
			pw.println(jsonObject);
			pw.close();
		} else {
			JSONObject jsonObject= new JSONObject();
			jsonObject.put("flag","0");
			PrintWriter pw = response.getWriter();
			pw.println(jsonObject);
			pw.close();
		}
	}
	
}
