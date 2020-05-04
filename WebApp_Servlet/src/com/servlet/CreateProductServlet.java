package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.Product;
import com.utils.DBUtils;
import com.utils.MyUtils;

@WebServlet("/createProduct")
public class CreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//Constructor
    public CreateProductServlet() {
        super();
    }

    // Hiển thị trang tạo sản phẩm.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
		dp.forward(req, resp);
	}
	
	// Khi người dùng nhập các thông tin sản phẩm, và nhấn Submit.
    // Phương thức này sẽ được gọi.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoreConnection(req);
		
		String code = (String) req.getParameter("code");
		String name = (String) req.getParameter("name");
		String priceStr = (String) req.getParameter("price");
		float price=0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Product product = new Product(code, name, price);
		String errorString = null;
		
		// Mã sản phẩm phải là chuỗi chữ [a-zA-Z_0-9]
        // Có ít nhất một ký tự.
		// Nếu sai sẽ xuất lỗi error String.=> forward sang trang create.jsp (Tạo mới lại)
		String regex="\\w+";
		
		
		if (code == null || !code.matches(regex)) {
			errorString = "Product Code invalid!";
		}
		
		if(errorString != null) {
			// Lưu thông tin vào request attribute trước khi forward sang views.
			// Nếu mọi thứ tốt đẹp thì Redirect (chuyển hướng) sang trang danh sách sản phẩm.
			req.setAttribute("errorString", errorString);
			req.setAttribute("product", product);
			RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
			dp.forward(req, resp);
		}
		else {
			// Nếu mọi thứ tốt đẹp.
	        // Redirect (chuyển hướng) sang trang danh sách sản phẩm.
			try {
				DBUtils.insertProduct(conn, product);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
			resp.sendRedirect(req.getContextPath()+"/productList");
		}
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
