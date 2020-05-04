package com.beans;

public class UserAccount {
	
	//Khai báo 3 biến thuộc tính của UserAccount
	private String userName;
	private String gender;
	private String password;
	
	//Contructor mặc định
	public UserAccount() {

	}

	//Getter, setter các thuộc tính
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
