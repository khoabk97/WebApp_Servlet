package com.filter;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.connectdb.ConnectionUtils;
import com.utils.MyUtils;

@WebFilter(filterName = "JDBCFilter", urlPatterns = {"/*"})
public class JDBCFilter implements Filter{
	//Constructor mặc định
	public JDBCFilter() {
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
		 HttpServletRequest req = (HttpServletRequest) request;
	    
		 // Chỉ mở connection (kết nối) đối với các request có đường dẫn đặc biệt.   // (Chẳng hạn đường dẫn tới các servlet, jsp, ..)
	     // Tránh tình trạng mở Connection với các yêu cầu thông thường.     // (Chẳng hạn image, css, javascript,... )
		 
		 if(this.needJDBC(req)) {
			 //Check: System.out.println("Open connection for: "+req.getServletPath());
			 Connection conn = null;
			 
			 //Thực hiện cố gắng tạo kết nối tới db, nếu lỗi sẽ rollback dữ liệu về lại trước khi bị lỗi và sau cùng là close kết nối.
			 try {
				//Tạo đối tượng Connection kết nối database dùng lớp ConnectionUtils + MySQLConnUtils
				conn = ConnectionUtils.getConnection();
				// Set tự động commit false, để chủ động điều khiển.
				conn.setAutoCommit(false);
				
				//Lưu trữ đối tượng Connection vào attribute của request dùng lớp MyUtils
				MyUtils.storeConnection(request, conn);
				
				//Cho phép request đi tiếp.
				//Đi tới Filter tiếp theo hoặc tới mục tiêu, xem thứ tự filter trong web.xml
				chain.doFilter(request, response);
				
				//Gọi phương thức commit() để hoàn thành giao dịch với DB, chỉ dùng khi autocommit bị disable
				conn.commit();
				
			} catch (Exception e) {
				e.printStackTrace();
				ConnectionUtils.rollbackQuietly(conn);
				throw new ServletException();
			}
			 finally {
				 ConnectionUtils.closeQuietly(conn);
			 }
		 }
		 else {
			// Với các request thông thường (image,css,html,..) không cần mở connection.
			// Cho phép request đi tiếp.(Đi tới Filter tiếp theo hoặc đi tới mục tiêu).
			 chain.doFilter(request, response);
		 }
	}
	
	
	
	// http://localhost:8080/ServletTutorial/spath/abc/mnp?p1=1
	//ContextPath: /ServletTutorial
	//ServletPath: /spath
	//pathInfo: /abc/mnp
	//queryString: ?p1=1
	// Kiểm tra mục tiêu của request hiện tại là 1 Servlet?
	private boolean needJDBC(HttpServletRequest request) {
		//Check: System.out.println("JDBC Filter");
		
		// Servlet Url-pattern: /spath/*
		String servletPath = request.getServletPath();
		
		//pathInfo: /abc/mnp
		String pathInfo = request.getPathInfo();
		
		//Nếu URL là.../spath => vào đúng serlvet -> cho phép
		//Nếu URL là .../spath/abc/mnp => vào trang con của servlet -> cho phép, còn nếu là trường hợp khác sẽ ko cho.
		//trường hợp dưới sẽ để dạng là /spath/*
		String urlPattern = servletPath;
		
		if (pathInfo != null) {
			// => /spath/*
			urlPattern = servletPath + "/*";
		}
		
		//Tạo 1 map lưu trữ key+value servletRegistrations, trong đối tượng servletContext gồm nhìu thứ: realpath, ...
		// Key: servletName.
        // Value: ServletRegistration
		Map<String, ? extends ServletRegistration> servletRegistrations = request.getServletContext().getServletRegistrations();
		
		//Dò tất cả các servlet trong WebApp, nếu trùng urlPattern thì return true, mở connection.
		Collection<? extends ServletRegistration> values = servletRegistrations.values();
		for (ServletRegistration sr : values) {
            Collection<String> mappings = sr.getMappings();		//Lấy đoạn mapping kiểm tra có chứa urlPattern
            if (mappings.contains(urlPattern)) {				
                return true;
            }
        }
        return false;
	}

}
