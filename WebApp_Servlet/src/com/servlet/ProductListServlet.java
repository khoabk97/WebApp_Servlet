package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beans.Product;
import com.utils.DBUtils;
import com.utils.MyUtils;

@WebServlet(urlPatterns = {"/productList"})
public class ProductListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	//Constructor
	public ProductListServlet() {
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoreConnection(req);
		
		String errorString = null;
		List<Product> list = null;
		try {
			list = DBUtils.queryProduct(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		
		//Lưu thông tin mã lỗi, productList vào request att trước khi forward sang views.
		req.setAttribute("errorString", errorString);
		req.setAttribute("productList", list);
		
		//Forward sang productListView.jsp
		RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/productListView.jsp");
		dp.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}
