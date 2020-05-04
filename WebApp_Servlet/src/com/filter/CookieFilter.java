package com.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.beans.UserAccount;
import com.utils.DBUtils;
import com.utils.MyUtils;

@WebFilter(filterName = "CookieFilter", urlPatterns = {"/*"})
public class CookieFilter implements Filter{
	//Constructor mặc định
	public CookieFilter() {
		super();
	}
	
	//Các phương thức cần override: init, destroy, doFilter
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	@Override
	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//JDBCFilter chạy trước, CookieFilter chạy sau
		//Cookie sẽ được store khi nhấn "Remember Me", còn session bắt đầu khi user truy cập hệ thống lần đầu, 
		//kết thúc khi user thoát khỏi hệ thống 	
	
		//Khởi tạo 1 session
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
	
		//Lấy giá trị session (get ATT LoginUser) nếu đã có set ATT trước đó. Ở đây do lần đâu nên là null
		UserAccount userInSession = MyUtils.getLoginedUser(session);
	
		//Lần đầu null ko xét. Lần sau truy cập lại URL, user chưa thoát sẽ set ATT Cookie_checked, chuyển sang filter tiếp theo 
		if (userInSession != null) {		
			session.setAttribute("COOKIE_CHECKED","CHECKED");
			chain.doFilter(request, response);
			return;
		}
		
		//Nếu là lần đầu
		//Lấy đối tượng conn đã tạo ở JDBC Filter được lưu trong request.
		Connection conn = MyUtils.getStoreConnection(request);
	
		//Kiểm tra cookie_checked, nếu có kết nối nhưng chưa lưu cookie, tạo đối tượng user được tìm = DBUtils.FindUser(UserName)
		//store đối tượng user vào session (set LoginUser)
		String checked = (String) session.getAttribute("COOKIE_CHECKED");
		if(checked == null && conn!=null) {
			String userName = MyUtils.getUserNameInCookie(req);	//Tạo cookie rồi lấy, lần đầu cookie UserName = null
			try {
				UserAccount user = DBUtils.findUser(conn, userName);
				MyUtils.storeLoginedUser(session, user);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
			//Đánh dấu đã kiểm tra Cookie.
			session.setAttribute("COOKIE_CHECKED", "CHECKED");
		}
		chain.doFilter(request, response);
	}
	
}
