package com.connectdb;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
	public static Connection getConnection()
	throws ClassNotFoundException,SQLException{
		
		//Hàm thực hiện gọi tới hàm getconn mặc định của MySQLconn-> hàm getconn có đối số
		return MySQLConnUtils.getMySQLConnection();
	}
	
	//Ngắt kết nối tới DB.
	public static void closeQuietly(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
		}
	}
	
	//khi bạn đã thực hiện các thay đổi với cơ sở dữ liệu và muốn ký thác các thay đổi đó, bạn sử dụng phương thức commit()
	//Khi bạn muốn xóa các thay đổi đã được thực hiện trước đó để quay về trạng thái trước khi thực hiện thay đổi khi thấy rằng có lỗi xảy ra, bạn sử dụng phương thức rollback().
	public static void rollbackQuietly(Connection conn) {
		try {
			conn.rollback();
		} catch (Exception e) {
		}
	}
}
