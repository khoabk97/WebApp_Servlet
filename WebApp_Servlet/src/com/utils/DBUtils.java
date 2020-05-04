package com.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.beans.Product;
import com.beans.UserAccount;

public class DBUtils {
	//Tìm user dựa vào userName+pass
	public static UserAccount findUser(Connection conn, String userName, String password)
	throws SQLException {
		String sql = "Select a.User_Name, a.Password, a.Gender from User_Account a where a.User_Name = ? and a.password= ? ";
		
		//Gửi câu lệnh SQL đến cơ sở dữ liệu và thực thi truy vấn
		//ở đây dùng PreparedStatement chuyên dùng để thực hiện các truy vấn được tham số hóa
		//Select a.User_Name, a.Password, a.Gender 
		//From User_Account a
		//Where a.User_Name = ? and a.password= ?
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, userName);
		pstm.setString(2, password);
		ResultSet rs = pstm.executeQuery();
		
		//Nếu có trả về user với đầy đủ userName, password, gender lấy được thông tin từ db
		//Nếu ko trả về null
		if(rs.next()) {
			String gender = rs.getString("Gender");
			UserAccount user = new UserAccount();
			user.setUserName(userName);
			user.setPassword(password);
			user.setGender(gender);
			return user;
		}
		return null;
	
	}
	
	//Tìm user dựa vào userName, overloading method
	public static UserAccount findUser(Connection conn, String userName)
			throws SQLException {
				String sql = "Select a.User_Name, a.Password, a.Gender from User_Account a"
							+" where a.User_Name = ?"; //Lưu ý nếu nối chuỗi phải có dấu cách
				
				//Gửi câu lệnh SQL đến cơ sở dữ liệu và thực thi truy vấn
				//ở đây dùng PreparedStatement chuyên dùng để thực hiện các truy vấn được tham số hóa
				//Select a.User_Name, a.Password, a.Gender 
				//From User_Account a
				//Where a.User_Name = ?
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setString(1, userName);
				ResultSet rs = pstm.executeQuery();
				
				//Nếu có trả về user với đầy đủ userName, password, gender lấy được thông tin từ db
				//Nếu ko trả về null
				if(rs.next()) {
					String password = rs.getString("Password");
					String gender = rs.getString("Gender");
					UserAccount user = new UserAccount();
					user.setUserName(userName);
					user.setPassword(password);
					user.setGender(gender);
					return user;
				}
				return null;
			
			}
	
	//Truy vấn chọn toàn bộ bảng thông tin Product gồm Code, Name và Price.(do nhiều hàng nên sài List, vòng While chạy)
	public static List<Product> queryProduct (Connection conn)
	throws SQLException {
		String sql = "Select a.Code, a.Name, a.Price from Product a";
		PreparedStatement pstm = conn.prepareStatement(sql);
		ResultSet rs = pstm.executeQuery();
		
		List<Product> list = new ArrayList<Product>();
		while(rs.next()) {
			String code = rs.getString("Code");
			String name = rs.getString("Name");
			Float price = rs.getFloat("Price");
			Product product = new Product();
			product.setCode(code);
			product.setName(name);
			product.setPrice(price);
			list.add(product);
		}
		return list;
		
	}
	
	//Truy vấn tìm 1 Product khi có mã code = ... (1 hàng data nên ko sài List, xuất ra thẳng đối tượng)
	public static Product findProduct(Connection conn,String code)
	throws SQLException{
		String sql = "Select a.Code, a.Name, a.Price from Product a"
					+" where a.Code = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1,code);
		ResultSet rs = pstm.executeQuery();
		
		while(rs.next()) {
			String name = rs.getString("Name");
			Float price = rs.getFloat("Price");
			Product product = new Product(code, name, price);
			return product;
		}
		return null;
	}
	
	//Truy vấn tìm: ResultSet rs = ptsm.exercuteQuery(); => rs.get...
	//Truy vấn cập nhật: ptsm.exercuteUpdate()
	
	//Truy vấn cập nhật Name, Price với mã Code đã biết của 1 Product hiện hữu.
	public static void updateProduct(Connection conn, Product product)
	throws SQLException{
		String sql = "Update Product set Name = ?, Price = ?"
					+" Where Code = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, product.getName());
		pstm.setFloat(2, product.getPrice());
		pstm.setString(3,product.getCode());
		pstm.executeUpdate();
		
	}
	
	//Cập nhật: Thêm dữ liệu vào table Product.
	public static void insertProduct(Connection conn, Product product)
	throws SQLException {
		String sql = "Insert into Product(Code, Name, Price) values(?,?,?)";
		
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1,product.getCode());
		pstm.setString(2, product.getName());
		pstm.setFloat(3, product.getPrice());
		pstm.executeUpdate();
	}
	
	//Xoá sản phẩm dựa vào Code.
	public static void deleteProduct(Connection conn, String code) 
	throws SQLException {
		String sql = "Delete from Product"
					+" where Code = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1,code);
		pstm.executeUpdate();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
