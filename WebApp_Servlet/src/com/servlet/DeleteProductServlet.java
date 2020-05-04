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

import com.utils.DBUtils;
import com.utils.MyUtils;

@WebServlet(urlPatterns = {"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public DeleteProductServlet() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = MyUtils.getStoreConnection(req);
		
		String code = (String) req.getParameter("code");
		
		String errorString = null;
		
		try {
			DBUtils.deleteProduct(conn, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		
		//Nếu có lỗi, forward (chuyển tiếp) sang trang thông báo lỗi.
		if (errorString != null) {
			// Lưu thông tin vào request attribute trước khi forward sang views.
            req.setAttribute("errorString", errorString);
            
            RequestDispatcher dp = req.getServletContext().getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
            dp.forward(req, resp);
		}
		 // Nếu mọi thứ tốt đẹp.
        // Redirect (chuyển hướng) sang trang danh sách sản phẩm.
        else {
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
