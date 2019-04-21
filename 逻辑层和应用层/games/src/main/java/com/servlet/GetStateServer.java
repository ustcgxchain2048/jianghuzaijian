package com.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.game.Operation;



public class GetStateServer extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();
		String accountId = (String) session.getAttribute("account");
		System.out.println(accountId);
		System.out.println(LoginServe.axClient);
		Boolean fatg = Operation.isFlag(LoginServe.axClient,accountId);
		if(fatg) {
			JSONObject jsonObject= new JSONObject();
			jsonObject.put("flag","1");
			PrintWriter pw = response.getWriter();
			pw.println(jsonObject);
			pw.close();
		}else {
			JSONObject jsonObject= new JSONObject();
			jsonObject.put("flag","0");
			PrintWriter pw = response.getWriter();
			pw.println(jsonObject);
			pw.close();
		}
		
	}
	
}