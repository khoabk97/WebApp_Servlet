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

@WebServlet(urlPatterns = {"/editProduct"})
public class EditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public EditProductServlet() {
		super();
	}
	
	//Hiển thị trang sửa sản phẩm
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoreConnection(req);
		String code = (String) req.getParameter("code");
		Product product = null;
		String errorString = null;
		
		try {
			product = DBUtils.findProduct(conn, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		
		// Các trường hợp: Không có lỗi, Sản phẩm không tồn tại để edit. (mặc định dùng &&, phải dùng || mới đúng chứ)
        // Redirect sang trang danh sách sản phẩm.
		if(errorString != null || product ==null) {
			resp.sendRedirect(req.getServletPath()+"/productList");
			return;
		}
		
		//Ngược lại => sẽ forward tới trang editProductView.
		req.setAttribute("errorString", errorString);
		req.setAttribute("product", product);
		
		RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
		dp.forward(req, resp);		
	}
	
	
	// Sau khi người dùng sửa đổi thông tin sản phẩm, và nhấn Submit.
    // Phương thức này sẽ được thực thi.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Lấy thông tin product và update
		Connection conn = MyUtils.getStoreConnection(req);
		String code = (String) req.getParameter("code");
		String name = (String) req.getParameter("name");
		String priceStr = (String) req.getParameter("price");
		float price=0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
		}
		Product product = new Product(code, name, price);
		
		String errorString = null;
		
		try {
			DBUtils.updateProduct(conn, product);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		
		// Lưu thông tin vào request attribute trước khi forward sang views.
		req.setAttribute("errorString", errorString);
		req.setAttribute("product", product);
		
		//Nếu có lỗi sẽ Forward lại trang editView.
		if(errorString!=null) {
			RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
			dp.forward(req, resp);
		}
		// Nếu mọi thứ tốt đẹp.
        // Redirect sang trang danh sách sản phẩm.
		else {
			resp.sendRedirect(req.getContextPath()+"/productList");
		}
	}
	
}
