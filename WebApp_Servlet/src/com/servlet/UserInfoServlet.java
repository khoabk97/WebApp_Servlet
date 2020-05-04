package com.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.beans.UserAccount;
import com.utils.MyUtils;

@WebServlet(urlPatterns = {"/userInfo"})
public class UserInfoServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//Constructor mặc định 
	public UserInfoServlet() {
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		//Check xem cookie có lưu chưa.
		/*
		 * Cookie[] cookiesa = req.getCookies(); for(int i = 0; i < cookiesa.length;
		 * i++){ String name = cookiesa[i].getName(); // Lấy tên cookie String value =
		 * cookiesa[i].getValue(); // Lấy giá trị System.out.println(name + " *** "+
		 * value); }
		 */
		
		//Kiểm tra người dùng đã đăng nhập (login) chưa.
		//Nếu chưa đăng nhập thì redirect về lại trang login.
		UserAccount loginedUser = MyUtils.getLoginedUser(session);
		if(loginedUser == null) {
			resp.sendRedirect(req.getContextPath()+"/login");
			return;
		}
		// Lưu thông tin vào request attribute trước khi forward (chuyển tiếp).
        req.setAttribute("user", loginedUser);
		
		//Nếu đã đăng nhập rồi (có loginedUser), lưu thông tin user vào req, và forward tới userInfoView.jsp
		RequestDispatcher dp = this.getServletContext().getRequestDispatcher("/WEB-INF/views/userInfoView.jsp");
		dp.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
