package com.synit.domain;

public class EmployeeDto {
	
	private String email;
	private String name;
	private String managerId;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	
	public EmployeeDto(String email, String name, String managerId) {
		this.email = email;
		this.name = name;
		this.managerId = managerId;
	}

}
