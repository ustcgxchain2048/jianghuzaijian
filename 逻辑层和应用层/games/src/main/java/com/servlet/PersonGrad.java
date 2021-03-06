package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.game.Main;
import com.game.Operation;



public class PersonGrad extends HttpServlet{

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
		String grade = request.getParameter("grade");
		String typeString = (String) session.getAttribute("type");
		System.out.println("*****************************************************");
		System.out.println(accountId);
		System.out.println(grade);
		System.out.println(typeString);
		System.out.println("*****************************************************");
		if(typeString.equals("one")) {
			Operation.setInforGrad(LoginServe.axClient, accountId, Integer.parseInt(grade));
		}else if(typeString.equals("two")){
			Operation.setTwoSigleGrad(request,LoginServe.axClient,Integer.parseInt(grade));
		}else {
			Operation.inserMoreGrad(LoginServe.axClient,accountId,Integer.parseInt(grade));
		}
		
		
	}
	
}
