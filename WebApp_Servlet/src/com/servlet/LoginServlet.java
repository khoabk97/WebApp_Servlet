package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.beans.UserAccount;
import com.utils.DBUtils;
import com.utils.MyUtils;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}
	
	// Hiển thị trang Login.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Forward tới trang /WEB-INF/views/loginView.jsp
		// (Người dùng không thể truy cập trực tiếp
		// vào các trang JSP đặt trong thư mục WEB-INF).
		HttpSession session = req.getSession();
		UserAccount loginedUser = MyUtils.getLoginedUser(session);
		String name="", value="";
		
		//Kiểm tra nếu có nhấn Logout => xoá session và cookie.
		Boolean logout = Boolean.parseBoolean(req.getParameter("logout"));
		if (logout) {
			session.invalidate();
			MyUtils.deleteUserCookie(resp);
			resp.sendRedirect(req.getContextPath()+"/login");
			return;
		}
		
		//Kiểm tra nếu có lưu cookie rồi thì truyền username đã lưu trong cookie qua trang view, nếu ko thì truyền "". 
		boolean cookieSaved = false;
		Cookie[] cookiesa = req.getCookies();
		for(int i = 0; i < cookiesa.length; i++){
			name = cookiesa[i].getName(); // Lấy tên cookie
			if (name.equals("ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE")) {
				value = cookiesa[i].getValue(); // Lấy giá trị
				cookieSaved = true;
			}
			
		}
		if (cookieSaved) {
			req.setAttribute("FINAL_USERNAME", value);
		}
		else {
			req.setAttribute("FINAL_USERNAME", "");
		}
		
		//Forward tới trang view
		RequestDispatcher dp = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
		dp.forward(req, resp);
		
		
	}
	
	
	// Khi người nhập userName & password, và nhấn Submit.
    // Phương thức này sẽ được thực thi.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String rememberMeStr = req.getParameter("rememberMe");
		boolean remember = "Y".equals(rememberMeStr); //Lưu ý!!!
		
		//Check: System.out.println(userName + " " +password + " "+rememberMeStr);
		
		UserAccount user = null;
		boolean hasError = false;
		String errorString = null;
		
		//Kiểm tra userName, password nếu ko có nhập gì cả => yêu cầu nhập
		//Nếu có nhập nhưng tìm ko thấy trong db (sai thông tin) => báo ko có, sử dụng try catch để xử lý lỗi
		if(userName == null || password ==null || userName.length() ==0 || password.length() == 0) {
			hasError = true;
			errorString = "Required username and password!";
		} else {
			Connection conn = MyUtils.getStoreConnection(req);
			try {
				//Tìm user trong DB
				user = DBUtils.findUser(conn, userName, password);
				
				if (user == null) {
					hasError = true;
					errorString = "User Name or password invalid";
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				hasError = true;
				errorString = e.getMessage();
			}
		}
		
		//Trường hợp có lỗi 
		//forward tới /WEB-INF/views/login.jsp
		if(hasError) {
			user = new UserAccount();
			user.setUserName(userName);
			user.setPassword(password);
			
			//Lưu thông tin vào request att trước khi forward
			req.setAttribute("errorString", errorString);
			req.setAttribute("user", user);
			
			//Forward tới login.jsp
			RequestDispatcher dp = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
			dp.forward(req, resp);
		}
		
		//Trường hợp ko có lỗi
		//Lưu thông tin người dùng vào Session, user đã có (userName, password, info) từ try catch
		//Sau đó chuyển hướng sang trang userInfo (Servlet).
		else {
				
			HttpSession session = req.getSession();
			MyUtils.storeLoginedUser(session, user);
			
			//Nếu người dùng chọn tính năng "Remember me" => Lưu cookie. Nếu ko, xoá cookie luôn
			if(remember) {
				MyUtils.storeUserCookie(resp, user);
			}
			else {
				MyUtils.deleteUserCookie(resp);
			}
			
			//Redirect sang /userInfo.
			resp.sendRedirect(req.getContextPath()+"/userInfo");
		}		
	}
	
}
