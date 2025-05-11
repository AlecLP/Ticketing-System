package com.synit.domain;

import com.synit.common_dtos.EmployeeDto;

import jakarta.persistence.Embeddable;

@Embeddable
public class Employee {
	
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
	public EmployeeDto toEmployeeDto() {
		EmployeeDto dto = new EmployeeDto();
		dto.setEmail(email);
		dto.setName(name);
		dto.setManagerId(managerId);
		return dto;
	}
	
	public Employee() {}

}
