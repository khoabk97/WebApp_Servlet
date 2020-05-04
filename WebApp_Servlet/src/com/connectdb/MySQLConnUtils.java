package com.connectdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnUtils {
	public static Connection getMySQLConnection() throws ClassNotFoundException, SQLException{
		//Dữ liệu đăng nhập mặc định để kết nối tới DB MYSQL nếu gọi phương thức getconn() ko đối số.
		// Chú ý: Thay đổi các thông số kết nối cho phù hợp.
		String hostName = "localhost";
		String dbName = "webapp_servlet";
		String userName = "root";
		String password = "123456";
		return getMySQLConnection(hostName, dbName, userName, password);
		
	}

	public static Connection getMySQLConnection(String hostName, String dbName, String userName, String password) 
	throws ClassNotFoundException, SQLException
	{
	     // Khai báo class Driver cho DB MySQL
	     // Việc này cần thiết với Java 5, Java6 tự động tìm kiếm Driver thích hợp.
	     // Nếu bạn dùng Java6, thì ko cần dòng này cũng được.
		Class.forName("com.mysql.jdbc.Driver");
		
		//connection URL = "jdbc:mysql://localhost:3306/o7_udtonghop"
		String connectionURL = "jdbc:mysql://" + hostName + ":3306/"+dbName;
		
		//Tạo đối tượng conn với URL truyền vào, trả về lại hàm gọi ban đầu.
		Connection conn = DriverManager.getConnection(connectionURL, userName, password);
		return conn;
	}
}
